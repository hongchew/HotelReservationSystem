/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateful.RoomReservationControllerRemote;
import entity.ReservationRecordEntity;
import entity.RoomTypeEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;

/**
 *
 * @author Hong Chew
 */
public class MainApp {
    
    private GuestSessionBeanRemote guestSessionBean;
    private RoomReservationControllerRemote roomReservationController;
    private boolean loggedIn;
    private Scanner sc = new Scanner(System.in);
    
    public MainApp() {
    }

    public MainApp(GuestSessionBeanRemote guestSessionBean, RoomReservationControllerRemote roomReservationController) {
        this.guestSessionBean = guestSessionBean;
        this.roomReservationController = roomReservationController;
    }
    
    public void runApp(){
        
        while(true){
            System.out.println("****Welcome to the Hotel Reservation client****");
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
                    searchHotelRoom();
                    break;
                case "4":
                    System.out.println("\n****Exiting system****");
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
        sc.nextLine();
        String name = sc.nextLine();
        System.out.println("Enter username");
        String username = sc.next();
        System.out.println("Enter password");
        String password = sc.next();
        System.out.println("Enter email address");
        String email = sc.next();
        
        guestSessionBean.registerNewGuest(name, username, password, email);
        System.out.println("\n****New Guest Created!****\n");
    }
    
    private void guestLogin(){
        System.out.println("\n****Guest Login****");
        System.out.println("Enter username");
        String username = sc.next();
        System.out.println("Enter password");
        String password = sc.next();
        
        try{
            roomReservationController.guestLogin(username, password);
            System.out.println("\nLogin Successful");
            loggedIn = true;
            guestMenu();
        }catch(InvalidLoginCredentialException e){
            System.err.println("\n" + e.getMessage());
        }
    }
    
    private void guestMenu(){
        
        while(true){
            System.out.println("\n****Welcome to the Hotel Reservation client****");
            System.out.println("(1) Search/Reserve Hotel Room");
            System.out.println("(2) View My Reservation Detail");
            System.out.println("(3) View All Reservations");
            System.out.println("(4) Log Out");
            String response = sc.next();
            switch(response){
                case "1":
                    searchHotelRoom();
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
                    System.err.println("Please enter a valid command");
            }
        }
    }
    
    private ReservationTicket searchHotelRoom(){
        
        try {
            System.out.println("\n****Search Hotel Room****");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Kindly note that our hotel only accepts reservations a maximum of 1 year in advance.");
            System.out.println("Enter check in date (dd/mm/yyyy):");
            Date startDate = df.parse(sc.next());
            System.out.println("Enter check out date (dd/mm/yyyy):");
            Date endDate = df.parse(sc.next());
            
            if(startDate.after(endDate) || startDate.equals(endDate)){
                System.err.println("\nCheck in date must be after checkout date.\n");
                return null;
            }
            
            ReservationTicket ticket = roomReservationController.searchRooms(startDate, endDate, false);
            if(ticket.getAvailableRoomTypes().isEmpty()){
                System.err.println("\nThere are no available rooms for your desired check in and check out date.\n");
                return null;
            }
            System.out.println("\n****Available Rooms****");
            for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
                RoomTypeEntity type = ticket.getAvailableRoomTypes().get(i);
                System.out.println("(" + i + ")" + type.getTypeName());
                System.out.println(type.getDescription());
                System.out.println("Amenities: " + type.getAmenities());
                System.out.println("Capacity: " + type.getCapacity());
                System.out.println("Rooms Available: " + ticket.getRespectiveNumberOfRoomsRemaining().get(i) );
                System.out.println("Cost: " + ticket.getRespectiveTotalBill().get(i));
                System.out.println();
            }
            System.out.println("\n****End of list****\n");
            while(loggedIn){
                System.out.println("Reserve a room? (Y/N)");
                String resp = sc.next();
                switch(resp){
                    case "Y":
                        reserveHotelRoom(ticket);
                        return ticket;
                    case "N":
                        return null;
                        
                    default:
                        System.out.println("Please choose a valid option");
                        break;
                }
            }
            return ticket;
        } catch (ParseException ex) {
            System.err.println("\nPlease enter valid date format\n");
            return null;
        }
    }
    
    private void reserveHotelRoom(ReservationTicket ticket){
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
            RoomTypeEntity type = ticket.getAvailableRoomTypes().get(i);
            System.out.println("Enter number of " + type.getTypeName() + " to reserve:");
            int num = sc.nextInt();
            if(num < 0 || num > ticket.getRespectiveNumberOfRoomsRemaining().get(i)){
                ticket.getRespectiveNumberReserved().add(0);
                System.out.println("****Invalid Number, 0 rooms of this type will be reserved****\n");
            }else{
                ticket.getRespectiveNumberReserved().add(num);
                System.out.println("****" + num + " of " + type.getTypeName() + " added to cart****\n");
            }
        }
        
        roomReservationController.reserveRoom(ticket);
        System.out.println("Reservation Successful.");
    }
    
    private void viewReservationDetail(){
        System.out.println("Enter reservation ID");
        Long resId = new Long(sc.nextInt());
        try {
            System.out.println("\n****View Reservation Detail****");
            String details = roomReservationController.retrieveReservationDetails(resId);
            System.out.println(details);
        } catch (ReservationRecordNotFoundException | EntityMismatchException ex) {
            System.err.println(ex.getMessage());
        }
        
        
    }
    
    private void viewAllReservations(){
        System.out.println("\n****Viewing all Reservation Records****");
        ArrayList<ReservationRecordEntity> reservations = roomReservationController.retrieveAllReservation();
        if(reservations.isEmpty()){
            System.err.println("\nNo reservation records available.\n");
            return;
        }
        
        for(ReservationRecordEntity r: reservations){
            System.out.println( "Reservation ID: " + r.getId().toString() + "\n" +
                                "Start Date: " + r.getStartDateAsString() + "\n" +
                                "End Date: " + r.getEndDateAsString() + "\n" +
                                "Room Type Reserved: " + r.getRoomType().getTypeName() + "\n" +
                                "Bill: $" + r.getBill());
            System.out.println();
        }
        System.out.println("****End of Reservation Records****\n");
        
    }
    
    private void guestLogOut(){
        roomReservationController.guestLogout();
        loggedIn = false;
        System.out.println("\n****Logout successful****\n");
    }
}
