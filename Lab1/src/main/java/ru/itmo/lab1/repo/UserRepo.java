package ru.itmo.lab1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.lab1.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
}
