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
import entity.RoomEntity;
import java.util.*;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.*;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.validator.constraints.*;
import util.enumeration.IsOccupiedEnum;
import util.exception.EarlyCheckInUnavailableException;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationRecordNotFoundException;
import util.exception.RoomNotAssignedException;
import util.exception.RoomUpgradeException;
import util.exception.UnoccupiedRoomException;
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
    private String email;
    
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
    
    @Override
    public void setGuestEmail(String email){
        this.email = email;
    }
    
    @Override
    public ArrayList<ReservationRecordEntity> retrieveAllReservation(){
        System.err.println("guest id: " + guest.getId());
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
        
        if(guest != null){
            return reservationSessionBean.guestReserveRooms(ticket, guest);
        }else{
            return reservationSessionBean.frontOfficeReserveRooms(ticket, email);
        }
    }
    
    @Override
    public List<ReservationRecordEntity> getReservationListByEmail(String email){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Query q = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.guestEmail = :email AND r.startDate = :date");
        q.setParameter("email", email);
        q.setParameter("date", date);

        List<ReservationRecordEntity> list = q.getResultList();
        for(ReservationRecordEntity r : list){
            r.getRoomType();
            r.getAssignedRoom();
            r.getException();
        }
        return list;
    }
    
    @Override
    public String checkInRoom(Long reservationId) throws EarlyCheckInUnavailableException, RoomNotAssignedException, RoomUpgradeException{

        ReservationRecordEntity reservation = em.find(ReservationRecordEntity.class, reservationId);
        //no room allocated
        if(reservation.getAssignedRoom() == null){
            throw new RoomNotAssignedException("Room was not allocated. Please check Room Allocation Exception Report.");
        }
        
        RoomEntity room = reservation.getAssignedRoom();
        
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.set(Calendar.HOUR, 14);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        
        //currently before 2pm standard check in time
        if(now.before(cal.getTime())){
            if(!checkValidEarlyCheckIn(reservation)){
                throw new EarlyCheckInUnavailableException("Early check in unavailable, please check in after 2pm");
            }
        }

        //Room type reserved != room typed allocataed -> free upgrade
        if(!room.getRoomType().equals(reservation.getRoomType())){
            reservation.setCheckInTime(now);
            room.setOccupancy(IsOccupiedEnum.OCCUPIED);
            throw new RoomUpgradeException("Assigned to Room" + room.getRoomNumber() + "Room upgraded to " + room.getRoomType().getTypeName());
        }
                  
        reservation.setCheckInTime(now);
        room.setOccupancy(IsOccupiedEnum.OCCUPIED);
        
        return "Assigned to Room " + room.getRoomNumber();
    }
    
    private boolean checkValidEarlyCheckIn(ReservationRecordEntity r){
        Date today = r.getStartDate();
        Query q = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.assignedRoom.roomNumber = :roomNumber AND r.endDate = :today");
        q.setParameter("roomNumber", r.getAssignedRoom().getRoomNumber());
        q.setParameter("today", today);
        try{
            q.getSingleResult();
        }catch(NoResultException e){
            //No booking that ended today -> can do early check in
            return true;
        }
        
        //There exist a booking that just ended today -> no early check in
        return false;
    }
    
    @Override
    public String checkOutRoom(String roomNumber) throws UnoccupiedRoomException, ReservationRecordNotFoundException{    
        Query q = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.assignedRoom.roomNumber = :roomNumber");
        q.setParameter("roomNumber", roomNumber);
        
        try{
            ReservationRecordEntity r = (ReservationRecordEntity) q.getSingleResult();
            if(r.getAssignedRoom().getOccupancy().equals(IsOccupiedEnum.UNOCCUPIED)){
                throw new UnoccupiedRoomException("Room not occupied - unable to check out.");
            }
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            r.setCheckOutTime(now);
            r.getAssignedRoom().setOccupancy(IsOccupiedEnum.UNOCCUPIED);
            r.getAssignedRoom().setIsOccupiedTo(null);
            return "Room " + roomNumber +   "checked out successfully";
        }catch(NoResultException | NonUniqueResultException e){
            throw new ReservationRecordNotFoundException("No reservation records for this room is found");
        }
    }
    
    @Override
    public void assignWalkInRoom(ArrayList<ReservationRecordEntity> reservations){
        for(ReservationRecordEntity r : reservations){
            Query q = em.createQuery("SELECT r FROM RoomEntity r WHERE r.occupancy = :notOccupied AND r.roomType = :type");
            List<RoomEntity> rooms = q.getResultList();
            reservationSessionBean.setAssignedRoom(rooms.get(0), r);
        }
    }
}
