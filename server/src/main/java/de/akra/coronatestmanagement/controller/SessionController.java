package de.akra.coronatestmanagement.controller;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    private UserDetailsService userDetailsService;

    public SessionController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Tests for people are always explicitly created with "UNKNOWN" values.
     */
    @PostMapping("/login")
    public boolean login(@RequestBody LoginParams params) {
        var u = this.userDetailsService.loadUserByUsername(params.userName);
        return u.getPassword().equals(params.password);
    }

    record LoginParams(String userName, String password) { }
}
