package telran.citizens.model;

import java.time.LocalDate;
import java.time.Period;

public class Person implements Comparable<Person> {
    int id;
    String firstName;
    String lastName;
    LocalDate birthDate;

    public Person(int id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;

    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int compareTo(Person person) {
        return Integer.compare(this.id, person.id);
    }

        public int getAge(){
        LocalDate today = LocalDate.now();
        return Period.between(birthDate,today).getYears();
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    @Override
    public final boolean equals(Object object) {
        if (!(object instanceof Person person)) return false;

        return id == person.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

