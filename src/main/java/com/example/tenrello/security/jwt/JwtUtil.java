package com.example.tenrello.security.jwt;

import com.example.tenrello.security.redis.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JwtUtil")
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    public static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey(application.properties)
    private String secretKey;
    private Key key;
    // 사용할 암호화 알고리즘
    public final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        // secretKey : 이미 base64로 인코딩 된 값
        // 사용하려면 디코딩
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // access token 생성
    public String createToken(String username) {
        log.info(username + "의 액세스 토큰 생성");
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 id
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급 시간
                        .signWith(key, signatureAlgorithm) // 키, 암호화 알고리즘
                        .compact(); // 완성
    }

    // refresh token 생성
    public String createRefreshToken() {
        log.info("리프레시 토큰 생성");
        Date date = new Date();

        return Jwts.builder()
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // access token 재발급
    public String reissueAccessToken(String token) {
        log.info("액세스 토큰 재발급");
        if (validateToken(token)) {
            Claims info = getUserInfoFromToken(token);
            String username = info.getSubject();
            log.info("재발급 요청자 : " + username);

            // refresh token 가져오기
            String refreshToken = redisUtil.getRefreshToken(username);

            // refresh token 존재하고 유효하다면
            if (StringUtils.hasText(refreshToken) && validateToken(refreshToken)) {
                log.info("리프레시 토큰 존재하고 유효함");
                return createToken(username);
            }
        }
        return null;
    }

    // 토큰 쿠키에 담기
    public void addJwtToCookie(String token, HttpServletResponse response) {
        log.info("토큰 쿠키에 담기");
        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행
        log.info("token = " + token);
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
        cookie.setPath("/");

        // Response 객체에 Cookie 추가
        response.addCookie(cookie);
    }

    // Cookie에서 토큰 가져오기
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    log.info("쿠키에서 토큰 꺼내기" + URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8));
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); // Encode 되어 넘어간 Value 다시 Decode
                }
            }
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            if (redisUtil.isBlackList(token)) {
                // blacklist에 존재하는 access token이면
                throw new IllegalArgumentException("로그아웃된 토큰입니다");
            }
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new IllegalArgumentException("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료");
            return false;
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
    }

    public String substringToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        throw new NullPointerException("token 비었거나 bearer로 시작하지 않습니다.");
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 액세스 토큰 남은 만료시간 계산
    public Long remainExpireTime(String token) {
        // 토큰 만료 시간
        Long expirationTime = getUserInfoFromToken(token).getExpiration().getTime();
        // 현재 시간
        Long dateTime = new Date().getTime();

        return expirationTime - dateTime;
    }

    public void expireCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}

