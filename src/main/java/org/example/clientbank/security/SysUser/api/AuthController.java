package org.example.clientbank.security.SysUser.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.clientbank.security.SysUser.api.dto.JwtRefreshTokenDto;
import org.example.clientbank.security.SysUser.api.dto.JwtRequest;
import org.example.clientbank.security.SysUser.api.dto.JwtResponse;
import org.example.clientbank.security.SysUser.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody JwtRefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenDto.getRefreshToken()));
    }
}
