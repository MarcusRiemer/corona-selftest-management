package de.akra.coronatestmanagement.controller;

import de.akra.coronatestmanagement.config.WebSecurityConfig;
import de.akra.coronatestmanagement.repository.PersonTestRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analysis")
@Secured(WebSecurityConfig.ROLE_ANALYST)
public class AnalysisController {

    private final PersonTestRepository personTestRepository;

    public AnalysisController(PersonTestRepository personTestRepository) {
        this.personTestRepository = personTestRepository;
    }

    record DashboardDescription(int numPositive) {
    }

    @GetMapping("/dashboard")
    public DashboardDescription groupsToday() {
        var d = LocalDate.now();
        var queryResult = personTestRepository.countPositiveTests(d);
        return new DashboardDescription(queryResult);
    }
}
