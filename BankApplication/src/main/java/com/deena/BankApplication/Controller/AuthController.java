package com.deena.BankApplication.Controller;

import com.deena.BankApplication.AuthService.AuthService;
import com.deena.BankApplication.Exception.BadRequestException;
import com.deena.BankApplication.Util.AuthResponse;
import com.deena.BankApplication.Util.LoginRequest;
import com.deena.BankApplication.Util.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Public registration - role is ignored (always USER)
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    // Admin-only: create a user and optionally set role
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-user")
    public ResponseEntity<AuthResponse> createUserByAdmin(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerByAdmin(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}

