/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import util.exception.EmployeeNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author Hong Chew
 */
@Singleton
@Local(InitialisationSessionBeanLocal.class)
@Startup
public class InitialisationSessionBean implements InitialisationSessionBeanLocal {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

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
            employeeSessionBean.createNewOpsManager("operationmanager", "operationmanager", "password");
            employeeSessionBean.createNewSalesManager("salesmanager", "salesmanager", "password");
            employeeSessionBean.createNewGuestRelationsOffr("guestrelationsofficer", "guestrelationsofficer", "password");
            
            
            Date today = new Date();
            
            try {
                RoomTypeEntity deluxeRoom = roomSessionBean.returnNewRoomTypeEntity("Deluxe Room", "A comfortable room that will satisfy any", "1 Queen Size", 1, "Mini fridge, bathroom, television, internet", 0);
                roomSessionBean.createNewNormalRate("Deluxe Room Normal Rate", new BigDecimal(50.00), today, null, deluxeRoom.getTypeId());
                roomSessionBean.createNewPublishedRate("Deluxe Room Published Rate", new BigDecimal(60.00), today, null, deluxeRoom.getTypeId());
                for(int i  = 1; i <= 5; i++){
                    roomSessionBean.createNewRoom(1, i, "Deluxe Room");
                }
                
                RoomTypeEntity premierRoom = roomSessionBean.returnNewRoomTypeEntity("Premier Room", "A premium room that will satisfy any", "1 King Size", 1, "Mini fridge, bathroom, television, internet", 0);
                roomSessionBean.createNewNormalRate("Premier Room Normal Rate", new BigDecimal(75.00), today, null, premierRoom.getTypeId());
                roomSessionBean.createNewPublishedRate("Premier Room Published Rate", new BigDecimal(80.00), today, null, premierRoom.getTypeId());
                for(int i  = 1; i <= 5; i++){
                    roomSessionBean.createNewRoom(2, i, "Premier Room");
                }
                
                RoomTypeEntity familyRoom = roomSessionBean.returnNewRoomTypeEntity("Family Room", "A comfortable room that will a family will love", "1 Queen Size, 1 Single", 3, "Mini fridge, bathroom, television, internet", 0);
                roomSessionBean.createNewNormalRate("Family Room Normal Rate", new BigDecimal(100.00), today, null, familyRoom.getTypeId());
                roomSessionBean.createNewPublishedRate("Family Room Published Rate", new BigDecimal(120.00), today, null, familyRoom.getTypeId());
                for(int i  = 1; i <= 5; i++){
                    roomSessionBean.createNewRoom(3, i, "Family Room");
                }
                
                RoomTypeEntity juniorSuite = roomSessionBean.returnNewRoomTypeEntity("Junior Suite", "A premium suite that will satisfy any family", "2 King Size", 4, "Mini fridge, bathroom, television, internet", 0);                
                roomSessionBean.createNewNormalRate("Junior Suite Normal Rate", new BigDecimal(150.00), today, null, juniorSuite.getTypeId());
                roomSessionBean.createNewPublishedRate("Junior Suite Published Rate", new BigDecimal(170.00), today, null, juniorSuite.getTypeId());
                for(int i  = 1; i <= 5; i++){
                    roomSessionBean.createNewRoom(4, i, "Junior Suite");
                }
                
                RoomTypeEntity grandSuite = roomSessionBean.returnNewRoomTypeEntity("Grand Suite", "The grandest suite we have to offer", "2 King Size", 4, "Full size fridge, 2 bathrooms, 2 television, internet, sofa", 0);
                roomSessionBean.createNewNormalRate("Grand Suite Normal Rate", new BigDecimal(300.00), today, null, grandSuite.getTypeId());
                roomSessionBean.createNewPublishedRate("Grand Suite Published Rate", new BigDecimal(350.00), today, null, grandSuite.getTypeId());      
                for(int i  = 1; i <= 5; i++){
                    roomSessionBean.createNewRoom(5, i, "Grand Suite");
                }
                
            } catch (RoomTypeNotFoundException roomTypeNotFoundException) {
                System.err.println(roomTypeNotFoundException.getMessage());
            } catch(ConstraintViolationException e){
                Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
                for(ConstraintViolation<?> v : violations){
                    System.err.println(v.getMessage());
                }
            }
           
        }
    }

}
