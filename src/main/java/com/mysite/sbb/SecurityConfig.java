package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
// 위의 두 어노테이션으로 인해 기존 설정이 동작하지 못하게 함
// 아래의 메서드로 동작해야한다.
// 스프링 시큐리티의 기본설정이 이 클래스로 설정된다. <- 내 생각
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        /*http.authorizeHttpRequests().requestMatchers(
                new AntPathRequestMatcher("/**")).permitAll()
        ;*/ // 위의 명령어는 모든 접근을 허용하게 한다. 라는 의미인데 filterchain 메서드에 아무것도 해주지 않아고 기본적으로 모든 접근에 허용이다.
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
