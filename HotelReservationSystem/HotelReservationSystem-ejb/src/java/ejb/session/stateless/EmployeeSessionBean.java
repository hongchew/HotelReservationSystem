/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.EmployeeAccessRightsEnum;
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
                emp.getEmployeeId();
                emp.getAccessRights();
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
        
        EmployeeEntity sysAdmin = new EmployeeEntity(name, username, password, EmployeeAccessRightsEnum.SYSADMIN);
        em.persist(sysAdmin);
        
    }
    
    @Override
    public void createNewOpsManager(String name, String username, String password){
        
        EmployeeEntity opManager = new EmployeeEntity(name, username, password, EmployeeAccessRightsEnum.OPERATIONS);
        em.persist(opManager);
        
    }
    
    @Override
    public void createNewSalesManager(String name, String username, String password){
        
        EmployeeEntity salesManager = new EmployeeEntity(name, username, password, EmployeeAccessRightsEnum.SALES);
        em.persist(salesManager);
        
    }
    @Override
    public void createNewGuestRelationsOffr(String name, String username, String password){
        
        EmployeeEntity grOffr = new EmployeeEntity(name, username, password, EmployeeAccessRightsEnum.GUESTRELATIONS);
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
