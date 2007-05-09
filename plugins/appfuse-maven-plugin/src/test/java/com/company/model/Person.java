package com.company.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * This class is used to test the install feature of this plugin.
 * Ideally, this could all be done with Java code (see InstallArtifactsMojoTest),
 * but MavenEmbedder doesn't seem to recognize plugins with extensions (i.e. the warpath plugin).
 */
@Entity
@Table(name = "person")
public class Person {
    private Long id;
    private Date creationDate;
    private String email;
    private Date modificationDate;
    private String username;
    private boolean active;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
}