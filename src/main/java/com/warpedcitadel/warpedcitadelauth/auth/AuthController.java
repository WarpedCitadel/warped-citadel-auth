package com.warpedcitadel.warpedcitadelauth.auth;

import com.warpedcitadel.warpedcitadelauth.auth.dto.*;
import com.warpedcitadel.warpedcitadelauth.payload.ApiResponse;
import com.warpedcitadel.warpedcitadelauth.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;


@RestController
@RequestMapping(path = "/auth", version = "1.0")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    private ResponseEntity<ApiResponse<UserReferenceDto>> loginAppUser(@RequestBody UserLoginDto user,
                                                                       WebRequest request) {
        UserReferenceDto userDto = authService.loginUser(user);

        ApiResponse<UserReferenceDto> response = new ApiResponse<>("Logged in",
                HttpStatus.OK.value(),
                userDto,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));

        String jwtToken = jwtUtil.generateToken(userDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


    @PostMapping("/signup")
    private ResponseEntity<ApiResponse<UserVerificationDto>> createAppUser(@Valid @RequestBody UserSignupDto user,
                                                                           WebRequest request) {

        UserVerificationDto verificationData = authService.createAppUser(user);
        ApiResponse<UserVerificationDto> response = new ApiResponse<>("Account created",
                HttpStatus.CREATED.value(),
                verificationData,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/activate")
    private ResponseEntity<ApiResponse<String>> activateAccount(@RequestBody VerificationTokenDto verification,
                                                                WebRequest request) {

        authService.accountVerification(verification);
        ApiResponse<String> response = new ApiResponse<>("Account verified",
                HttpStatus.OK.value(),
                "Account activated",
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/activate/resend")
    private ResponseEntity<ApiResponse<UserVerificationDto>> resendPasscode(@RequestBody UserVerificationDto userVerification,
                                                                            WebRequest request) {

        UserVerificationDto verificationData = authService.createNewVerificationPasscode(userVerification.email());
        ApiResponse<UserVerificationDto> response = new ApiResponse<>("New passcode",
                HttpStatus.OK.value(),
                verificationData,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}