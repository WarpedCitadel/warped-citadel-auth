package com.warpedcitadel.warpedcitadelauth.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warpedcitadel.warpedcitadelauth.auth.dto.UserSignupDto;
import com.warpedcitadel.warpedcitadelauth.auth.dto.UserVerificationDto;
import com.warpedcitadel.warpedcitadelauth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.accept.ApiVersionStrategy;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthService authService;

    @Mock
    private ApiVersionStrategy apiVersionStrategy;

    @InjectMocks
    private AuthController authController;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setApiVersionStrategy(apiVersionStrategy)
                .build();
    }

    @Test
    void _test_createAppUser() throws Exception {

        Path filePath = Path.of("src/test/resources/json/appUserSignup.json");
        String json = Files.readString(filePath);

        UserSignupDto user = objectMapper.readValue(json, UserSignupDto.class);

        UserVerificationDto verificationData = new UserVerificationDto(
                "johnblanche@gmail.com",
                "32aa8760-2431-43f7-8993-5278dd478032"
                );

        when(authService.createAppUser(user)).thenReturn(verificationData);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("x-api-version", "1.0"))
                        .andExpect(status().isCreated())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.title").value("Account created"))
                        .andExpect(jsonPath("$.status").value(201))
                        .andExpect(jsonPath("$.data.email").value("johnblanche@gmail.com"))
                        .andExpect(jsonPath("$.data.sessionToken").value("32aa8760-2431-43f7-8993-5278dd478032"))
                        .andExpect(jsonPath("$.instance").value("/api/auth/signup"))
                        .andExpect(jsonPath("$.timestamp").exists());
    }


//    @Test
//    void _test_loginAppUser() throws Exception {
//
//        Path filePath = Path.of("src/test/resources/json/appUserLogin.json");
//        String json = Files.readString(filePath);
//
//        UserLoginDto user = objectMapper.readValue(json, UserLoginDto.class);
//
//        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huQmxhbmNoZTEiLCJpYXQiOjE3ODA3Nzk2MDgsImV4cCI6MTc4MDc4MDUwOH0.JrksQumyiVuI68qjMxPcBIOxF6en6DQeYha1cwUZD_U";
//        UserReferenceDto userDto = new UserReferenceDto("JohnBlanche", "019ea371-9498-7cb1-b4b9-4ee3db8dc132", "user");
//
//        when(jwtUtil.generateToken(userDto)).thenReturn(jwtToken);
//        when(authService.loginUser(user)).thenReturn(userDto);
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                        .header("x-api-version", "1.0"))
//                        .andExpect(status().isOk())
//                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                        .andExpect(header().string("Authorization",
//                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huQmxhbmNoZTEiLCJpYXQiOjE3ODA3Nzk2MDgsImV4cCI6MTc4MDc4MDUwOH0.JrksQumyiVuI68qjMxPcBIOxF6en6DQeYha1cwUZD_U"))
//                        .andExpect(jsonPath("$.title").value("Logged in"))
//                        .andExpect(jsonPath("$.status").value(200))
//                        .andExpect(jsonPath("$.data.userUUID").value("019ea371-9498-7cb1-b4b9-4ee3db8dc132"))
//                        .andExpect(jsonPath("$.instance").value("/auth/login"))
//                        .andExpect(jsonPath("$.timestamp").exists());
//    }
}
