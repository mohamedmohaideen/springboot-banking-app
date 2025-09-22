package com.deena.BankApplication.AuthService;

import com.deena.BankApplication.BankRepository.UserRepository;
import com.deena.BankApplication.Exception.BadRequestException;
import com.deena.BankApplication.UserEntity.Role;
import com.deena.BankApplication.UserEntity.User;
import com.deena.BankApplication.Util.AuthResponse;
import com.deena.BankApplication.Util.LoginRequest;
import com.deena.BankApplication.Util.RegisterRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Public registration endpoint — always creates a USER. Ignore role field in request.
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        existingUser.ifPresent(userRepository::delete);

        // Assign default role USER if none provided
        Role roleToAssign = registerRequest.getRole() == null ? Role.USER : registerRequest.getRole();

        User user = User.builder()
                .userName(registerRequest.getUserName().trim())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail().trim())
                .role(roleToAssign)
                .build();

        User savedUser = userRepository.save(user);
        String accessToken = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .userName(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }


    /**
     * Admin creates a user and may set a role. Caller must be ADMIN (controller enforces).
     */
    public AuthResponse registerByAdmin(RegisterRequest registerRequest) {
        if (existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("User already registered with email: " + registerRequest.getEmail());
        }

        Role roleToAssign = registerRequest.getRole() == null ? Role.USER : registerRequest.getRole();

        User user = User.builder()
                .userName(registerRequest.getUserName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(roleToAssign)
                .build();

        User savedUser = userRepository.save(user);
        String accessToken = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .userName(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        // 1. Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 2. Fetch user from DB
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail()));

        // 3. Generate JWT
        String accessToken = jwtService.generateToken(user);

        // 4. Return response
        return AuthResponse.builder()
                .accessToken(accessToken)
                .userName(user.getEmail())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

}

