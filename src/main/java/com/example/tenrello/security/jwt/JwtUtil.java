package com.example.tenrello.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtUtil {

    // Header key 값 (cookie의 name 값)
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // token 식별자 규칙 (이 뒤는 token 이구나! value 앞에 붙이기, 한 칸 띄우기)
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분 (millisec)

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey(application.properties)
    private String secretKey;
    //secretkey 담을 객체 (jwt 암호화, 복호화에 사용)
    private Key key;
    // enum, 사용할 암호화 알고리즘
    public final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        // secretKey : 이미 base64로 인코딩 된 값
        // 사용하려면 디코딩
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String nickname){
        log.info(nickname + "의 토큰 생성");
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(nickname) // 사용자 id
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급 시간
                        .signWith(key,signatureAlgorithm) // 키, 암호화 알고리즘
                        .compact(); // 완성
    }

    // 토큰 쿠키에 담기
    public void addJwtToCookie(String token, HttpServletResponse response) {
        log.info("토큰 쿠키에 담기");
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행
            log.info("token = " + token);
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

    // Cookie에서 토큰 가져오기
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        log.info("쿠키에서 토큰 꺼내기" + URLDecoder.decode(cookie.getValue(), "UTF-8"));
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new IllegalArgumentException("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}

