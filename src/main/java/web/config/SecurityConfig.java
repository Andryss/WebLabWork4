package web.config;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import web.services.PersonDetailsService;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(personDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //noinspection Convert2MethodRef
        http
                .csrf((c) -> c
                        .disable() // No csrf - no problems
                )
                .httpBasic((c) -> c
                        .disable() // Now Spring won't send the login form for no reason
                )
                .authorizeHttpRequests((c) -> c
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((c) -> c
                        .loginProcessingUrl("/auth/login")
                        .successHandler(successHandler())
                        .failureHandler(failureHandler())
                )
                .logout((c) -> c
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler(logoutSuccessHandler())
                )
                .exceptionHandling((c) -> c
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
        ;

        return http.build();
    }



    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) ->
                writeJsonResponse(response, HttpStatus.OK.value(), "user " + authentication.getName() + " successfully authenticated");
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) ->
                writeJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) ->
                writeJsonResponse(response, HttpStatus.OK.value(), "user " + authentication.getName() + " successfully logged out");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                writeJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), accessDeniedException.getMessage());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) ->
                writeJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
    }

    private void writeJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write("{ \"message\": \"" + message + "\" }");
    }

}
