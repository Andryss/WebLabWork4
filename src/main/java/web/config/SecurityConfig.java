package web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import web.filters.AuthSuccessHandler;
import web.filters.OneSessionFilter;
import web.filters.OneSessionFilterImpl;
import web.services.PersonDetailsService;
import web.util.RequestMessageResponse;

import java.io.IOException;
import java.util.List;

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
                .sessionManagement((c) -> c
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // To track one user session
                )
                .csrf((c) -> c
                        .disable() // Cross-site bla bla bla
                )
                .cors((c) -> c
                        .configurationSource(corsConfigurationSource()) // Allow-Cross-Origin-Policy: *
                )
                .httpBasic((c) -> c
                        .disable() // Now Spring won't send the login form for no reason
                )
                .authorizeHttpRequests((c) -> c
                        .requestMatchers("/history").authenticated()
                        .anyRequest().permitAll()
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
                .addFilterAfter(oneSessionFilter(), UsernamePasswordAuthenticationFilter.class) // Add one session filter
        ;

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .requestMatchers("/static/css/**", "/static/js/**", "/audio/**", "/pictures/**", "/favicon.ico");
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthSuccessHandler() {
            @Override
            public OneSessionFilter oneSessionFilter() {
                return SecurityConfig.this.oneSessionFilter();
            }

            @Override
            public void writeJsonResponse(HttpServletResponse response, int status, Object obj) throws IOException {
                SecurityConfig.this.writeJsonResponse(response, status, obj);
            }
        };
    }

    @Bean
    public OneSessionFilter oneSessionFilter() {
        return new OneSessionFilterImpl() {
            @Override
            public AuthenticationFailureHandler failureHandler() {
                return SecurityConfig.this.failureHandler();
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) ->
                writeJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), new RequestMessageResponse(exception));
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) ->
                writeJsonResponse(response, HttpStatus.OK.value(), new RequestMessageResponse("user " + authentication.getName() + " successfully logged out"));
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                writeJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), new RequestMessageResponse(accessDeniedException));
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) ->
                writeJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), new RequestMessageResponse(authException));
    }

    private void writeJsonResponse(HttpServletResponse response, int status, Object obj) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        jsonWriter().writeValue(response.getWriter(), obj);
    }

    @Bean
    public ObjectMapper jsonWriter() {
        return new ObjectMapper();
    }

}
