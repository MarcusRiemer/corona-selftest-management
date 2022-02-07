package de.akra.coronatestmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class PersonTest {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Origin origin = Origin.UNKNOWN;

    @Enumerated(EnumType.STRING)
    private Result result = Result.UNKNOWN;

    @ManyToOne()
    private Person person;

    private LocalDate date;

    protected PersonTest() {

    }

    public PersonTest(Person person, LocalDate date) {
        this.person = person;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public State getState() {
        return new State(origin, result);
    }

    @JsonIgnore
    public Person getPerson() {
        return person;
    }

    public void setOrigin(String origin) {
        this.origin = Origin.valueOf(origin.toUpperCase());
    }

    public void setResult(String result) {
        this.result = Result.valueOf(result.toUpperCase());
    }

    /**
     * Tests may be conducted at different sites. For the moment
     * we assume that these are the only valid sites.
     */
    public enum Origin {
        SCHOOL, THIRD_PARTY, SELF_DISCLOSURE, UNKNOWN
    }

    public enum Result {
        POSITIVE, NEGATIVE, UNKNOWN
    }

    public record State(Origin origin, Result result) {
    }

}
