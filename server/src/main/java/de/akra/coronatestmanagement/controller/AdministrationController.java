package de.akra.coronatestmanagement.controller;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.akra.coronatestmanagement.config.WebSecurityConfig;
import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.model.PersonGroup;
import de.akra.coronatestmanagement.model.PersonTestExemption;
import de.akra.coronatestmanagement.repository.PersonGroupRepository;
import de.akra.coronatestmanagement.repository.PersonRepository;
import de.akra.coronatestmanagement.repository.PersonTestExemptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/api/administration")
@Secured(WebSecurityConfig.ROLE_ADMIN)
public class AdministrationController {
    private static final Logger log = LoggerFactory.getLogger(AdministrationController.class);

    private final PersonGroupRepository groupRepository;
    private final PersonRepository personRepository;
    private final PersonTestExemptionRepository personTestExemptionRepository;

    @Autowired
    public AdministrationController(PersonGroupRepository groupRepository, PersonRepository personRepository, PersonTestExemptionRepository personTestExemptionRepository) {
        this.groupRepository = groupRepository;
        this.personRepository = personRepository;
        this.personTestExemptionRepository = personTestExemptionRepository;
    }

    @PostMapping("/personBatchImport")
    public ImportResult handleFileUpload(@RequestParam("csvFile") MultipartFile file) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();

        var now = LocalDate.now();

        // TODO: There must be a way to get a List<CsvLine> from Jackson?!
        List<Object> values = mapper.
                readerFor(CsvLine.class)
                .with(bootstrapSchema)
                .readValues(file.getInputStream())
                .readAll();

        var grouped = values.stream()
                .map(l -> (CsvLine) l)
                .collect(groupingBy(CsvLine::group));

        // TODO: Implement a testable version building upon CsvLine
        var requiredGroupNames = grouped.keySet();
        var existingGroups = this.groupRepository.findAllByName(requiredGroupNames);
        var existingGroupNames = existingGroups.stream().map(PersonGroup::getName).collect(Collectors.toSet());

        var missingGroupNames = requiredGroupNames.stream().filter(g -> !existingGroupNames.contains(g)).toList();
        var missingGroups = missingGroupNames.stream().map(n -> new PersonGroup(n)).toList();
        groupRepository.saveAll(missingGroups);
        existingGroups.addAll(missingGroups);

        var groupByName = existingGroups.stream().collect(toMap(PersonGroup::getName, g -> g));

        log.info("Missing group names for import: {}", missingGroupNames);

        List<ImportError> errors = new ArrayList<>();
        List<String> skippedPeople = new ArrayList<>();
        List<String> skippedExemptions = new ArrayList<>();
        int numPeople = 0;
        int numExemptions = 0;

        // TODO: Better batching: Firing two queries per person seems expensive
        for (var raw : values) {
            try {
                CsvLine line = (CsvLine) raw;

                // TODO: Actually use these dates
                LocalDate vaccDate = parseDate(line.vaccinated);
                LocalDate recoverDate = !line.recovered.isBlank() ? LocalDate.from(DATE_TIME_FORMATTER.parse(line.recovered)) : null;

                var group = groupByName.get(line.group);

                var person = new Person(line.firstName, line.lastName, group);
                var dbPerson = this.personRepository.findOne(Example.of(person));
                if (dbPerson.isPresent()) {
                    skippedPeople.add(line.toString());
                    person = dbPerson.get();
                } else {
                    // Ensure that we continue to work with the persisted person
                    person = this.personRepository.save(person);
                    numPeople++;
                }

                // TODO: Remove redundancy that is only here due to import statistics
                if (vaccDate != null) {
                    var importResult = this.possiblyImportExemption(person, vaccDate, PersonTestExemption.Reason.VACCINATED);
                    if (importResult.isPresent()) {
                        skippedExemptions.add(importResult.get().toString());
                    } else {
                        numExemptions++;
                    }
                }
                if (recoverDate != null) {
                    var importResult = this.possiblyImportExemption(person, recoverDate, PersonTestExemption.Reason.RECOVERED);
                    if (importResult.isPresent()) {
                        skippedExemptions.add(importResult.get().toString());
                    } else {
                        numExemptions++;
                    }
                }

            } catch (Exception ex) {
                errors.add(new ImportError(raw.toString(), ex.toString()));
            }
        }

        return new ImportResult(values.size(), numPeople, numExemptions, errors, skippedPeople, skippedExemptions, missingGroupNames);
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");

    /**
     * An attempt to sort of leniently but safely parse dates. Example CSV sometimes mixed up
     * "/" and "." delimiters.
     */
    private static LocalDate parseDate(String date) {
        if (date.isBlank()) {
            return null;
        } else {
            return LocalDate.from(DATE_TIME_FORMATTER.parse(date.replace("/", ".")));
        }
    }

    private Optional<PersonTestExemption> possiblyImportExemption(Person p, LocalDate endDate, PersonTestExemption.Reason reason) {
        var exemption = new PersonTestExemption(p, reason, "CSV Import", LocalDate.now(), endDate);

        // We rely on the existance of the comment and the end date to match already imported
        // absence reasons.
        var exemptionMatcher = ExampleMatcher.matchingAll().withIgnorePaths("begin");
        var example = Example.of(exemption, exemptionMatcher);

        var dbExemption = this.personTestExemptionRepository.findOne(example);
        if (dbExemption.isPresent()) {
            return dbExemption;
        } else {
            this.personTestExemptionRepository.save(exemption);
            return Optional.empty();
        }
    }

    /**
     * Raw structure of an entry in the uploaded CSV.
     */
    record CsvLine(String firstName, String lastName, String vaccinated, String recovered, String group) {
    }

    record ImportError(String line, String exception) {}

    /**
     * The client needs to know basic statistics about the operation.
     */
    record ImportResult(
            int numRecords,
            int numPeople,
            int numExemptions,
            List<ImportError> errors,
            List<String> skippedPeople,
            List<String> skippedExemptions,
            List<String> newGroups
    ) {
    }
}
