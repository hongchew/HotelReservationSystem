/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.GuestRelationOfficerEntity;
import entity.OperationManagerEntity;
import entity.SalesManagerEntity;
import entity.SystemAdminEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Hong Chew
 */
@Stateless
@Local(EmployeeSessionBeanLocal.class)
@Remote(EmployeeSessionBeanRemote.class)
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public EmployeeSessionBean() {
    }
    
    @Override
    public EmployeeEntity login(String username, String password) throws InvalidLoginCredentialException{
        try{
            EmployeeEntity emp = retrieveEmployeeByUsername(username);
            if(emp.getPassword().equals(password)){
                return emp;
            }else{
                throw new InvalidLoginCredentialException("Invalid Login Credentials");
            }
            
        }catch(EmployeeNotFoundException ex){
            throw new InvalidLoginCredentialException("Invalid Login Credentials");
        }
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException{
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.username = :username");
        query.setParameter("username", username);
        
        try{
        return (EmployeeEntity) query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex){
            throw new EmployeeNotFoundException("Invalid Login Credentials");
        }
    }
    
    @Override
    public void createNewSysAdmin(String name, String username, String password){
        
        SystemAdminEntity sysAdmin = new SystemAdminEntity(username, username, password);
        em.persist(sysAdmin);
        
    }
    
    @Override
    public void createNewOpsManager(String name, String username, String password){
        
        OperationManagerEntity opManager = new OperationManagerEntity(username, username, password);
        em.persist(opManager);
        
    }
    
    @Override
    public void createNewSalesManager(String name, String username, String password){
        
        SalesManagerEntity salesManager = new SalesManagerEntity(username, username, password);
        em.persist(salesManager);
        
    }
    @Override
    public void createNewGuestRelationsOffr(String name, String username, String password){
        
        GuestRelationOfficerEntity grOffr = new GuestRelationOfficerEntity(username, username, password);
        em.persist(grOffr);
        
    }
    
    @Override
    public List<EmployeeEntity> retrieveAllEmployees(){
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e");
        List<EmployeeEntity> list = query.getResultList();
        for(EmployeeEntity e: list){
            e.getEmployeeName();
            e.getAccessRights();
        }
        
        return list;
    }
}
