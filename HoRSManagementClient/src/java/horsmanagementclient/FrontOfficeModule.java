/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateful.RoomReservationControllerRemote;
import entity.ReservationRecordEntity;
import entity.RoomTypeEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolationException;
import util.exception.EarlyCheckInUnavailableException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.RoomNotAssignedException;
import util.exception.RoomUpgradeException;
import util.exception.UnoccupiedRoomException;
import util.objects.ReservationTicket;

/**
 *
 * @author Hong Chew
 */
public class FrontOfficeModule {
    
    private RoomReservationControllerRemote roomReservationController;
    private Scanner sc = new Scanner(System.in);
    public FrontOfficeModule() {
    }

    public FrontOfficeModule(RoomReservationControllerRemote roomReservationController) {
        this.roomReservationController = roomReservationController;
    }
    
    public void runFrontOfficeModule(){
        
        while(true){
            System.out.println("\n****Welcome to the front office module****");
            System.out.println("(1)Search/Reserve Room \n" +
                            "(2)Check-in Guest \n" +
                            "(3)Check-out Guest \n" +
                            "(4)Return");
            switch(sc.next()){
                case "1":
                    searchRoom();
                    break;
                case "2":
                    checkIn();
                    break;
                case "3":
                    checkOut();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Please select a valid input");
                    break;
            }
        }
    }
    
    private ReservationTicket searchRoom(){
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            Date startDate = cal.getTime();
            System.out.println("Enter check out date (dd/mm/yyyy):");
            Date endDate = df.parse(sc.next());
            
            if(startDate.after(endDate) || startDate.equals(endDate)){
                System.err.println("\nCheck in date must be after checkout date.\n");
                return null;
            }
            
            ReservationTicket ticket = roomReservationController.searchRooms(startDate, endDate);
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
                while(true){
                    System.out.println("Reserve a room? (Y/N)");
                    String resp = sc.next();
                    switch(resp){
                        case "Y":
                            reserveHotelRoom(ticket);
                            return ticket;
                        case "N":
                            return ticket;
                        default:
                            System.out.println("Please choose a valid option");
                            break;
                    }
                }
            
        } catch (ParseException ex) {
            System.err.println("Please enter valid date format");
            return null;
        }
    }
        
    private void reserveHotelRoom(ReservationTicket ticket) {
        System.out.println("Enter guest email address");
        String email = sc.next();
        try{
            roomReservationController.setGuestEmail(email);
        }catch(ConstraintViolationException e){
            System.err.println("Invalid email address");
            return;
        }
        
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
            RoomTypeEntity type = ticket.getAvailableRoomTypes().get(i);
            System.out.println("Enter number of " + type.getTypeName() + " to reserve:");
            int num = sc.nextInt();
            if(num < 0 || num > ticket.getRespectiveNumberOfRoomsRemaining().get(i)){
                ticket.getRespectiveNumberReserved().add(0);
                System.out.println("\nInvalid Number, 0 rooms of this type will be reserved\n");
            }else{
                ticket.getRespectiveNumberReserved().add(num);
                System.out.println(num + " of " + type.getTypeName() + " added to cart\n");
            }
        }
        
        ArrayList<ReservationRecordEntity> reservations = roomReservationController.reserveRoom(ticket);
        System.out.println("Reservation Successful.");
        roomReservationController.assignWalkInRoom(reservations);
        for(ReservationRecordEntity r : reservations){
            checkInRoom(r.getId());
        }
    }
    
    
    
    private void checkIn(){
        
        try{
            System.out.println("Enter Email");
            String email = sc.next();
        
            List<ReservationRecordEntity> reservations = roomReservationController.getReservationListByEmail(email);
            if(reservations.isEmpty()){
                System.out.println("No reservations made for this guest today");
            }else{
                System.out.println("****Checking In****");
                System.out.println("===================");
                for(ReservationRecordEntity r : reservations){
                    System.out.println("Reservation ID: " + r.getId() + "\n" +
                                        r.getRoomType().getTypeName());
                    checkInRoom(r.getId());
                    System.out.println("===================");
                }
                System.out.println("****End of Check In****");
            }
        }catch(ConstraintViolationException e){
            System.err.println("Invalid email entered.");
        } 
    }
    
    private void checkInRoom(Long reservationId) {
        try{
            System.out.println(roomReservationController.checkInRoom(reservationId));
        } catch (RoomNotAssignedException | RoomUpgradeException | EarlyCheckInUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
    }  
    
    private void checkOut(){
        System.out.println("Enter room number");
        String roomNumber = sc.next();
        try{
            System.out.println(roomReservationController.checkOutRoom(roomNumber));
        }catch(ReservationRecordNotFoundException | UnoccupiedRoomException e){
            System.out.println(e.getMessage());
        }
    }


}
