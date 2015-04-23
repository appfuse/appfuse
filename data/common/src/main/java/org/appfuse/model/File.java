package org.appfuse.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents an AppFuse File attachment object. The actual file
 * content is stored on the server filesystem but this meta-data object is persisted
 * in the database to record the file attachment upload. This could be modified/enhanced
 * to be a WORM or Write-Once-Read-Many implementation.
 *
 * @author <a href="mailto:david@citechnical.com">David L. Whitehurst</a>
 *         created: 4/23/15
 *         time: 12:10 AM
 * @version CHANGEME
 */
@Entity
@Table(name = "app_file_attachment")
public class File extends BaseObject {
    // @Todo members?
    private Long id;

    private String name;

    private String newFilename;

    private String contentType;

    private Long size;

    private String url;  // rest-like e.g. /file/4

    private String deleteUrl; // rest-like e.g. /delete/4

    private String deleteType; // @Todo determine if needed

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewFilename() {
        return newFilename;
    }

    public void setNewFilename(String newFilename) {
        this.newFilename = newFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Transient
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Transient
    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    @Transient
    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }

    /**
     * Returns a multi-line String with key=value pairs.
     *
     * @return a String representation of this class.
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Compares object equality. When using Hibernate, the primary key should
     * not be a part of this comparison.
     *
     * @param o object to compare to
     * @return true/false based on equality tests
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }

    /**
     * When you override equals, you should override hashCode. See "Why are
     * equals() and hashCode() importation" for more information:
     * http://www.hibernate.org/109.html
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
