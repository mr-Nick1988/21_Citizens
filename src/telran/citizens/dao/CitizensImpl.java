package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.*;

public class CitizensImpl implements Citizens {
    private static final Comparator<Person> lastNameComparator;
    private static final Comparator<Person> ageComparator;

    static {
        lastNameComparator = (p1, p2) -> {
            int res = p1.getLastName().compareTo(p2.getLastName());
            return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
        };
        ageComparator = (p1, p2) -> {
            int res = Integer.compare(p1.getAge(), p2.getAge());
            return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
        };
    }

    private final TreeSet<Person> idCollection;
    private final TreeSet<Person> lastNameCollection;
    private final TreeSet<Person> ageCollection;


    public CitizensImpl() {
        idCollection = new TreeSet<>();
        lastNameCollection = new TreeSet<>(lastNameComparator);
        ageCollection = new TreeSet<>(ageComparator);
    }

    public CitizensImpl(List<Person> citizens) {
        this();
        citizens.forEach(p -> add(p));
    }

    //O(log n)
    @Override
    public boolean add(Person person) {
        if (person == null || !idCollection.add(person)) {
            return false;
        }
        lastNameCollection.add(person);
        ageCollection.add(person);
        return true;
    }

    //O(log n)
    @Override
    public boolean remove(int id) {
        Person victim = find(id);
        if (victim == null) {
            return false;
        }
        return idCollection.remove(victim) && lastNameCollection.remove(victim) && ageCollection.remove(victim);
    }

    //O(log n)
    @Override
    public Person find(int id) {
        Person someone = new Person(id, null, null, null);
        Person res = idCollection.ceiling(someone);
        return (res != null && res.getId() == id) ? res : null;

    }

    //O(log m)
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        Person someoneFrom = new Person(Integer.MIN_VALUE,null,null,LocalDate.now().minusYears(minAge));
        Person someoneTo = new Person(Integer.MAX_VALUE,null,null,LocalDate.now().minusYears(maxAge));
        return ageCollection.subSet(someoneFrom ,true,someoneTo,true);

    }

    //O(log n)
    @Override
    public Iterable<Person> find(String lastName) {
        Person someoneFrom = new Person(Integer.MIN_VALUE, null, lastName, null);
        Person someoneTo = new Person(Integer.MAX_VALUE,null,lastName,null);
        return lastNameCollection.subSet(someoneFrom, someoneTo);
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
