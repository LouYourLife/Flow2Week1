package facades;

import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Person;
import exceptions.PersonNotFoundException;
import interfaces.IPersonFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author vnord
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    private PersonFacade() {

    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) {
        EntityManager em = emf.createEntityManager();
        try {
            Person p = new Person(fName, lName, phone);
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            PersonDTO person = new PersonDTO(p);
            return person;
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException(String.format("Could not delete, id: %d does not exist", id));
        } else {
            try {
                em.getTransaction().begin();
                em.remove(person);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person == null) {
                throw new PersonNotFoundException(String.format("Person with id : (%d) not found", id));
            } else {
                return new PersonDTO(person);
            }
        } finally {
            em.close();
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery tq = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> persons = tq.getResultList();
            PersonsDTO list = new PersonsDTO(persons);
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Person person = em.find(Person.class, p.getId());
            person.setfName(p.getfName());
            person.setlName(p.getlName());
            person.setPhone(p.getPhone());
            person.setLastEdited();

            em.getTransaction().commit();

            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

}
