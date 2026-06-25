package com.warpedcitadel.warpedcitadelauth.auth;

import com.warpedcitadel.warpedcitadelauth.audit.AuditRepository;
import com.warpedcitadel.warpedcitadelauth.auth.dto.UserLoginDto;
import com.warpedcitadel.warpedcitadelauth.auth.dto.UserReferenceDto;
import com.warpedcitadel.warpedcitadelauth.auth.dto.UserSignupDto;
import com.warpedcitadel.warpedcitadelauth.auth.dto.UserVerificationDto;
import com.warpedcitadel.warpedcitadelauth.auth.model.AuthModel;
import com.warpedcitadel.warpedcitadelauth.auth.model.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder(4);

    }

    @Test
    void _test_createAppUser() {

        UserSignupDto userDto = new UserSignupDto(
                "JohnBlanche",
                "pa$sW0rd5!",
                "jaeger.nolte@gmail.com");

        doNothing().when(authRepository).createAppUser(Mockito.any(UserModel.class));

        UserVerificationDto userVerificationResult = authService.createAppUser(userDto);

        ArgumentCaptor<UserModel> user = ArgumentCaptor.forClass(UserModel.class);
        Mockito.verify(authRepository).createAppUser(user.capture());

        Assertions.assertEquals("jaeger.nolte@gmail.com", userVerificationResult.email());
        Assertions.assertTrue(isValidUuid(userVerificationResult.sessionToken()));

        UserModel userModelResult = user.getValue();
        Assertions.assertNotNull(userModelResult);
        Assertions.assertEquals("JohnBlanche", userModelResult.getUsername());
        Assertions.assertEquals("jaeger.nolte@gmail.com", userModelResult.getEmail());
        Assertions.assertTrue(passwordEncoder.matches("pa$sW0rd5!", userModelResult.getPasswordHash()));
        Assertions.assertNotNull(userModelResult.getToken());
        Assertions.assertTrue(isValidUuid(userModelResult.getToken()));
        Assertions.assertNotNull(userModelResult.getPasscode());
        Assertions.assertEquals(6, userModelResult.getPasscode().length());
    }

    @Nested
    class loginTests {

        @Test
        void _test_loginAppUser() {

            UserLoginDto userDto = new UserLoginDto(
                    "JohnBlanche",
                    "pa$sW0rd5!");

            UserModel userModel = new UserModel(
                    userDto.username()
            );

            AuthModel mockDbUser = new AuthModel(
                    "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                    "JohnBlanche",
                    "$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6",
                    "user",
                    true,
                    true
            );

            when(authRepository.authenticateUser(userModel.getUsername())).thenReturn(mockDbUser);

            UserReferenceDto result = authService.loginUser(userDto);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(userDto.username(), mockDbUser.getUsername());
            Assertions.assertTrue(passwordEncoder.matches(userDto.password(), mockDbUser.getPasswordHash()));
            Assertions.assertEquals((result.userUUID()), mockDbUser.getUuid());

            verify(auditRepository, times(1)).updateLastActiveDtm(mockDbUser.getUuid());
        }


        @Test
        void _test_loginUserNotVerifiedActive() {

            UserLoginDto userDto = new UserLoginDto(
                    "JohnBlanche",
                    "pa$sW0rd5!");

            UserModel userModel = new UserModel(
                    userDto.username()
            );

            AuthModel mockDbUser = new AuthModel(
                    "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                    "JohnBlanche",
                    "$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6",
                    "user",
                    false,
                    false
            );

            when(authRepository.authenticateUser(userModel.getUsername())).thenReturn(mockDbUser);

            Assertions.assertThrowsExactly(BadCredentialsException.class, () -> {
                authService.loginUser(userDto);
            });
        }
    }



    // ### HELPER FUNCTIONS ###

    public static boolean isValidUuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        Matcher matcher = UUID_PATTERN.matcher(uuid);
        return matcher.matches();
    }
}