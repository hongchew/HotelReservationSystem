/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.GuestEntity;
import entity.ReservationRecordEntity;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;

/**
 *
 * @author Hong Chew
 */
@Stateful
@Local(RoomReservationControllerLocal.class)
@Remote(RoomReservationControllerRemote.class)
public class RoomReservationController implements RoomReservationControllerRemote, RoomReservationControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    private GuestEntity guest;
    
    public RoomReservationController() {
    }
    
    
    @Override
    public void setGuest(Long guestId){
       this.guest = em.find(GuestEntity.class, guestId);
    }
    
    @Override
    public String retrieveReservationDetails(Long resId, Long guestId) throws ReservationRecordNotFoundException, EntityMismatchException{
        ReservationRecordEntity res =  em.find(ReservationRecordEntity.class, resId);
        if(res == null){
            throw new ReservationRecordNotFoundException("Reservation Record not found");
        }else if(!res.getReservedByGuest().getId().equals(guestId)){
            throw new EntityMismatchException("Guest Id provided does not match Guest Id in reservation record");
        }else{
            String details = "Reservation Id: " + res.getId() + "\n" +
                             "Start Date: " + res.getStartDateAsString() + "\n" +
                             "End Date: " + res.getEndDateAsString() + "\n" +
                             "Bill: $" + res.getBill() + "\n";
            
            return details;
        }
        
    }
    
    
    
    
}
