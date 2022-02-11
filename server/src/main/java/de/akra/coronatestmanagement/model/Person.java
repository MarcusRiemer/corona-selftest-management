package de.akra.coronatestmanagement.model;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Someone who needs to get tested. "Test Subject" would be an alternate name, but
 * that sounds kind of ... gross.
 */
@Entity
public class Person {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToOne()
    private PersonGroup personGroup;

    @OneToMany(mappedBy = "person")
    private Set<PersonTest> tests;

    @OneToMany(mappedBy = "person")
    private Set<PersonTestExemption> exemptions;

    protected Person() {
    }

    public Person(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    public Person(String firstName, String lastName, PersonGroup personGroup) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personGroup = personGroup;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<PersonTestExemption> getExemptions() {
        return exemptions;
    }

    public Set<PersonTest> getTests() {
        return tests;
    }

    public boolean isExempt() {
        return exemptions.size() > 0;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
