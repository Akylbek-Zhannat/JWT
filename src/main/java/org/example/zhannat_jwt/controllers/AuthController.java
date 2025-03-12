package org.example.zhannat_jwt.controllers;

import org.example.zhannat_jwt.models.AuthRequest;
import org.example.zhannat_jwt.models.User;
import org.example.zhannat_jwt.security.JwtUtil;
import org.example.zhannat_jwt.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        UserDetails user = userService.loadUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Неверные учетные данные");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.registerUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok("Пользователь успешно зарегистрирован!");
    }


}
