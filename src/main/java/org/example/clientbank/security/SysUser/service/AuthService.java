package org.example.clientbank.security.SysUser.service;


import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.clientbank.security.SysUser.SysUser;
import org.example.clientbank.security.SysUser.api.dto.JwtRequest;
import org.example.clientbank.security.SysUser.api.dto.JwtResponse;
import org.example.clientbank.security.jwt.JwtProvider;
import org.example.clientbank.security.refreshToken.RefreshToken;
import org.example.clientbank.security.refreshToken.db.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public JwtResponse login(JwtRequest jwtRequest) {
        SysUser sysUser = userService.getUserByLogin(jwtRequest.getLogin()).orElse(null);

        if (sysUser == null) {
            return null;
        }

        boolean passwordMatches = passwordEncoder.matches(jwtRequest.getPassword(), sysUser.getEncryptedPassword());
        if (!passwordMatches) {
            return null;
        }

        String accessToken = jwtProvider.generateAccessToken(sysUser);
        String refreshToken = jwtProvider.generateRefreshToken(sysUser);

        RefreshToken refreshTokenToBD = new RefreshToken(refreshToken, true, sysUser);
        refreshTokenRepository.save(refreshTokenToBD);

        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse refreshToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();

            RefreshToken saveRefreshToken = refreshTokenRepository
                    .findRefreshTokenByRefreshToken(refreshToken).orElse(null);

            if (saveRefreshToken != null && saveRefreshToken.getRefreshToken().equals(refreshToken) && saveRefreshToken.getIsValid()) {
                final SysUser sysUser = userService.getUserByLogin(login)
                        .orElse(null);
                if (sysUser == null) {
                    return null;
                }

                final String accessToken = jwtProvider.generateAccessToken(sysUser);
                final String newRefreshToken = jwtProvider.generateRefreshToken(sysUser);

                saveRefreshToken.setIsValid(false);
                refreshTokenRepository.save(saveRefreshToken);

                RefreshToken refreshTokenToBD = new RefreshToken(newRefreshToken, true, sysUser);
                refreshTokenRepository.save(refreshTokenToBD);

                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        return null;
    }
}