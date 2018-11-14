/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.ReservationRecordEntity;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.PartnerEntity;
import java.util.Date;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;
import util.objects.ReservationTicketWrapper;

/**
 *
 * @author Hong Chew
 */
@WebService(serviceName = "ReservationWebService")
@Stateless
public class ReservationWebService {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    /**
     *
     * @param username
     * @param password
     * @return PartnerEntity primary key 
     * @throws InvalidLoginCredentialException
     */
    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException{
        try{
            return partnerSessionBean.partnerLogin(username, password);          
        }catch(InvalidLoginCredentialException e){
            throw e;
        }
    }
    
    /**
     *
     * @param startDate
     * @param endDate
     * @return ReservationTicket with information of available room types
     */
    public ReservationTicketWrapper searchRoom(Date startDate, Date endDate){
        ReservationTicket reservationTicket = reservationSessionBean.searchRooms(startDate, endDate);
        return new ReservationTicketWrapper(reservationTicket);
    }
    
    /**
     *
     * @param ticket
     * @param partnerId
     * @param guestEmail
     * @return ArrayList of ReservationRecordEntity created during reservation
     */
    public ArrayList<ReservationRecordEntity> partnerReserveRooms(ReservationTicketWrapper ticketWrapper, Long partnerId, String guestEmail){
        PartnerEntity partner = partnerSessionBean.retrievePartnerById(partnerId);
        ReservationTicket ticket = reservationSessionBean.unwrapTicketWrapper(ticketWrapper);
        
        return reservationSessionBean.partnerReserveRooms(ticket, partner, guestEmail);
    }
    
    
    /**
     *
     * @param partnerId
     * @return ArrayList of ReservationRecordEntities associated with the partner
     */
    public ArrayList<String> viewAllPartnerReservation(Long partnerId){
        
        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<ReservationRecordEntity> reservations = partnerSessionBean.retrieveAllPartnerReservations(partnerId);
        for(ReservationRecordEntity r : reservations){
            descriptions.add(r.toString());
        }
        return descriptions;
    }
    
    /**
     *
     * @param reservationId
     * @param partnerId
     * @return return string with details of the reservation requested. 
     */
    public String viewReservationDetail(Long reservationId, Long partnerId){
        try {
            return partnerSessionBean.viewReservationDetail(reservationId, partnerId);
        } catch (EntityMismatchException | ReservationRecordNotFoundException ex) {
            return ex.getMessage();
        }
    }
}
