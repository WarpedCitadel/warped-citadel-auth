package com.warpedcitadel.warpedcitadelauth.security;


import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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

    @Value("${jwt.audience}")
    private String audience;

    private final Resource privateKeyResource;
    private final Resource publicKeyResource;

    public final RSAPrivateKey privateKey;
    public final RSAPublicKey publicKey;

    public JwtUtil(
            @Value("classpath:keys/private.pem")
            Resource privateKeyResource,

            @Value("classpath:keys/public.pem")
            Resource publicKeyResource) {

        this.privateKeyResource = privateKeyResource;
        this.publicKeyResource = publicKeyResource;

        this.privateKey = loadPrivateKey();
        this.publicKey = loadPublicKey();
    }


    public String generateToken(String username){

        return Jwts.builder()
                .header()
                    .keyId(keyID)
                    .and()
                .subject(username)
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }


    public RSAPrivateKey loadPrivateKey() {

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

            return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(spec);

        } catch (Exception exception) {

            throw new IllegalStateException("Unable to load RSA private key", exception);
        }
    }


    public RSAPublicKey loadPublicKey() {

        try {

            String key = new String(
                    publicKeyResource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            key = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(decoded);

            return (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(spec);

        } catch (Exception exception) {

            throw new IllegalStateException("Unable to load RSA public key", exception);
        }
    }


    public String getUserFromToken(String token){

        return Jwts.parser().verifyWith(publicKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    public boolean validateJwtToken(String token){

        try {

            Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);
            return true;

        } catch (Exception validationException) {

            log.error("JWT validation error: {}", validationException.getMessage());
        }

        return false;
    }
}
