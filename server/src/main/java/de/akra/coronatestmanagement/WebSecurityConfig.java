package de.akra.coronatestmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_REPORTER = "REPORTER";
    public static final String ROLE_ANALYST = "ANALYST";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Ordering of these antMatchers matters. The generic "we want to
        // authenticate the API" matcher comes first and therefore has lowest
        // precedence. The "but logins do not require authentication" comes
        // last and is therefore more specific and overrides the general API
        // rule.
        http.formLogin().loginProcessingUrl("/api/session/login")
                .and().authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/api/session/**").permitAll();

        // CSRF doesn't play well with a single page application
        http.csrf().disable();

        // Return "403 Forbidden" instead of a redirect when permissions are insufficient
        http.exceptionHandling().authenticationEntryPoint(unauthenticatedRequestHandler());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("pass").roles(ROLE_ADMIN, ROLE_ANALYST, ROLE_REPORTER).build();
        UserDetails reporter = User.withDefaultPasswordEncoder().username("reporter").password("pass").roles(ROLE_REPORTER).build();
        UserDetails analyst = User.withDefaultPasswordEncoder().username("analyst").password("pass").roles(ROLE_ANALYST).build();

        return new InMemoryUserDetailsManager(admin, reporter, analyst);
    }

    @Bean
    UnauthenticatedRequestHandler unauthenticatedRequestHandler() {
        return new UnauthenticatedRequestHandler();
    }

    static class UnauthenticatedRequestHandler implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setStatus(403);
        }
    }
}