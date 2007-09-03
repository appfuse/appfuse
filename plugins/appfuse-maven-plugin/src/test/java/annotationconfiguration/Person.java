package annotationconfiguration;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity(name = "Persona")
@Table(name = "person")
public class Person {
    private Long personId;
    private Date creationDate;
    private String email;
    private Date modificationDate;
    private String username;
    private boolean active;
    List<Item> items;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getPersonId() {
        return personId;
    }

    @Column(name = "creation_date", nullable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    @Column(name = "email_address", length = 40, unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    @Column(name = "modification_date", nullable = false)
    public Date getModificationDate() {
        return modificationDate;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
