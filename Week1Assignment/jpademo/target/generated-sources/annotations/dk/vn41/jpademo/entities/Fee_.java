package dk.vn41.jpademo.entities;

import dk.vn41.jpademo.entities.Person;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-09-23T13:27:04")
@StaticMetamodel(Fee.class)
public class Fee_ { 

    public static volatile SingularAttribute<Fee, Integer> amount;
    public static volatile SingularAttribute<Fee, Person> person;
    public static volatile SingularAttribute<Fee, Long> id;
    public static volatile SingularAttribute<Fee, Date> payDate;

}