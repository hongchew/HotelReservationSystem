/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Size;
import util.enumeration.EmployeeAccessRightsEnum;

/**
 *
 * @author Hong Chew
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    
    @Size(min = 1, max = 32)
    @Column(nullable = false)
    private String employeeName;
    
    @Size(min = 5, max = 16)
    @Column(nullable = false, unique = true)
    private String username;
    
    @Size(min = 5, max = 32)
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeAccessRightsEnum accessRights;
            
    
    public EmployeeEntity() {
    }

    public EmployeeEntity(String employeeName, String username, String password, EmployeeAccessRightsEnum accessRights) {
        this.employeeName = employeeName;
        this.username = username;
        this.password = password;
        this.accessRights = accessRights;
    }

    
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof EmployeeEntity)) {
            return false;
        }
        EmployeeEntity other = (EmployeeEntity) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee Name: " + employeeName +
                "\nEmployee Role: " + accessRights + "\n";
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeeAccessRightsEnum getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(EmployeeAccessRightsEnum accessRights) {
        this.accessRights = accessRights;
    }

    
    
}
