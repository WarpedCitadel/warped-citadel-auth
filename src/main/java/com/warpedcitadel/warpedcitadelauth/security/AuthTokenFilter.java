package com.warpedcitadel.warpedcitadelauth.security;

import com.warpedcitadel.warpedcitadelauth.auth.AuthRepository;
import com.warpedcitadel.warpedcitadelauth.auth.dto.UserReferenceDto;
import com.warpedcitadel.warpedcitadelauth.auth.model.UserDetailsModel;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final String BEARER_ = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthRepository authRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, java.io.IOException {

        try {

            String jwtToken = parseJwt(request);

            if (jwtToken != null) {

                Claims claims = jwtUtil.validateJwtToken(jwtToken);

                UserReferenceDto userDetails = new UserReferenceDto(
                        claims.get("userUUID", String.class),
                        claims.getSubject()
                );

                UserDetailsModel userDetailsModel = authRepository.authenticateUser(userDetails.username());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetailsModel.getUsername(),
                                null,
                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + userDetailsModel.getRole()
                                        )));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (Exception exception) {

            logger.error("Cannot set user authentication: {}", exception);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith(BEARER_)) {
            return headerAuth.substring(BEARER_.length());
        }

        return null;
    }
}