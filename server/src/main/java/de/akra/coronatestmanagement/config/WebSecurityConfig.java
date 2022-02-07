package de.akra.coronatestmanagement.config;

import de.akra.coronatestmanagement.CoronaTestManagementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_REPORTER = "REPORTER";
    public static final String ROLE_ANALYST = "ANALYST";

    public WebSecurityConfig(Environment env, InitialUsersConfiguration initialUsersConfiguration) {
        this.env = env;
        this.initialUsersConfiguration = initialUsersConfiguration;
    }

    private final Environment env;

    private final InitialUsersConfiguration initialUsersConfiguration;

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
        if (initialUsersConfiguration.initialUsers().size() == 0) {
            throw new CoronaTestManagementException("No users configured");
        }

        var users = initialUsersConfiguration.initialUsers().stream()
                .map(u -> User.withDefaultPasswordEncoder().username(u.name()).roles(u.roles()).password(u.password()).build())
                .collect(Collectors.toCollection(ArrayList::new));

        return new InMemoryUserDetailsManager(users);
    }
}