package telran.citizens.dao;

import telran.citizens.model.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CitizenImpl implements Citizens {
    private Collection<Person> citizens = new ArrayList<>();

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
        Collections.sort(sorted);
        return sorted;
    }

    //O(n log n)
    @Override
    public Iterable<Person> getAllPersonsSortedByAge() {
        ArrayList<Person> sorted = new ArrayList<>(citizens);
        Collections.sort(sorted, (p1, p2) -> {
            int ageCompare = Integer.compare(p1.getAge(), p2.getAge());
            if (ageCompare != 0) {
                return ageCompare;
            }
            return p1.getFirstName().compareTo(p2.getFirstName());
        });
        return sorted;
    }

    //O(n log n)
    @Override
    public Iterable<Person> getAllPersonsSortedByLastName() {
        ArrayList<Person> sorted = new ArrayList<>(citizens);
        Collections.sort(sorted, (p1, p2) -> {
            int lastNameCompare = p1.getLastName().compareTo(p2.getLastName());
            if (lastNameCompare != 0) {
                return lastNameCompare;
            }
            return Integer.compare(p1.getId(), p2.getId());
        });
        return sorted;
    }

    //O(1)
    @Override
    public int size() {
        return citizens.size();
    }
}
