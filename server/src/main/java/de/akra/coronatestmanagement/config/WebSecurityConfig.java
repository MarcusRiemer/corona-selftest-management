package de.akra.coronatestmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

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
        // authenticate the API" matcher comes last and therefore has lowest
        // precedence. The "but logins do not require authentication" comes
        // first and is therefore more specific and overrides the general API
        // rule.
        http
                .formLogin()
                .successHandler((_req, res, _auth) -> res.setStatus(200))
                .loginProcessingUrl("/api/session/login")
                .and()
                .logout()
                .logoutUrl("/api/session/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .and()
                .authorizeRequests()
                .antMatchers("/api/session/**").permitAll()
                .antMatchers("/api/**").authenticated();


        // CSRF doesn't play well with a single page application
        http.csrf().disable();

        // Return "403 Forbidden" instead of a redirect when permissions are insufficient
        http.exceptionHandling().authenticationEntryPoint((_req, res, _auth) -> res.setStatus(403));
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("pass").roles(ROLE_ADMIN, ROLE_ANALYST, ROLE_REPORTER).build();
        UserDetails reporter = User.withDefaultPasswordEncoder().username("reporter").password("pass").roles(ROLE_REPORTER).build();
        UserDetails analyst = User.withDefaultPasswordEncoder().username("analyst").password("pass").roles(ROLE_ANALYST).build();

        return new InMemoryUserDetailsManager(admin, reporter, analyst);
    }
}