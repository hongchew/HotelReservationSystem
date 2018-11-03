/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.GuestSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.GuestEntity;
import entity.ReservationRecordEntity;
import entity.RoomTypeEntity;
import java.util.*;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;

/**
 *
 * @author Hong Chew
 */
@Stateful
@Local(RoomReservationControllerLocal.class)
@Remote(RoomReservationControllerRemote.class)
public class RoomReservationController implements RoomReservationControllerRemote, RoomReservationControllerLocal {

    @EJB
    private GuestSessionBeanLocal guestSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private GuestEntity guest;
    
    public RoomReservationController() {
    }
    
    @Override
    public void guestLogin(String username, String password) throws InvalidLoginCredentialException {
        guest = guestSessionBean.guestLogin(username, password);
    }
    
    @Override
    public void guestLogout(){
        guest = null;
    }
    
    public void setGuest(Long guestId){
       this.guest = em.find(GuestEntity.class, guestId);
    }
    
    @Override
    public ArrayList<ReservationRecordEntity> retrieveAllReservation(){
        return guest.getReservationRecords();
    }
    
    @Override
    public String retrieveReservationDetails(Long resId) throws ReservationRecordNotFoundException, EntityMismatchException{
        return reservationSessionBean.retrieveReservationDetails(resId, guest.getId());
    }

    @Override
    public ReservationTicket searchRooms(Date startDate, Date endDate){
        return reservationSessionBean.searchRooms(startDate, endDate);
    }
    
    @Override
    public ArrayList<ReservationRecordEntity> reserveRoom(ReservationTicket ticket){
        return reservationSessionBean.guestReserveRooms(ticket, guest);
        
    }
}
