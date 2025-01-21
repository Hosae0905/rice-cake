package project.ricecake.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.ricecake.member.service.UserDetailsServiceImpl;
import project.ricecake.utils.JwtUtils;

import java.io.IOException;

/**
 * JwtAuthFilter
 * JWT 토큰을 검증하는 필터 클래스
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl memberDetailsService;

    /**
     *
     * @param request       (Http 요청 객체)
     * @param response      (Http 응답 객체)
     * @param filterChain   (Filter Chain 정보)
     * @throws ServletException : 서블릿 관련 예외가 발생할 경우
     * @throws IOException      : 입출력 관련 예외가 발생할 경우
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String authorization = request.getHeader("Authorization");      // Http 헤더에서 Authorization 값을 변수에 저장

        // 만약 토큰이 null이 아니고 Bearer로 시작하면
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);      // "Bearer " 부분을 제외한 나머지 부분을 token 변수에 저장
            String memberId = jwtUtils.extractClaim(token);           // token 정보를 통해서 회원 아이디를 확인
            UserDetails member = memberDetailsService.loadUserByUsername(memberId);     // 회원 아이디를 DB에서 조회

            // 만약 토큰의 정보와 회원 아이디(username)를 통해서 토큰 검증을 성공할 경우
            if (jwtUtils.validateToken(token, member.getUsername())) {
                // 해당 회원의 정보를 통해서 인증 토큰을 생성
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword(), member.getAuthorities());
                
                // 인증된 회원 토큰을 SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
