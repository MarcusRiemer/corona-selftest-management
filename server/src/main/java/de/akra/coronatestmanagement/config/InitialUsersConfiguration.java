package de.akra.coronatestmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties("app")
@ConstructorBinding
public record InitialUsersConfiguration(List<InitialUser> initialUsers) {
    @ConstructorBinding
    record InitialUser(String name, String password, String[] roles) { }
}
