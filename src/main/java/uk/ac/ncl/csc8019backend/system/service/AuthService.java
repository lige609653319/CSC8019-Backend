package uk.ac.ncl.csc8019backend.system.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.csc8019backend.system.common.ResultCode;
import uk.ac.ncl.csc8019backend.system.dto.RegisterRequest;
import uk.ac.ncl.csc8019backend.system.dto.UserInfoResponse;
import uk.ac.ncl.csc8019backend.system.entity.Role;
import uk.ac.ncl.csc8019backend.system.entity.User;
import uk.ac.ncl.csc8019backend.system.exception.CustomException;
import uk.ac.ncl.csc8019backend.system.repository.UserRepository;
import uk.ac.ncl.csc8019backend.system.security.CustomUserDetails;
import uk.ac.ncl.csc8019backend.system.security.SecurityUtils;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserInfoResponse getCurrentUserInfo() {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUserDetails();
        if (userDetails == null) {
            throw new CustomException(ResultCode.UNAUTHORIZED, "Unauthorized");
        }

        return UserInfoResponse.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .role(uk.ac.ncl.csc8019backend.system.entity.Role.valueOf(userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")))
                .build();
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new CustomException(ResultCode.BAD_REQUEST, "Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException(ResultCode.BAD_REQUEST, "Email already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.CLIENT)
                .build();

        userRepository.save(user);
    }
}
