package com.warpedcitadel.warpedcitadelauth.audit;

import com.warpedcitadel.warpedcitadelauth.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(path = "/user", version = "1.0")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/profile/{uuid}/session")
    public ResponseEntity<ApiResponse<List<String>>> getUserSessions(@PathVariable String uuid, WebRequest request) {

        List<String> sessions = auditService.getAppUserSessions(uuid);

        ApiResponse<List<String>> userSessions = new ApiResponse<>("User sessions",
                HttpStatus.OK.value(),
                sessions,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(userSessions, HttpStatus.OK);
    }
}
