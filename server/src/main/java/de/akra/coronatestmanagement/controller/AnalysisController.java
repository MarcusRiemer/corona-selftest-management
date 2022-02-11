package de.akra.coronatestmanagement.controller;

import de.akra.coronatestmanagement.config.WebSecurityConfig;
import de.akra.coronatestmanagement.repository.PersonTestExemptionRepository;
import de.akra.coronatestmanagement.repository.PersonTestRepository;
import de.akra.coronatestmanagement.util.TestDate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/analysis")
@Secured(WebSecurityConfig.ROLE_ANALYST)
public class AnalysisController {

    private final PersonTestRepository personTestRepository;

    private final PersonTestExemptionRepository personTestExemptionRepository;

    public AnalysisController(PersonTestRepository personTestRepository, PersonTestExemptionRepository personTestExemptionRepository) {
        this.personTestRepository = personTestRepository;
        this.personTestExemptionRepository = personTestExemptionRepository;
    }

    @GetMapping("/dashboard")
    public DashboardDescription dashboardFor(
            @RequestParam() Optional<String> date
    ) {
        var d = date.isPresent() ? TestDate.parse(date.get()) : LocalDate.now();
        var overallCount = personTestRepository.countTests(d);
        var groupedCount = personTestRepository.countTestsByGroup(d);
        var overallExempt = personTestExemptionRepository.countExemptions(d);
        return new DashboardDescription(overallCount, overallExempt, groupedCount);
    }

    record DashboardDescription(PersonTestRepository.CountTests overall,
                                PersonTestExemptionRepository.CountExemptions overallExempt,
                                List<PersonTestRepository.CountGroupTests> grouped) {
    }
}
