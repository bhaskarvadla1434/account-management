package com.banking.account_management.controller;

import com.banking.account_management.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for login and JWT token generation")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "Login to get JWT token",
            description = "Authenticate using username and password. Returns a JWT token on success."
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        // For demo purpose, using hardcoded credentials
        if ("user".equals(username) && "password".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
