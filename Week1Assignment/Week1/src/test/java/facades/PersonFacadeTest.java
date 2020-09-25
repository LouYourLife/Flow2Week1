package facades;

import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.PersonNotFoundException;
import static java.util.Collections.list;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;

public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    private Person p1;
    private Person p2;
    private Person p3;

    private Address a1;
    private Address a2;
    private Address a3;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Hisirdoux", "Casperan", "12345678");
        p2 = new Person("Logan", "Sanders", "48632965");
        p3 = new Person("Tony", "Stark", "49238686");

        a1 = new Address("Somewhere", 144, "Nowhere");
        a2 = new Address("Here", 154, "Sure");
        a3 = new Address("There", 164, "Everywhere");

        p1.setAddress(a1);
        p2.setAddress(a2);
        p3.setAddress(a3);

        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Person").executeUpdate();
            em.createQuery("DELETE FROM Address").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addPerson method, of class PersonFacade.
     */
    @Test
    public void testAddPerson() {
        PersonDTO person = facade.addPerson("Virgil", "Sanders", "66677700", "Light side", 123, "Mind palace");
        PersonsDTO persons = facade.getAllPersons();
        List<PersonDTO> list = persons.getAll();

        assertEquals(4, list.size());
        assertTrue(person != null);
        assertThat(list, hasItems(
                Matchers.<PersonDTO>hasProperty("fName", is("Logan")),
                Matchers.<PersonDTO>hasProperty("fName", is("Hisirdoux")),
                Matchers.<PersonDTO>hasProperty("fName", is("Tony")),
                Matchers.<PersonDTO>hasProperty("fName", is("Virgil"))
        )
        );
    }

    /**
     * Test of deletePerson method, of class PersonFacade.
     */
    @Test
    public void testDeletePerson() throws PersonNotFoundException {
        PersonDTO person = facade.deletePerson(p1.getId());
        PersonsDTO persons = facade.getAllPersons();
        List<PersonDTO> list = persons.getAll();

        assertEquals(2, list.size());
        assertFalse(list.contains(person));
    }

    /**
     * Test of getPerson method, of class PersonFacade.
     */
    @Test
    public void testGetPerson() throws PersonNotFoundException {
        PersonDTO person = facade.getPerson(p1.getId());
        int exp = p1.getId();
        int res = person.getId();

        assertTrue(person != null);
        assertEquals(exp, res);
    }

    /**
     * Test of getAllPersons method, of class PersonFacade.
     */
    @Test
    public void testGetAllPersons() {
        PersonsDTO persons = facade.getAllPersons();
        List<PersonDTO> list = persons.getAll();

        assertThat(list, everyItem(hasProperty("lName")));
        assertThat(list, hasItems(
                Matchers.<PersonDTO>hasProperty("fName", is("Logan")),
                Matchers.<PersonDTO>hasProperty("fName", is("Hisirdoux")),
                Matchers.<PersonDTO>hasProperty("fName", is("Tony"))
        )
        );
    }

    /**
     * Test of editPerson method, of class PersonFacade.
     */
    @Test
    public void testEditPerson() throws PersonNotFoundException {
        //p1 = new Person("Hisirdoux", "Casperan", "12345678");
        PersonDTO person = new PersonDTO(p1);
        p1.setPhone("99999999");
        PersonDTO pEdit = facade.editPerson(new PersonDTO(p1));
        PersonsDTO persons = facade.getAllPersons();
        List<PersonDTO> list = persons.getAll();

        assertThat(person.getPhone(), is(not(pEdit.getPhone())));
        assertEquals(person.getfName(), pEdit.getfName());
        assertEquals(person.getlName(), pEdit.getlName());
    }

    @Test
    public void whenExceptionThrown_thenAssertionSucceeds() {
        Exception exception = assertThrows(PersonNotFoundException.class, () -> {
            PersonDTO person = facade.getPerson(100);
        });

        String expectedMessage = "Person with id : (100) not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
