package project.ricecake.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.ricecake.member.service.MemberDetailsServiceImpl;
import project.ricecake.utils.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final MemberDetailsServiceImpl memberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        // 토큰이 Bearer로 시작하면
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String memberId = jwtUtils.extractClaim(token);
            UserDetails member = memberDetailsService.loadUserByUsername(memberId);

            if (jwtUtils.validateToken(token, member.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(member, member.getPassword(), member.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }
}
