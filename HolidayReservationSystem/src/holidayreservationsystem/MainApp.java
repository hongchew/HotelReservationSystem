/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.reservation.InvalidLoginCredentialException_Exception;
import ws.client.reservation.ReservationRecordEntity;
import ws.client.reservation.ReservationTicket;
import ws.client.reservation.RoomTypeEntity;

/**
 *
 * @author Hong Chew
 */
public class MainApp {
    private final Scanner sc = new Scanner(System.in);
    private long loggedInId;
    private DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
    private ReservationTicket ticket;
    
    public MainApp(){
        
    }
    
    public void runApp(){
        System.out.println("****Welcome to Holiday.com****");

        while(true){
            System.out.println("(1)Log In\n"
                        + "(2)Search Rooms \n"
                        + "(*)Exit\n");
            String resp = sc.next();
            switch(resp){
                case "1":
                    login();
                    break;
                case "2":
                    search();
                    break;
                default:
                    System.out.println("Exiting system");
                    System.exit(0);
            }
        }       
    }

    private void login() {
        try {
            System.out.println("Enter username: ");
            String username = sc.next();
            System.out.println("Enter password: ");
            String password = sc.next();
            loggedInId = partnerLogin(username, password);
            System.out.println("\nLogin successful\n!");
            loggedInMenu();
        } catch (InvalidLoginCredentialException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void search() {
        try {
            System.out.println("***Search Hotel Room****");
            System.out.println("Enter desired check in date (dd/mm/yyyy)");
            String dateString = sc.next();
            Date startDate = df.parse(dateString);
            System.out.println("Enter desired check out date (dd/mm/yyyy)");
            dateString = sc.next();
            Date endDate = df.parse(dateString);
            
            
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(startDate);
            XMLGregorianCalendar startGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            gc.setTime(endDate);
            XMLGregorianCalendar endGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            ticket = searchRoom(startGC, endGC);
            if(ticket.getAvailableRoomTypes().isEmpty()){
                System.err.println("There are no available rooms for your desired check in and check out date.");
                
            }else{
                System.out.println("****Available Rooms****");
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
                if(loggedInId > 0L){
                    System.out.println("Reserve rooms? (Y/N)");
                    String response  = sc.next();
                    if(response.equalsIgnoreCase("Y")){
                        reserve();
                    }else{
                        System.out.println("No rooms reserved");
                    }
                }
            }              
        } catch (ParseException ex) {
            System.err.println("Please enter valid a valid date format (dd/mm/yyyy)");
        } catch (DatatypeConfigurationException ex) {
            System.err.println(ex.getMessage());
        }
    }

     private void loggedInMenu() {
        System.out.println("****Welcome to Holiday.com ****");

        while(true){
            System.out.println("(1)Search Rooms"
                        + "(2)Reserve Room"
                        + "(3)View Reservation Detail"
                        + "(4)View All Reservations"
                        + "(*)Exit");
            String resp = sc.next();
            switch(resp){
                case "1":
                    search();
                    break;
                case "2":
                    reserve();
                    break;
                case "3":
                    viewDetail();
                    break;
                case "4":
                    viewAll();
                    break;
                default:
                    System.out.println("Exiting system");
                    System.exit(0);
            }
        }
    }

    private void reserve() {
        System.out.println("Enter email address: ");
        String email = sc.next();
        
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){
            RoomTypeEntity type = ticket.getAvailableRoomTypes().get(i);
            System.out.println("Enter number of " + type.getTypeName() + " to reserve:");
            int num = sc.nextInt();
            if(num < 0 || num > ticket.getRespectiveNumberOfRoomsRemaining().get(i)){
                System.out.println("Invalid Number, 0 rooms of this type will be reserved");
            }else{
                System.out.println(num + " of " + type.getTypeName() + " added to cart");
            }
        }
        List<ReservationRecordEntity> reserved = partnerReserveRooms(ticket, loggedInId, email);
        
        System.out.println("Reservation Successful.");        
    }

    private void viewDetail() {
        System.out.println("****View Reservation Detail****");
        System.out.println("Enter reservation ID: ");
        Long resId = sc.nextLong();
        System.out.println(viewReservationDetail(resId, loggedInId));
    }

    private void viewAll() {
        List<ReservationRecordEntity> reservations = viewAllPartnerReservation(loggedInId);
        System.out.println("\n****Reservation List****");
        for(ReservationRecordEntity r :reservations){
            System.out.println("Res Id:" + r.getId() + "\n Reserved by Guest Email:" + r.getGuestEmail());
        }
        System.out.println("****End List****\n");
    }

    
    private static Long partnerLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.reservation.ReservationWebService_Service service = new ws.client.reservation.ReservationWebService_Service();
        ws.client.reservation.ReservationWebService port = service.getReservationWebServicePort();
        return port.partnerLogin(arg0, arg1);
    }

    private static java.util.List<ws.client.reservation.ReservationRecordEntity> partnerReserveRooms(ws.client.reservation.ReservationTicket arg0, java.lang.Long arg1, java.lang.String arg2) {
        ws.client.reservation.ReservationWebService_Service service = new ws.client.reservation.ReservationWebService_Service();
        ws.client.reservation.ReservationWebService port = service.getReservationWebServicePort();
        return port.partnerReserveRooms(arg0, arg1, arg2);
    }

    private static ReservationTicket searchRoom(javax.xml.datatype.XMLGregorianCalendar arg0, javax.xml.datatype.XMLGregorianCalendar arg1) {
        ws.client.reservation.ReservationWebService_Service service = new ws.client.reservation.ReservationWebService_Service();
        ws.client.reservation.ReservationWebService port = service.getReservationWebServicePort();
        return port.searchRoom(arg0, arg1);
    }

    private static java.util.List<ws.client.reservation.ReservationRecordEntity> viewAllPartnerReservation(java.lang.Long arg0) {
        ws.client.reservation.ReservationWebService_Service service = new ws.client.reservation.ReservationWebService_Service();
        ws.client.reservation.ReservationWebService port = service.getReservationWebServicePort();
        return port.viewAllPartnerReservation(arg0);
    }

    private static String viewReservationDetail(java.lang.Long arg0, java.lang.Long arg1) {
        ws.client.reservation.ReservationWebService_Service service = new ws.client.reservation.ReservationWebService_Service();
        ws.client.reservation.ReservationWebService port = service.getReservationWebServicePort();
        return port.viewReservationDetail(arg0, arg1);
    }
    
}
