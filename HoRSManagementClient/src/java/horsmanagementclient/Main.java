/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateful.RoomReservationControllerRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Hong Chew
 */
public class Main {

    @EJB
    private static RoomReservationControllerRemote roomReservationController;

    @EJB
    private static RoomSessionBeanRemote roomSessionBean;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;
    
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(roomSessionBean, employeeSessionBean, partnerSessionBean, roomReservationController);
        mainApp.runApp();
        
    }
    
}
