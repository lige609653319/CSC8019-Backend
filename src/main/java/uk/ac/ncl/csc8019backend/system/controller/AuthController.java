package uk.ac.ncl.csc8019backend.system.controller;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ncl.csc8019backend.system.common.Result;
import uk.ac.ncl.csc8019backend.system.dto.LoginRequest;
import uk.ac.ncl.csc8019backend.system.dto.LoginResponse;
import uk.ac.ncl.csc8019backend.system.dto.RegisterRequest;
import uk.ac.ncl.csc8019backend.system.dto.UserInfoResponse;
import uk.ac.ncl.csc8019backend.system.security.JwtUtils;
import uk.ac.ncl.csc8019backend.system.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }

    @GetMapping("/me")
    public Result<UserInfoResponse> getCurrentUserInfo() {
        return Result.success(authService.getCurrentUserInfo());
    }

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return Result.success(null, "User registered successfully");
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        LoginResponse data = new LoginResponse(token, "Bearer ");

        return Result.success(data, "Login successful");
    }
}
