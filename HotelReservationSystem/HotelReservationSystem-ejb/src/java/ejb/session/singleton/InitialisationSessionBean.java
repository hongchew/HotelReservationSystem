/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author Hong Chew
 */
@Singleton
@Local(InitialisationSessionBeanLocal.class)
@Startup
public class InitialisationSessionBean implements InitialisationSessionBeanLocal {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    
    public InitialisationSessionBean() {
    }
    
    
    @PostConstruct
    public void postConstruct(){
        try{
            
            employeeSessionBean.retrieveEmployeeByUsername("admin"); //first startup, no default admin account
            
        }catch(EmployeeNotFoundException ex){
            
            employeeSessionBean.createNewSysAdmin("admin", "admin", "password");
            
        }
    }
}
