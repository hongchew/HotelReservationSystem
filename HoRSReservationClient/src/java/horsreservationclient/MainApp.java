/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import entity.GuestEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateful.RoomReservationControllerRemote;
import entity.ReservationRecordEntity;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;

/**
 *
 * @author Hong Chew
 */
public class MainApp {
    
    private GuestSessionBeanRemote guestSessionBean;
    private RoomReservationControllerRemote roomReservationController;
    private Scanner sc = new Scanner(System.in);
    
    public MainApp() {
    }

    public MainApp(GuestSessionBeanRemote guestSessionBean, RoomReservationControllerRemote roomReservationController) {
        this.guestSessionBean = guestSessionBean;
        this.roomReservationController = roomReservationController;
    }
    
    public void runApp(){
        System.out.println("****Welcome to the Hotel Reservation client****");
        while(true){
            System.out.println("(1) Register as Guest");
            System.out.println("(2) Guest Login");
            System.out.println("(3) Search Hotel Room");
            System.out.println("(4) Exit");
            
            String response = sc.next();
            switch(response){
                case "1":
                    registerGuest();
                    break;
                case "2":
                    guestLogin();
                    break;
                case "3":
                    break;
                case "4":
                    System.out.println("Exiting system");
                    System.exit(0);
                    break;
                default:
                    System.err.println("Invalid command");
            }
        }   
    }
    
    private void registerGuest(){
        System.out.println("****Register New Guest****");
        System.out.println("Enter name");
        String name = sc.nextLine();
        System.out.println("Enter username");
        String username = sc.next();
        System.out.println("Enter password");
        String password = sc.next();
        System.out.println("Enter email address");
        String email = sc.next();
        
        guestSessionBean.registerNewGuest(name, username, password, email);
        System.out.println("New Guest Created!");
    }
    
    private void guestLogin(){
        System.out.println("****Guest Login****");
        System.out.println("Enter username");
        String username = sc.next();
        System.out.println("Enter password");
        String password = sc.next();
        
        try{
            guestSessionBean.guestLogin(username, password);
            System.out.println("Login Successful");
            guestMenu();
        }catch(InvalidLoginCredentialException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void guestMenu(){
        System.out.println("****Welcome to the Hotel Reservation client****");
        while(true){
            System.out.println("(1) Search/Reserve Hotel Room");
            System.out.println("(2) View My Reservation Detail");
            System.out.println("(3) View All Reservations");
            System.out.println("(4) Log Out");
            String response = sc.next();
            switch(response){
                case "1":
                    break;
                    
                case "2":
                    viewReservationDetail();
                    break;
                
                case "3":
                    viewAllReservations();
                    break;
                    
                case "4":
                    guestLogOut();
                    return;
                    
                default:
                    System.out.println("Please enter a valid command");
            }
        }
    }
    
    private void viewReservationDetail(){
        System.out.println("Enter reservation ID");
        Long resId = new Long(sc.nextInt());
        try {
            String details = roomReservationController.retrieveReservationDetails(resId);
            System.out.println(details);
        } catch (ReservationRecordNotFoundException | EntityMismatchException ex) {
            System.out.println(ex.getMessage());
        }
        
        
    }
    
    private void viewAllReservations(){
        System.out.println("Viewing all Reservation Records:");
        ArrayList<ReservationRecordEntity> reservations = roomReservationController.retrieveAllReservation();
        for(ReservationRecordEntity r: reservations){
            System.out.println( "Reservation ID: " + r.getId().toString() + "\n" +
                                "Start Date: " + r.getStartDateAsString() + "\n" +
                                "End Date: " + r.getEndDateAsString() + "\n" +
                                "Room Type Reserved: " + r.getRoomType().getTypeName() + "\n" +
                                "Bill: $" + r.getBill());
            System.out.println();
        }
        System.out.println("****End of Reservation Records****");
        
    }
    
    private void guestLogOut(){
        roomReservationController.guestLogout();
        System.out.println("Logout successful");
    }
}
