package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CitizensImpl implements Citizens {
    // Comparator lastNameComparator:
    // Используется для сортировки списка lastNameCollection.
    // Сравнивает объекты Person сначала по фамилии (lastName), затем, при равенстве фамилий, по ID.
    // Это гарантирует стабильную сортировку, когда несколько Person имеют одинаковые фамилии.
    private static final Comparator<Person> lastNameComparator;

    // Comparator ageComparator:
    // Используется для сортировки списка ageCollection.
    // Сравнивает объекты Person сначала по возрасту (age), затем, при равенстве возрастов, по ID.
    // Обеспечивает стабильную сортировку, когда несколько Person имеют одинаковый возраст.
    private static final Comparator<Person> ageComparator;

    static {
        lastNameComparator = (p1, p2) -> {
            int res = p1.getLastName().compareTo(p2.getLastName());
            return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
        };
        ageComparator = (p1, p2) ->{
            int res = Integer.compare(p1.getAge(), p2.getAge());
            return res!=0?res:Integer.compare(p1.getId(), p2.getId());
        };
    }

    // idCollection:
    // Список для хранения объектов Person, отсортированный по ID.
    // Используется для быстрого поиска Person по ID с помощью бинарного поиска.
    private final List<Person> idCollection;

    // lastNameCollection:
    // Список для хранения объектов Person, отсортированный по фамилии (lastName) с помощью lastNameComparator.
    // Используется для быстрого поиска Person по фамилии с помощью бинарного поиска.
    private final List<Person> lastNameCollection;

    // ageCollection:
    // Список для хранения объектов Person, отсортированный по возрасту (age) с помощью ageComparator.
    // Используется для быстрого поиска Person по возрасту с помощью бинарного поиска.
    private final List<Person> ageCollection;

    // Конструктор по умолчанию:
    // Инициализирует все три списка пустыми ArrayList.
    public CitizensImpl() {
        idCollection = new ArrayList<>();
        lastNameCollection = new ArrayList<>();
        ageCollection = new ArrayList<>();
    }

    // Конструктор с параметром List<Person>:
    // Инициализирует объект CitizensImpl, добавляя все Person из переданного списка.
    // Использует конструктор по умолчанию для инициализации списков, а затем добавляет элементы.
    public CitizensImpl(List<Person> citizens) {
        this(); // Вызов конструктора по умолчанию.
        citizens.forEach(p -> add(p)); // Добавление каждого Person из списка.
    }

    // Метод add(Person person):
    // Добавляет объект Person во все три списка, поддерживая их отсортированными.
    // Использует Collections.binarySearch для поиска позиции вставки в каждом списке.
    // Сложность: O(log n) для поиска позиции вставки в каждом списке, O(n) для вставки в ArrayList.
    @Override
    public boolean add(Person person) {
        // Проверка на null:
        // Если переданный Person равен null, возвращает false.
        if (person == null) {
            return false;
        }
        // Поиск позиции для вставки в idCollection:
        // Использует бинарный поиск для нахождения позиции вставки.
        // Если Person с таким ID уже существует, возвращает false.
        int index = Collections.binarySearch(idCollection, person);
        if (index >= 0) {
            return false;
        }
        // Вычисление позиции вставки:
        // Если Person не найден, binarySearch возвращает отрицательное значение.
        // Вычисляем индекс вставки по формуле -index - 1.
        index = -index - 1;
        idCollection.add(index, person); // Вставка Person в idCollection.

        // Поиск позиции для вставки в ageCollection:
        // Аналогично idCollection, но используется ageComparator.
        index = Collections.binarySearch(ageCollection, person, ageComparator);
        index = index >= 0 ? index : -index - 1;
        ageCollection.add(index, person); // Вставка Person в ageCollection.

        // Поиск позиции для вставки в lastNameCollection:
        // Аналогично idCollection, но используется lastNameComparator.
        index = Collections.binarySearch(lastNameCollection, person, lastNameComparator);
        index = index >= 0 ? index : -index - 1;
        lastNameCollection.add(index, person); // Вставка Person в lastNameCollection.

        return true; // Возвращает true, если Person успешно добавлен.
    }

    // Метод remove(int id):
    // Удаляет Person из всех трех списков по ID.
    // Использует метод find(int id) для поиска Person.
    // Сложность: O(log n) для поиска, O(n) для удаления из ArrayList.
    @Override
    public boolean remove(int id) {
        // Поиск Person по ID:
        // Использует метод find(int id) для поиска Person в idCollection.
        Person victim = find(id);
        if (victim == null) {
            return false; // Если Person не найден, возвращает false.
        }
        // Удаление Person из всех списков:
        // Удаляет Person из idCollection, lastNameCollection и ageCollection.
        return idCollection.remove(victim) && lastNameCollection.remove(victim) && ageCollection.remove(victim);
    }

    // Метод find(int id):
    // Ищет Person по ID в idCollection с помощью бинарного поиска.
    // Сложность: O(log n).
    @Override
    public Person find(int id) {
        // Создание "фиктивного" Person для поиска:
        // Создает объект Person с заданным ID, но без других данных.
        int index = Collections.binarySearch(idCollection, new Person(id, "", "", null));
        return index >= 0 ? idCollection.get(index) : null; // Возвращает Person, если найден, иначе null.
    }

    // Метод find(int minAge, int maxAge):
    // Ищет Person в заданном диапазоне возрастов в ageCollection.
    // Использует бинарный поиск для определения границ диапазона.
    // Сложность: O(log n).
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        // Создание "фиктивного" Person для поиска нижней границы:
        // Создает объект Person с минимальным ID и возрастом minAge.
        Person fromPerson = new Person(Integer.MIN_VALUE, null, null, LocalDate.now().minusYears(minAge));
        int from = -Collections.binarySearch(ageCollection, fromPerson, ageComparator)-1;

        // Создание "фиктивного" Person для поиска верхней границы:
        // Создает объект Person с максимальным ID и возрастом maxAge.
        Person toPerson = new Person(Integer.MAX_VALUE, null, null, LocalDate.now().minusYears(maxAge));
        int to = -Collections.binarySearch(ageCollection, toPerson, ageComparator)-1;

        // Возвращает подсписок Person в заданном диапазоне возрастов:
        // Возвращает часть ageCollection от from до to.
        return ageCollection.subList(from, to);
    }

    // Метод find(String lastName):
    // Ищет Person с заданной фамилией в lastNameCollection.
    // Использует бинарный поиск для определения границ диапазона.
    // Сложность: O(log n).
    @Override
    public Iterable<Person> find(String lastName) {
        // Создание "фиктивного" Person для поиска нижней границы:
        // Создает объект Person с минимальным ID и заданной фамилией.
        Person someone = new Person(Integer.MIN_VALUE, null, lastName, null);
        int from = -Collections.binarySearch(lastNameCollection, someone, lastNameComparator) - 1;

        // Создание "фиктивного" Person для поиска верхней границы:
        // Создает объект Person с максимальным ID и заданной фамилией.
        someone = new Person(Integer.MAX_VALUE, null, lastName, null);
        int to = -Collections.binarySearch(lastNameCollection, someone, lastNameComparator) - 1;

        // Возвращает подсписок Person с заданной фамилией:
        // Возвращает часть lastNameCollection от from до to.
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
