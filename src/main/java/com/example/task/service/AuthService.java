package com.example.task.service;


import com.example.task.config.CustomUserDetailService;
import com.example.task.config.JwtService;
import com.example.task.dto.auth.LoginDto;
import com.example.task.dto.auth.LoginResponseDto;
import com.example.task.dto.auth.RegistrationDto;
import com.example.task.dto.response.ResponseDto;
import com.example.task.dto.user.ProfileResponseDto;
import com.example.task.entity.User;
import com.example.task.exp.EmailAlreadyExistsException;
import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailService;
    private final MailService mailService;

    public ResponseEntity<?> registration(RegistrationDto dto) {

        try {
            Pattern compile = Pattern.compile("^\\S+@\\S+\\.\\S+$");
            boolean isValidEmail = compile.matcher(dto.getEmail()).find();
            if (!isValidEmail)
                return ResponseDto.response("Email is not valid", HttpStatus.BAD_REQUEST);

            Optional<User> exists = repository.findByEmail(dto.getEmail());
            if (exists.isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists");
            }

            User user = new User();
            user.setFirstName(dto.getFirstname());
            user.setLastName(dto.getLastname());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

            sendCodeToEmail(user);

            repository.save(user);
            ProfileResponseDto profileResponseDto = getDTO(user);
            return ResponseDto.response(profileResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            throw new MailSendException("Conflict with mail");
        }
    }

    public void sendCodeToEmail(User user) {
        int code = (new Random().nextInt(999999));
        String emailCode = String.valueOf(code).substring(0, 4);
        user.setEmailCode(emailCode);
        mailService.sendEmail(user.getEmail(), " code :", emailCode);
    }

    public ProfileResponseDto getDTO(User entity) {

        ProfileResponseDto profileDTO = new ProfileResponseDto();
        profileDTO.setId(entity.getId());
        profileDTO.setFirstname(entity.getFirstName());
        profileDTO.setLastname(entity.getLastName());
        profileDTO.setEmail(entity.getEmail());
        return profileDTO;
    }

    public ResponseEntity<?> verifyEmail(String email, String code) {
        Optional<User> optionalUser = repository.findByEmail(email);
        if (optionalUser.isEmpty())
            return ResponseDto.response("Such user not found", HttpStatus.NOT_FOUND);
        User user = optionalUser.get();
        if (user.getEmailCode().equals(code)) {
            user.setEnabled(true);
            user.setEmailCode(null);
            repository.save(user);
        } else {
            return ResponseDto.response("Code is wrong", HttpStatus.BAD_REQUEST);
        }
        return ResponseDto.response("User enabled", HttpStatus.OK);
    }

    public ResponseEntity<?> login(LoginDto dto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        User principal = (User) authenticate.getPrincipal();
        var accessToken = jwtService.generateAccessToken(principal);
        LoginResponseDto responseDto = LoginResponseDto.builder()
                .role(principal.getUserRole())
                .accessToken(accessToken)
                .build();
        return ResponseDto.response(responseDto, HttpStatus.OK);
    }

}
