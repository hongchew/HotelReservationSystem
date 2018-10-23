/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Hong Chew
 */

public interface EmployeeSessionBeanRemote {

    public EmployeeEntity login(String username, String password) throws InvalidLoginCredentialException;

    public EmployeeEntity retrieveEmployeeByUsername(String username) throws InvalidLoginCredentialException;

    public void createNewSysAdmin(String name, String username, String password);

    public void createNewOpsManager(String name, String username, String password);

    public void createNewSalesManager(String name, String username, String password);

    public void createNewGuestRelationsOffr(String name, String username, String password);
    
}
