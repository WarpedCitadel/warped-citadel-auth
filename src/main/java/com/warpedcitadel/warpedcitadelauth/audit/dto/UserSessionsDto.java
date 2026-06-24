package com.warpedcitadel.warpedcitadelauth.audit.dto;

import java.util.List;

public record UserSessionsDto(
        List<String> sessions
) {}
