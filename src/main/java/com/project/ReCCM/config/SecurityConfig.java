package com.project.ReCCM.config;

import com.project.ReCCM.service.member.CustomMemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomMemberDetailsService memberDetailsService;

    // SecurityFilterChain 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/product", "/custom", "/api/**", "/images/**", "/member/join").permitAll() // 비회원 허용 범위
                        .anyRequest().authenticated() // 그 외의 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/member/login") // 사용자 정의 로그인 페이지
                        .loginProcessingUrl("/member/login") // 로그인 폼 action URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 이동할 페이지
                        .failureUrl("/member/login?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/member/login?logout=true") // 로그아웃 성공 시 이동할 페이지
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider()); // 커스텀 AuthenticationProvider 추가

        return http.build();
    }

    // PasswordEncoder 빈 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화
    }

    // AuthenticationManager 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // DaoAuthenticationProvider 설정
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(memberDetailsService); // memberDetailsService 설정
        authProvider.setPasswordEncoder(passwordEncoder()); // PasswordEncoder 설정

        return authProvider;
    }
}
