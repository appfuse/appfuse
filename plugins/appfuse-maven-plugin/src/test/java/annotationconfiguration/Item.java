package annotationconfiguration;

import javax.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    Long id;
    Person Person;
    String name;


    public Item() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "name" ,nullable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToOne()
    @JoinColumn(name = ("username"))
    public Person getPerson() {
        return Person;
    }

    public void setPerson(Person person) {
        Person = person;
    }
}
