package com.taskflow.task.web;

import com.taskflow.user.persistence.UserEntity;
import com.taskflow.user.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create-user")
    public ResponseEntity<UserEntity> createTestUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser" + System.currentTimeMillis());  // Unique
        user.setPassword("temppassword");
        user.setEmail("test" + System.currentTimeMillis() + "@test.com");
        user.setRoles("ROLE_USER");

        UserEntity saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }
}