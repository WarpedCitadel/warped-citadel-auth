package com.warpedcitadel.warpedcitadelauth.auth.dto;

public record VerificationTokenDto(
        String token,
        String passcode
) {}
