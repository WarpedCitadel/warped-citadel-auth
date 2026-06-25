package com.warpedcitadel.warpedcitadelauth.audit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditService auditService;


    @Test
    void _test_getAppUserSessions(){

        List<String> userSessions = List.of("2026-03-12 23:17:37.290907",
                "2026-03-17 10:43:27.290907",
                "2026-05-01 06:34:17.290907",
                "2026-06-23 14:45:37.290907",
                "2026-07-12 01:54:17.290907",
                "2026-06-06 08:12:57.290907");

        String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";
        when(auditRepository.getAppUserSessions(validUUID)).thenReturn(userSessions);

        List<String> result = auditService.getAppUserSessions(validUUID);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userSessions, result);
    }
}