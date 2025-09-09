package ru.itmo.lab1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.lab1.api.AuthRequest;
import ru.itmo.lab1.service.MainService;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        var response = mainService.register(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        var response = mainService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    // ---------- Secured ----------

    @PutMapping("/api/data")
    public ResponseEntity<?> putData(@RequestBody String data,
                                     @RequestAttribute("username") String username) {
        mainService.saveData(username, data);
        return ResponseEntity.ok("Saved");
    }

    @GetMapping("/api/data")
    public ResponseEntity<?> getData(@RequestAttribute("username") String username) {
        var data = mainService.getData(username);
        return ResponseEntity.ok(data);
    }

}
