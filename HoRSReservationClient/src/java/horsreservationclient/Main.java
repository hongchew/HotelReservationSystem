/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateful.RoomReservationControllerRemote;

/**
 *
 * @author Hong Chew
 */
public class Main {

    @EJB
    private static RoomReservationControllerRemote roomReservationController;

    @EJB
    private static GuestSessionBeanRemote guestSessionBean;
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(guestSessionBean, roomReservationController);
        mainApp.runApp();
        
    }
    
}
