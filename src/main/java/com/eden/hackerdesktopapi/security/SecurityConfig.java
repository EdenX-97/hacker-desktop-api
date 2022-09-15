package com.eden.hackerdesktopapi.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;


/**
 * Configure of spring security
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProv authenticationProvider;
    private final AuthenticationEntryPt authenticationEntryPoint;
    private final AccessDenyHandlerImpl accessDenyHandlerImpl;
    private final ExceptionFilter exceptionFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public SecurityConfig(AuthenticationProv authenticationProvider,
                          AuthenticationEntryPt authenticationEntryPoint,
                          AccessDenyHandlerImpl accessDenyHandlerImpl,
                          ExceptionFilter exceptionFilter) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDenyHandlerImpl = accessDenyHandlerImpl;
        this.exceptionFilter = exceptionFilter;
    }

    private static final String[] AUTH_WHITELIST = {
        "/user/register",
        "/rss/subscribeRSS",
        "/news/getWeeklyNews",
        "/news/getAllWeeklyNews",
        "/news/updateHackerNewsWeekly",
        "/news/updateOverflowNewsWeekly",
        "/news/updateInfoQNewsWeekly",
        "/podcast/getPodcast",
        "/podcast/updateAccidentalTechPodcasts",
    };

    private static final String[] AUTH_ADMIN = {

    };

    private static final String[] AUTH_USER = {

    };

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable();

        http
            .authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(AUTH_ADMIN).hasRole("ADMIN")
                .antMatchers(AUTH_USER).hasRole("USER")
                .anyRequest().authenticated()

                .and()
                    .addFilter(new JWTLoginFilter(authenticationManager()))
                    .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                    .addFilterBefore(exceptionFilter, CorsFilter.class)
                    .formLogin().loginProcessingUrl("/login").permitAll()
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDenyHandlerImpl)
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
