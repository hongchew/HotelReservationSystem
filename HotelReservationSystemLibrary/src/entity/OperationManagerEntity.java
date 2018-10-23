/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import util.enumeration.EmployeeAccessRightsEnum;

/**
 *
 * @author Hong Chew
 */
@Entity
public class OperationManagerEntity extends EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public OperationManagerEntity() {
    }

    public OperationManagerEntity(String employeeName, String username, String password) {
        super(employeeName, username, password, EmployeeAccessRightsEnum.OPERATIONS);
    }
    
    
    
}
