package de.akra.coronatestmanagement.controller;

import de.akra.coronatestmanagement.WebSecurityConfig;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
@Secured(WebSecurityConfig.ROLE_ANALYST)
public class AnalysisController {
}
