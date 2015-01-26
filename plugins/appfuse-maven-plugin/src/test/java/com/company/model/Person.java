package com.company.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

/**
 * This class is used to test the install feature of this plugin.
 * Ideally, this could all be done with Java code (see InstallArtifactsMojoTest),
 * but MavenEmbedder doesn't seem to recognize plugins with extensions (i.e. the warpath plugin).
 */
@Entity
@Table(name = "person")
@Indexed
public class Person implements java.io.Serializable {
    private Long id;
    private Date creationDate;
    private String email;
    private Integer age;
    private Date modificationDate;
    private String username;
    private boolean active;
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
    public Long getId() {
        return id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    @Column(name = "creation_date", nullable = false)
    @Field
    public Date getCreationDate() {
        return creationDate;
    }

    @Column(name = "email_address", length = 40, unique = true, nullable = false)
    @Field
    public String getEmail() {
        return email;
    }

    @Column(name = "modification_date", nullable = false)
    @Field
    public Date getModificationDate() {
        return modificationDate;
    }

    @Column(name = "username")
    @Field
    public String getUsername() {
        return username;
    }

    @Column(nullable = false)
    @Field
    public Integer getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (email != null ? !email.equals(person.email) : person.email != null) return false;
        if (version != null ? !version.equals(person.version) : person.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
