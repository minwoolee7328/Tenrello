package com.example.tenrello.security.jwt;

import com.example.tenrello.security.details.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "인가 필터")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService; // 사용자가 존재하는지

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request 의 cookie에서 token 꺼내기
        String tokenValue = jwtUtil.getJwtFromCookie(request);

        if (StringUtils.hasText(tokenValue)) {
            tokenValue = tokenValue.substring(7);
            log.info("tokenValue : " + tokenValue);
            if (!jwtUtil.validateToken(tokenValue)) {
                return;
            }
            // body의 내용 꺼내기
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                // token 생성 시 subject에 username 넣어둠
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // token -> authentication 객체에 담기 -> SecurityContext에 담기 -> ContextHolder에 담기
    // 인증 처리
    public void setAuthentication(String nickname) {
        log.info("로그인 성공");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(nickname);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성 (아직 인증 전)
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

