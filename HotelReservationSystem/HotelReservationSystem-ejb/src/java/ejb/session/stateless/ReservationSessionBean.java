/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationRecordEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeUnavailableException;
import util.objects.ReservationTicket;

/**
 *
 * @author Hong Chew
 */
@Stateless
@Local(ReservationSessionBeanLocal.class)
@Remote(ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    
    
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
    
    public ReservationTicket searchRooms(Date startDate, Date endDate){
        ReservationTicket reservationTicket = new ReservationTicket(startDate, endDate);
        Query q = em.createQuery("SELECT r FROM RoomTypeEntity r");
        List<RoomTypeEntity> typeList = q.getResultList();
        for(RoomTypeEntity type : typeList){
            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            
            BigDecimal totalBill = new BigDecimal(0);
            Integer numRoomsRemaining = Integer.MAX_VALUE;
            boolean flag = false;
            for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                try{
                    Integer numAvail = roomSessionBean.getNumberOfRoomsAvailable(type, date);
                    if(numAvail <= 0){ //no room available for 1 day means room type is not available for that search
                        flag = false;
                        break;
                    }else{
                        numRoomsRemaining = Math.min(numAvail, numRoomsRemaining);
                        totalBill = totalBill.add(roomSessionBean.getRatePerNight(type, date));
                        flag = true;
                    }
                }catch(RoomTypeUnavailableException | RoomRateNotFoundException e){
                    flag = false;
                    break;
                }
            }
            if(flag){
                reservationTicket.getAvailableRoomTypes().add(type);
                reservationTicket.getRespectiveNumberOfRoomsRemaining().add(numRoomsRemaining);
                reservationTicket.getRespectiveTotalBill().add(totalBill);
            }
        }
        
        return reservationTicket;
    }
    
}
