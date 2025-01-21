package project.ricecake.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoderConfig
 * 비밀번호 암호화를 구성하는 클래스
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * 비밀번호 암호화 방식을 반환
     * @return BCrypt 암호화 인코더를 반환
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
