package com.warpedcitadel.warpedcitadelauth.security;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;


@RestController
@RequestMapping(path = "/.well-known", version = "1.0")
public class JwksController {

    @Value("${jwt.keyId}")
    private String keyID;

    private final RSAPublicKey publicKey;

    public JwksController(JwtUtil jwtUtil) {
        this.publicKey = jwtUtil.loadPublicKey();
    }


    @GetMapping("/jwks.json")
    public Map<String, Object> jwks() {

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID(keyID)
                .algorithm(JWSAlgorithm.RS256)
                .keyUse(KeyUse.SIGNATURE)
                .build();

        return new JWKSet(rsaKey).toJSONObject();
    }
}
