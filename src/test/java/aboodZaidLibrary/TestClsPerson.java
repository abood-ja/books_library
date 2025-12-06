package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestClsPerson {

    @Test
    void testConstructorAndGetters() {
        clsPerson person = new clsPerson("John", "Doe", "john@example.com", "123456789");
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("john@example.com", person.getEmail());
        assertEquals("123456789", person.getPhone());
        assertEquals("John Doe", person.getFullName());
    }

    @Test
    void testSetFirstName() {
        clsPerson person = new clsPerson("John", "Doe", "", "");
        person.setFirstName("Alice");
        assertEquals("Alice", person.getFirstName());
    }

    @Test
    void testSetLastName() {
        clsPerson person = new clsPerson("John", "Doe", "", "");
        person.setLastName("Smith");
        assertEquals("Smith", person.getLastName());
    }

    @Test
    void testSetEmail() {
        clsPerson person = new clsPerson("", "", "old@example.com", "");
        person.setEmail("new@example.com");
        assertEquals("new@example.com", person.getEmail());
    }

    @Test
    void testSetPhone() {
        clsPerson person = new clsPerson("", "", "", "111");
        person.setPhone("999");
        assertEquals("999", person.getPhone());
    }

    @Test
    void testGetFullName_AfterSetters() {
        clsPerson person = new clsPerson("John", "Doe", "", "");
        person.setFirstName("Alice");
        person.setLastName("Smith");
        assertEquals("Alice Smith", person.getFullName());
    }
}
