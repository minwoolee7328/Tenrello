package com.example.tenrello.auth.service;

import com.example.tenrello.auth.dto.SigninRequestDto;
import com.example.tenrello.auth.dto.SignupRequestDto;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.jwt.JwtUtil;
import com.example.tenrello.security.redis.RedisUtil;
import com.example.tenrello.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AuthServiceImpl")
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User user = new User(username, nickname, password);
        userRepository.save(user);
    }

    @Override
    public void login(SigninRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        String token = jwtUtil.createToken(user.getUsername());
        String refreshToken = jwtUtil.createRefreshToken();

        // RefreshToken Redis 저장
        redisUtil.saveRefreshToken(user.getUsername(), refreshToken);

        jwtUtil.addJwtToCookie(token, response);
    }

    @Transactional
    @Override
    public void logout(User user, HttpServletRequest request, HttpServletResponse response) {
        log.info("로그아웃 서비스");
        String bearerAccessToken = jwtUtil.getJwtFromCookie(request);
        String accessToken = jwtUtil.substringToken(bearerAccessToken);
        String username = user.getUsername();

        // refresh token 삭제
        if (redisUtil.getRefreshToken(username) != null) {
            log.info("로그아웃 시 리프레시 토큰 확인");
            redisUtil.deleteRefreshToken(username);
        }

        // access token blacklist 로 저장
        log.info("액세스 토큰 블랙리스트로 저장 : " + accessToken);
        redisUtil.addBlackList(accessToken, jwtUtil.remainExpireTime(accessToken));
    }

}
