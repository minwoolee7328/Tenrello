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
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getJwtFromCookie(request);

        if (StringUtils.hasText(accessToken)) {
            accessToken = jwtUtil.substringToken(accessToken);
            log.info("액세스 토큰 값 : " + accessToken);

            String newAccessToken = jwtUtil.reissueAccessToken(accessToken);
            jwtUtil.addJwtToCookie(newAccessToken,response);

            if (!jwtUtil.validateToken(accessToken)) {
                log.info("액세스 토큰 유효하지 않음");
                return;
            }

            log.info("body의 사용자 정보 꺼내기");
            Claims info = jwtUtil.getUserInfoFromToken(accessToken);

            try {
                // token 생성 시 subject에 username 넣어둠
                log.info(info.getSubject());
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.info("오류 발생");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // token -> authentication 객체에 담기 -> SecurityContext에 담기 -> ContextHolder에 담기
    // 인증 처리
    public void setAuthentication(String username) {
        log.info("인증 성공");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성 (아직 인증 전)
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}

