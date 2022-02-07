package de.akra.coronatestmanagement.controller;

import de.akra.coronatestmanagement.config.WebSecurityConfig;
import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.model.PersonGroup;
import de.akra.coronatestmanagement.model.PersonTest;
import de.akra.coronatestmanagement.repository.PersonGroupRepository;
import de.akra.coronatestmanagement.repository.PersonTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Responsible for all data retrieval and manipulation that can be considered test
 * management.
 */
@RestController
@RequestMapping("/api/tests")
@Secured(WebSecurityConfig.ROLE_REPORTER)
public class CoronaTestController {
    private final PersonGroupRepository groupRepository;
    private final PersonTestRepository personTestRepository;

    @Autowired
    public CoronaTestController(
            PersonGroupRepository groupRepository,
            PersonTestRepository personTestRepository) {
        this.groupRepository = groupRepository;
        this.personTestRepository = personTestRepository;
    }

    /**
     * Dashboard data for all groups that may have a test today.
     */
    @GetMapping("/groupsToday")
    public List<PeopleGroupListDescription> groupsToday() {
        var d = LocalDate.now();
        var queryResult = groupRepository.findGroupWithNumTestsByDay(d);
        return queryResult
                .stream()
                .map(p -> new PeopleGroupListDescription(p.getGroup().getId(), p.getGroup().getName(), p.getGroup().getCount(), p.getNumTests()))
                .toList();
    }

    /**
     * Detailed test data for a specific group.
     */
    @GetMapping("/forGroup/{groupId}")
    public TestResultsGroupList forGroup(@PathVariable UUID groupId) {
        var d = LocalDate.now();
        var g = groupRepository.findById(groupId).orElseThrow();
        var tests = personTestRepository.findTestForGroupByDate(groupId, d);

        return new TestResultsGroupList(d, g, tests.stream().map(t -> new PersonTestResultDescription(t.getPerson(), t)).toList());
    }

    /**
     * Tests for people are always explicitly created with "UNKNOWN" values.
     */
    @PostMapping("/createForGroup")
    public boolean createForGroup(@RequestBody CreateGroupTestSeriesParams params) {
        var date = params.getTestDate();
        var personGroup = this.groupRepository.findById(params.groupId).orElseThrow();

        ArrayList<PersonTest> tests = new ArrayList<>(personGroup.getPeople().size());
        for (var person : personGroup.getPeople()) {
            tests.add(new PersonTest(person, date));
        }
        personTestRepository.saveAll(tests);

        return true;
    }

    /**
     * Update multiple test cases at once. In many cases the client will send a list
     * with only a single item but having a batch option as default eases the few
     * vital batch operations that are actually required.
     */
    @PostMapping("/update")
    public boolean update(@RequestBody List<UpdateTestParams> params) {
        for (var p : params) {
            PersonTest t = personTestRepository.findById(p.testId).orElseThrow();
            if (p.changedProp.equals("origin")) {
                t.setOrigin(p.value);
            } else if (p.changedProp.equals("result")) {
                t.setResult(p.value);
            }
            personTestRepository.save(t);
        }

        return true;
    }

    @DeleteMapping("/{testId}")
    public boolean delete(@PathVariable UUID testId) {
        personTestRepository.deleteById(testId);
        return true;
    }

    public record UpdateTestParams(UUID testId, String changedProp, String value) {
    }

    public record CreateGroupTestSeriesParams(UUID groupId, String testDate) {
        public LocalDate getTestDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            return LocalDate.parse(testDate.replace('/', '-'), formatter);
        }
    }

    public record TestResultsGroupList(LocalDate date, PersonGroup group, List<PersonTestResultDescription> results) {
    }

    public record PersonTestResultDescription(Person person, PersonTest result) {
    }

    public record PeopleGroupListDescription(UUID id, String name, int personCount, int testCount) {

    }


}
