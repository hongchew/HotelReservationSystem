/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.RoomRankingEntity;
import entity.RoomTypeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    
    
    public InitialisationSessionBean() {
    }
    
    
    @PostConstruct
    public void postConstruct(){
        try{
            
            employeeSessionBean.retrieveEmployeeByUsername("admin"); //first startup, no default admin account
            
        }catch(EmployeeNotFoundException ex){
            
            employeeSessionBean.createNewSysAdmin("admin", "admin", "password");
            em.persist(new RoomRankingEntity(new Long(1)));
            
            roomSessionBean.returnNewRoomTypeEntity("Deluxe Room", "A comfortable room that will satisfy any", "1 Queen Size", 1, "Mini fridge, bathroom, television, internet", 0);
            
            roomSessionBean.returnNewRoomTypeEntity("Premier Room", "A premium room that will satisfy any", "1 King Size", 1, "Mini fridge, bathroom, television, internet", 0);
            
            roomSessionBean.returnNewRoomTypeEntity("Family Room", "A comfortable room that will a family will love", "1 Queen Size, 1 Single", 3, "Mini fridge, bathroom, television, internet", 0);
                        
            roomSessionBean.returnNewRoomTypeEntity("Junior Suite", "A premium suite that will satisfy any family", "2 King Size", 4, "Mini fridge, bathroom, television, internet", 0);       
            
            roomSessionBean.returnNewRoomTypeEntity("Grand Suite", "A premium suite that will satisfy any family", "2 King Size", 4, "Mini fridge, bathroom, television, internet, sofa", 0);
                
        }
    }

}
