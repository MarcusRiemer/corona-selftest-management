package de.akra.coronatestmanagement.controller;

import de.akra.coronatestmanagement.config.WebSecurityConfig;
import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.model.PersonGroup;
import de.akra.coronatestmanagement.model.PersonTest;
import de.akra.coronatestmanagement.model.PersonTestExemption;
import de.akra.coronatestmanagement.repository.PersonGroupRepository;
import de.akra.coronatestmanagement.repository.PersonRepository;
import de.akra.coronatestmanagement.repository.PersonTestRepository;
import de.akra.coronatestmanagement.util.TestDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Responsible for all data retrieval and manipulation that can be considered test
 * management.
 */
@RestController
@RequestMapping("/api/tests")
@Secured(WebSecurityConfig.ROLE_REPORTER)
public class CoronaTestController {
    private static final Logger log = LoggerFactory.getLogger(CoronaTestController.class);

    private final PersonGroupRepository groupRepository;
    private final PersonRepository personRepository;
    private final PersonTestRepository personTestRepository;

    @Autowired
    public CoronaTestController(
            PersonGroupRepository groupRepository,
            PersonRepository personRepository, PersonTestRepository personTestRepository) {
        this.groupRepository = groupRepository;
        this.personRepository = personRepository;
        this.personTestRepository = personTestRepository;
    }

    /**
     * Dashboard data for all groups that may have a test today.
     */
    @GetMapping("/groups")
    public List<PeopleGroupListDescription> groupsForDay(
            @RequestParam() Optional<String> date
    ) {
        log.info("Requesting groups for {}", date);

        var d = date.isPresent() ? TestDate.parse(date.get()) : LocalDate.now();
        var queryResult = groupRepository.findGroupWithNumTestsByDay(d).stream().toList();

        log.debug("Got {} groups, expected {}", queryResult.size(), groupRepository.count());

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

        // Okay, there doesn't
        var people = personRepository.findPeopleForGroupByDate(groupId)
                .stream()
                .collect(Collectors.toMap(Person::getId, Function.identity()));
        var testsByPerson = personTestRepository.findTestsForGroupByDate(groupId, d)
                .stream()
                .collect(Collectors.toMap(PersonTest::getPersonId, Function.identity()));

        List<PersonTestResultDescription> results = people.values()
                .stream()
                .map(t -> new PersonTestResultDescription(t, Optional.of(testsByPerson.get(t.getId())), t.getExemptions())).toList();

        return new TestResultsGroupList(d, g, results);
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
            //if (params.testAll || !person.isExempt()) {
                tests.add(new PersonTest(person, date));
            //}
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

    public record CreateGroupTestSeriesParams(UUID groupId, String testDate, boolean testAll) {
        public LocalDate getTestDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            return LocalDate.parse(testDate.replace('/', '-'), formatter);
        }
    }

    public record TestResultsGroupList(LocalDate date, PersonGroup group, List<PersonTestResultDescription> results) {
    }

    public record PersonTestResultDescription(Person person, Optional<PersonTest> result, Set<PersonTestExemption> exemptions) {
    }

    public record PeopleGroupListDescription(UUID id, String name, int personCount, int testCount) {

    }


}
