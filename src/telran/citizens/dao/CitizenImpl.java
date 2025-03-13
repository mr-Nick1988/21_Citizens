package telran.citizens.dao;

import telran.citizens.model.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class CitizenImpl implements Citizens {
    private Collection<Person> citizens = new ArrayList<>();
    private final Comparator<Person> byIdComparator = (p1, p2) -> Integer.compare(p1.getId(), p2.getId());
    private final Comparator<Person> byAgeComparator = (p1, p2) -> {
        int ageCompare = Integer.compare(p1.getAge(), p2.getAge());
        return ageCompare != 0 ? ageCompare : p1.getFirstName().compareTo(p2.getFirstName());
    };
    private final Comparator<Person> byLastNameComparator = (p1, p2) -> {
        int lastNameCompare = p1.getLastName().compareTo(p2.getLastName());
        return lastNameCompare != 0 ? lastNameCompare : Integer.compare(p1.getId(), p2.getId());
    };

    //O(n)
    @Override
    public boolean add(Person person) {
        if (person == null || find(person.getId()) != null) {
            return false;
        }
        return citizens.add(person);
    }

    //O(n)
    @Override
    public boolean remove(int id) {
        Person person = find(id);
        return citizens.remove(person);
    }

    //O(n)
    @Override
    public Person find(int id) {
        for (Person person : citizens) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    //O(n)
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        ArrayList<Person> res = new ArrayList<>();
        for (Person person : citizens) {
            if (person.getAge() >= minAge && person.getAge() <= maxAge) {
                res.add(person);
            }
        }
        return res;
    }

    //O(n log n)
    @Override
    public Iterable<Person> getAllPersonsSortedById() {
        ArrayList<Person> sorted = new ArrayList<>(citizens);
        sorted.sort(byIdComparator);
        return sorted;
    }

    //O(n log n)
    @Override
    public Iterable<Person> getAllPersonsSortedByAge() {
        ArrayList<Person> sorted = new ArrayList<>(citizens);
        sorted.sort(byAgeComparator);
        return sorted;
    }

    //O(n log n)
    @Override
    public Iterable<Person> getAllPersonsSortedByLastName() {
        ArrayList<Person> sorted = new ArrayList<>(citizens);
        sorted.sort(byLastNameComparator);
        return sorted;
    }

    //O(1)
    @Override
    public int size() {
        return citizens.size();
    }
}
