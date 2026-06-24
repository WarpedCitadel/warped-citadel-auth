package com.warpedcitadel.warpedcitadelauth.auth.dto;

import jakarta.annotation.Nullable;

public record UserVerificationDto(
        String email,

        @Nullable
        String sessionToken
) {}
