package project.ricecake.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.PrintWriter;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl memberDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("api/v1/member/**").permitAll()
                        .requestMatchers("api/v1/board/**").permitAll()
                        .requestMatchers("api/v1/comment/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurer()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/member/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.sendRedirect("/login");
                        }))
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtils, memberDetailsService), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    public CorsConfigurationSource corsConfigurer() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    private final AccessDeniedHandler accessDeniedHandler = ((request, response, accessDeniedException) -> {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ErrorCode.FORBIDDEN);
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String result = objectMapper.writeValueAsString(errorResponse);
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
    });

    private final AuthenticationEntryPoint authenticationEntryPoint = ((request, response, authException) -> {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ErrorCode.UNAUTHORIZED);
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String result = objectMapper.writeValueAsString(errorResponse);
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
    });
}
