package com.warpedcitadel.warpedcitadelauth.payload;

import java.time.Instant;
import java.util.Map;


public record GenericApiErrorResponse<timestamp>(
        String title,
        int status,
        Map<String, String> error,
        String instance,
        Instant timestamp
) {}
