package project.ricecake.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.ricecake.config.filter.JwtAuthFilter;
import project.ricecake.error.ErrorCode;
import project.ricecake.error.ErrorResponse;
import project.ricecake.member.service.UserDetailsServiceImpl;
import project.ricecake.utils.JwtUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * WebSecurityConfig
 * Spring Security 구성 정보 클래스
 */
@EnableWebSecurity  // 애노테이션을 추가하여 Spring Security Filter Chain 자동으로 등록
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl memberDetailsService;

    /**
     * Security Filter Chain 설정
     * @param http (Filter Chain 정보)
     * @return Spring Security Filter Chain 구성 정보
     * @throws Exception : 설정에서 문제가 발생할 경우
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // URL 매칭
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("api/v1/member/**").permitAll()
                        .requestMatchers("api/v1/board/**").permitAll()
                        .requestMatchers("api/v1/comment/**").permitAll()
                        .anyRequest().authenticated())

                // Form Login 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // Http Basic 인증 방식을 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 직접 설정한 cors 정보를 사용
                .cors(cors -> cors.configurationSource(corsConfigurer()))

                // 세션 관리 정책을 무상태로 지정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증 및 인가에 대한 예외가 발생할 경우 처리할 수 있는 핸들러 등록
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint))

                // 로그아웃을 할 시 어떻게 처리할 지 지정
                .logout(logout -> logout
                        .logoutUrl("/api/v1/member/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.sendRedirect("/login");
                        })))

                // JWT 인증 필터를 시큐리티 필터에 등록
                .addFilterBefore(new JwtAuthFilter(jwtUtils, memberDetailsService), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    /**
     * CORS 설정 정보
     * @return CORS 설정 정보를 반환
     */
    public CorsConfigurationSource corsConfigurer() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        corsConfig.setAllowedOrigins(List.of("*"));                // 모든 출처에 대해서 허용
        corsConfig.setAllowedHeaders(List.of("*"));                // 모든 헤더에 대해서 허용
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));    // 어떤 HTTP 메서드를 허용할 것인지 지정
        corsConfig.setAllowCredentials(true);                          // 자격 증명을 포함한 요청을 허용
        corsConfig.setMaxAge(3600L);                                   // preflight 요청에 대한 응답을 캐시할 수 있는 시간을 지정 (60분)
        source.registerCorsConfiguration("/**", corsConfig);    // 등록
        return source;
    }

    /**
     * 인가에서 예외가 발생할 경우 처리하기 위한 예외 핸들러
     */
    private final AccessDeniedHandler accessDeniedHandler = ((request, response, accessDeniedException) -> {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ErrorCode.FORBIDDEN);
        securityErrorResponse(errorResponse, response);
    });

    /**
     * 인증에서 예외가 발생할 경우 처리하기 위한 예외 핸들러
     */
    private final AuthenticationEntryPoint authenticationEntryPoint = ((request, response, authException) -> {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ErrorCode.UNAUTHORIZED);
        securityErrorResponse(errorResponse, response);
    });


    /**
     * Spring Security에서 사용할 예외 응답
     * @param errorResponse (클라이언트에게 반환할 예외 응답 객체)
     * @param response      (클라이언트에게 반환할 응답 객체)
     * @throws IOException : 클라이언트에게 반환할 때 IO 오류가 발생할 경우
     */
    private void securityErrorResponse(ErrorResponse errorResponse, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());        // 응답 상태 코드를 지정
        response.setCharacterEncoding("UTF-8");                     // 응답 문자 인코딩 형식 지정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);  // 응답 Content-Type 형식 지정

        String result = objectMapper.writeValueAsString(errorResponse);
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
    }
}
