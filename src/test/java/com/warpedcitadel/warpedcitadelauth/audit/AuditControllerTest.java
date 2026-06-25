package com.warpedcitadel.warpedcitadelauth.audit;

import com.warpedcitadel.warpedcitadelauth.audit.dto.UserSessionsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.accept.ApiVersionStrategy;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AuditControllerTest.class);
    @Mock
    private MockMvc mockMvc;

    @Mock
    private ApiVersionStrategy apiVersionStrategy;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuditController auditController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditController)
                .setApiVersionStrategy(apiVersionStrategy)
                .build();
    }


    @Test
    void _test_getUserSessions() throws Exception {

        List<String> sessions = List.of("2026-03-12 23:17:37.290907",
                                        "2026-03-17 10:43:27.290907",
                                        "2026-05-01 06:34:17.290907",
                                        "2026-06-23 14:45:37.290907",
                                        "2026-07-12 01:54:17.290907",
                                        "2026-06-06 08:12:57.290907");

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";
        UserSessionsDto userSessions = new UserSessionsDto(sessions);

        when(auditService.getAppUserSessions(validUUID)).thenReturn(userSessions.sessions());

        mockMvc.perform(get("/user/profile/{uuid}/session", validUUID)
                .header("x-api-version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("User sessions"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.instance").value("/user/profile/019ea371-9498-7cb1-b4b9-4ee3db8dc132/session"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data[*]").exists());
    }
}