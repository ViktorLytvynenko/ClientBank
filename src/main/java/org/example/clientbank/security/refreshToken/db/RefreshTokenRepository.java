package org.example.clientbank.security.refreshToken.db;

import org.example.clientbank.security.refreshToken.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByRefreshToken(String refreshToken);
}
