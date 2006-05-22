package org.appfuse.organization.model;

import java.util.Date;

import org.appfuse.model.BaseObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @hibernate.class table="department"
 */
public class Department extends BaseObject {
    private Long departmentId;
    private String departmentName;
    private String missionStatement;
    private Date createdDate;

    /**
     * @return Returns the id.
     * @hibernate.id column="department_id"
     *  generator-class="increment" unsaved-value="null"
     */
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @hibernate.property column="department_name" length="50" not-null="true"
     * @spring.validator type="required"
     */
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @hibernate.property column="mission_statement" length="50" not-null="true"
     * @spring.validator type="required"
     */
    public String getMissionStatement() {
        return missionStatement;
    }

    public void setMissionStatement(String missionStatement) {
        this.missionStatement = missionStatement;
    }
    
    /**
     * @hibernate.property column="created_date" length="20" not-null="true"
     * @spring.validator type="required"
     */
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public boolean equals(Object object) {
        if (!(object instanceof Department)) {
            return false;
        }
        Department rhs = (Department) object;
        return new EqualsBuilder().append(this.departmentName, rhs.departmentName)
                .append(this.missionStatement, rhs.missionStatement)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(1923026325, -1034774675)
                .append(this.departmentName).append(this.missionStatement)
                .toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("departmentId", this.departmentId)
                .append("missionStatement", this.missionStatement)
                .append("departmentName", this.departmentName).toString();
    }
}
