package ru.itmo.lab1.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.itmo.lab1.api.AuthRequest;
import ru.itmo.lab1.api.AuthResponse;
import ru.itmo.lab1.api.DataResponse;
import ru.itmo.lab1.entity.User;
import ru.itmo.lab1.repo.UserRepo;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public final class MainService {
    private static final long expiration = 1000 * 60 * 60 * 6; // 6 час
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");
    private final SecretKey secretKey;
    private final UserRepo userRepo;

    public MainService(@Value("${jwt.secret}") String secret,
                       UserRepo userRepo) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.userRepo = userRepo;
    }

    public String authenticate(String token) {
        var parser = Jwts.parser().verifyWith(secretKey).build();
        var decoded = parser.parseSignedClaims(token).getPayload();
        return decoded.getSubject();
    }

    public AuthResponse register(AuthRequest request) {
        if (userRepo.existsById(request.getUsername())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Username is already in use");
        }
        if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters and contain at least one uppercase character and one special character");
        }

        String password = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(password);
        user = userRepo.save(user);

        AuthResponse response = new AuthResponse();
        response.setUsername(user.getUsername());
        response.setToken(createToken(user.getUsername()));
        return response;
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findById(request.getUsername())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.FORBIDDEN, "Username not found"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Wrong password");
        }

        AuthResponse response = new AuthResponse();
        response.setUsername(user.getUsername());
        response.setToken(createToken(user.getUsername()));
        return response;
    }

    private String createToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public void saveData(String username, String data) {
        User user = userRepo.findById(username).orElseThrow();
        user.setData(data);
        userRepo.save(user);
    }

    public DataResponse getData(String username) {
        User user = userRepo.findById(username).orElseThrow();
        return new DataResponse(user.getData());
    }

}
