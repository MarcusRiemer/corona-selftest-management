package de.akra.coronatestmanagement.controller;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.akra.coronatestmanagement.config.WebSecurityConfig;
import de.akra.coronatestmanagement.model.Person;
import de.akra.coronatestmanagement.model.PersonGroup;
import de.akra.coronatestmanagement.repository.PersonGroupRepository;
import de.akra.coronatestmanagement.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

    @Autowired
    public AdministrationController(PersonGroupRepository groupRepository, PersonRepository personRepository) {
        this.groupRepository = groupRepository;
        this.personRepository = personRepository;
    }

    @PostMapping("/personBatchImport")
    public ImportResult handleFileUpload(@RequestParam("csvFile") MultipartFile file) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");

        List<String> errors = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        // TODO: Better batching: Firing two queries per person seems expensive
        for (var raw : values) {
            try {
                CsvLine line = (CsvLine) raw;

                // TODO: Actually use these dates
                LocalDate vaccDate = !line.vaccinated.isBlank() ? LocalDate.from(formatter.parse(line.vaccinated)) : null;
                LocalDate recoverDate = !line.recovered.isBlank() ? LocalDate.from(formatter.parse(line.recovered)) : null;

                var group = groupByName.get(line.group);

                var person = new Person(line.firstName, line.lastName, group);
                Example<Person> example = Example.of(person);
                if (this.personRepository.findOne(example).isPresent()) {
                    skipped.add(line.toString());
                } else {
                    this.personRepository.save(person);
                }
            } catch (Exception ex) {
                errors.add(raw.toString());
            }
        }

        return new ImportResult(values.size(), errors, skipped, missingGroupNames);
    }

    record CsvLine(String firstName, String lastName, String vaccinated, String recovered, String group) {
    }

    record ImportResult(int numLines, List<String> errors, List<String> skipped, List<String> newGroups) {
    }
}
