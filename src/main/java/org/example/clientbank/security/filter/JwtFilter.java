package org.example.clientbank.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.clientbank.security.JwtAuthentication;
import org.example.clientbank.security.JwtUtils;
import org.example.clientbank.security.SysUser.SysUser;
import org.example.clientbank.security.SysUser.service.UserService;
import org.example.clientbank.security.jwt.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token == null) {
            fc.doFilter(request, response);
            return;
        }
        try {
            if (jwtProvider.validateAccessToken(token)) {
                final Claims claims = jwtProvider.getAccessClaims(token);
                final String login = claims.getSubject();


                SysUser user = userService.getUserByLogin(login).orElse(null);
                if (user != null) {
                    final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims, user);
                    jwtInfoToken.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT", e);
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}