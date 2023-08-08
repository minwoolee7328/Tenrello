package com.example.tenrello.auth.service;

import com.example.tenrello.auth.dto.SigninRequestDto;
import com.example.tenrello.auth.dto.SignupRequestDto;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.jwt.JwtUtil;
import com.example.tenrello.security.redis.RedisUtil;
import com.example.tenrello.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        User user = new User(username, password);
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

        // RefreshToken Redis 저장 (ExpirationTime 설정을 통해 자동 삭제 처리)
        redisUtil.saveRefreshToken(user.getUsername(), refreshToken);

        jwtUtil.addJwtToCookie(token, response);
    }

}
