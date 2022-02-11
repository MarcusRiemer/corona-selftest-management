package de.akra.coronatestmanagement.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * People may be exempt from testing for a variety of reasons: Absence, vaccinated,
 * recovered, ... This entity tries to walk the line between reasons that have some
 * sort of possible meaning for the application / the analysis and free form reasons
 * that may be required for real life appearances.
 */
@Entity
public class PersonTestExemption {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Column(nullable = true)
    private String comment;

    @Column(nullable = false)
    private LocalDate begin;

    @Column(nullable = false)
    private LocalDate end;

    @ManyToOne
    private Person person;

    protected PersonTestExemption() {

    }

    public PersonTestExemption(Person person, Reason reason, String comment, LocalDate begin, LocalDate end) {
        this.person = person;
        this.reason = reason;
        this.comment = comment;
        this.begin = begin;
        this.end = end;
    }

    public Reason getReason() {
        return reason;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public enum Reason {
        VACCINATED, RECOVERED, ABSENT, CUSTOM
    }
}
