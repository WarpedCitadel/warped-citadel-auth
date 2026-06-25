package com.warpedcitadel.warpedcitadelauth.security;


import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    @Value("${jwt.keyId}")
    private String keyID;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("classpath:keys/private.pem")
    private Resource privateKeyResource;

    @Value("classpath:keys/private.pem")
    private Resource publicKeyResource;

    private PrivateKey privateKey;

    private PublicKey publicKey;


    public String generateToken(String username){
        privateKey = loadPrivateKey();

        return Jwts.builder()
                .header()
                    .keyId(keyID)
                    .and()
                .subject(username)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }


    public PrivateKey loadPrivateKey() {

        try {

            String key = new String(
                    privateKeyResource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            key = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            PKCS8EncodedKeySpec spec =
                    new PKCS8EncodedKeySpec(decoded);

            return KeyFactory.getInstance("RSA")
                    .generatePrivate(spec);

        } catch (Exception exception) {

            throw new IllegalStateException("Unable to load RSA private key", exception);
        }
    }


    public PublicKey loadPublicKey() {

        try {

            String key = new String(
                    publicKeyResource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            key = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(decoded);

            return KeyFactory.getInstance("RSA")
                    .generatePublic(spec);

        } catch (Exception exception) {

            throw new IllegalStateException("Unable to load RSA public key", exception);
        }
    }


    public String getUserFromToken(String token){
        publicKey = loadPublicKey();

        return Jwts.parser().verifyWith(publicKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    public boolean validateJwtToken(String token){
        publicKey = loadPublicKey();

        try {

            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
            return true;

        } catch (Exception validationException) {

            log.error("JWT validation error: {}", validationException.getMessage());
        }
        return false;
    }
}
