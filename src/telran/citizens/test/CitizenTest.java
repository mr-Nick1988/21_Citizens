package telran.citizens.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.citizens.dao.CitizenImpl;
import telran.citizens.dao.Citizens;
import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CitizenTest {
    private Citizens citizens;
    private static final LocalDate now = LocalDate.now();

    @BeforeEach
    void setUp() {
        citizens = new CitizenImpl();
        citizens.add(new Person(1, "Valera", "Nedayborcha", now.minusYears(23)));
        citizens.add(new Person(2, "Olga", "Petrova", now.minusYears(20)));
        citizens.add(new Person(3, "Ivan", "Ivanov", now.minusYears(20)));
        citizens.add(new Person(4, "Svetlana", "Ivanov", now.minusYears(42)));
    }

    @Test
    void testAdd() {
        Person person1 = new Person(5, "Duk", "Laserde", now.minusYears(25));
        assertTrue(citizens.add(person1));
        assertEquals(5, citizens.size());
        assertFalse(citizens.add(person1));
        assertEquals(5, citizens.size());
    }

    @Test
    void testRemove() {
        assertTrue(citizens.remove(1));
        assertNull(citizens.find(1));
        assertEquals(3, citizens.size());
        assertFalse(citizens.remove(999));
    }

    @Test
    void testFind() {
        Person found = citizens.find(2);
        assertNotNull(found);
        assertEquals("Olga", found.getFirstName());
        assertEquals("Petrova", found.getLastName());
        assertEquals(20, found.getAge());
        assertNull(citizens.find(999));
    }

    @Test
    void testFindByAgeRange() {
        List<Person> ageRange = new ArrayList<>();
        Iterable<Person> people = citizens.find(20, 30);
        for (Person p : people) {
            ageRange.add(p);
        }
        assertEquals(3, ageRange.size());
        boolean hasSvetlana = false;
        for (Person p : ageRange) {
            if (p.getFirstName().equals("Svetlana")) {
                hasSvetlana = true;
                break;
            }
        }
        assertFalse(hasSvetlana, "Svetlana (42) should not be in the age range");
    }

    @Test
    void testGetAllPersonsSortedById() {
        List<Person> sortedById = new ArrayList<>();
        Iterable<Person> people = citizens.getAllPersonsSortedById();
        for (Person p : people) {
            sortedById.add(p);
        }
        assertEquals(4, sortedById.size());
        assertEquals(1, sortedById.get(0).getId());
        assertEquals(2, sortedById.get(1).getId());
        assertEquals(3, sortedById.get(2).getId());
        assertEquals(4, sortedById.get(3).getId());
    }

    @Test
    void testGetAllPersonsSortedByAge() {
        List<Person> sortedByAge = new ArrayList<>();
        Iterable<Person> people = citizens.getAllPersonsSortedByAge();
        for (Person p : people) {
            sortedByAge.add(p);
        }
        assertEquals(4, sortedByAge.size());
        assertEquals("Ivan", sortedByAge.get(0).getFirstName());
        assertEquals("Olga", sortedByAge.get(1).getFirstName());
        assertEquals("Valera", sortedByAge.get(2).getFirstName());
        assertEquals("Svetlana", sortedByAge.get(3).getFirstName());
    }

    @Test
    void testGetAllPersonsSortedByLastName() {
        List<Person> sortedByLastName = new ArrayList<>();
        Iterable<Person> people = citizens.getAllPersonsSortedByLastName();
        for (Person p : people) {
            sortedByLastName.add(p);
        }
        assertEquals(4, sortedByLastName.size());
        assertEquals("Ivanov", sortedByLastName.get(0).getLastName());
        assertEquals("Ivanov", sortedByLastName.get(1).getLastName());
        assertEquals("Nedayborcha", sortedByLastName.get(2).getLastName());
        assertEquals("Petrova", sortedByLastName.get(3).getLastName());
    }

    @Test
    void testSize() {
        assertEquals(4, citizens.size());
    }

}