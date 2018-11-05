/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import ws.client.reservation.InvalidLoginCredentialException_Exception;
import ws.client.reservation.ReservationTicket;

/**
 *
 * @author Hong Chew
 */
public class MainApp {
    
    public MainApp(){
        
    }
    
    public void runApp(){
        
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
