package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CitizensImpl implements Citizens {
    private static final Comparator<Person> lastNameComparator = (p1, p2) -> {
        int res = p1.getLastName().compareTo(p2.getLastName());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };

    private static final Comparator<Person> ageComparator = (p1, p2) ->{
        int res = Integer.compare(p1.getAge(), p2.getAge());
        return res!=0?res:Integer.compare(p1.getId(), p2.getId());
    };
    private final List<Person> idCollection;
    private final List<Person> lastNameCollection;
    private final List<Person> ageCollection;

    public CitizensImpl() {
        idCollection = new ArrayList<>();
        lastNameCollection = new ArrayList<>();
        ageCollection = new ArrayList<>();
    }

    public CitizensImpl(List<Person> citizens) {
        this();
        citizens.forEach(p -> add(p));
    }

    // O(log(n)) + O(n) + O(log(n)) + O(n) + O(log(n)) + O(n) = 3*O(log(n)) + 3*O(n) = O(n)
    @Override
    public boolean add(Person person) {
        if (person == null) {
            return false;
        }
        int index = Collections.binarySearch(idCollection, person);
        if (index >= 0) {
            return false;
        }
        index = -index - 1;
        idCollection.add(index, person);
        index = Collections.binarySearch(ageCollection, person, ageComparator);
        index = index >= 0 ? index : -index - 1;
        ageCollection.add(index, person);
        index = Collections.binarySearch(lastNameCollection, person, lastNameComparator);
        index = index >= 0 ? index : -index - 1;
        lastNameCollection.add(index, person);
        return true;
    }

    // O(n)
    @Override
    public boolean remove(int id) {
        Person victim = find(id);
        if (victim == null) {
            return false;
        }
        return idCollection.remove(victim) && lastNameCollection.remove(victim) && ageCollection.remove(victim);
    }

    // O(log(n))
    @Override
    public Person find(int id) {
        int index = Collections.binarySearch(idCollection, new Person(id, "", "", null));
        return index >= 0 ? idCollection.get(index) : null;
    }

    // O(n)
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        Person fromPerson = new Person(Integer.MIN_VALUE, null, null, LocalDate.now().minusYears(minAge));
        int from = -Collections.binarySearch(ageCollection, fromPerson, ageComparator)-1;
        Person toPerson = new Person(Integer.MAX_VALUE, null, null, LocalDate.now().minusYears(maxAge));
        int to = -Collections.binarySearch(ageCollection, toPerson, ageComparator)-1;
        return ageCollection.subList(from, to);
    }

    // O(n)
    @Override
    public Iterable<Person> find(String lastName) {
        Person someone = new Person(Integer.MIN_VALUE, null, lastName, null);
        int from = -Collections.binarySearch(lastNameCollection, someone, lastNameComparator) - 1;
        someone = new Person(Integer.MAX_VALUE, null, lastName, null);
        int to = -Collections.binarySearch(lastNameCollection, someone, lastNameComparator) - 1;
        return lastNameCollection.subList(from, to);
    }

    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedById() {
        return idCollection;
    }

    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedByLastName() {
        return lastNameCollection;
    }

    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedByAge() {
        return ageCollection;
    }

    // O(1)
    @Override
    public int size() {
        return idCollection.size();
    }
}
