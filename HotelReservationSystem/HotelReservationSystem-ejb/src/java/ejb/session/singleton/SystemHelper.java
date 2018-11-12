/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.AvailabilityRecordEntity;
import entity.ExceptionReportEntity;
import entity.ReservationRecordEntity;
import entity.RoomEntity;
import entity.RoomRankingEntity;
import entity.RoomTypeEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;
import util.exception.NoAvailableRoomException;
import util.exception.NoHigherRankException;

/**
 *
 * @author Hong Chew
 */
@Singleton
@LocalBean
@Startup
public class SystemHelper {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public SystemHelper() {
    }

    //Generate a new avail record for all room type every day for today + 365 day
    @Schedule(hour = "0")
    public void addNewAvailRecordDaily(){
        Query q = em.createQuery("SELECT r FROM RoomTypeEntity r");
        List<RoomTypeEntity> roomTypes = q.getResultList();
        for(RoomTypeEntity r : roomTypes){
            AvailabilityRecordEntity avail = new AvailabilityRecordEntity(addDays(new Date(),365), r);
            r.addNewAvailabilityRecord(avail);
        }
    }
    
    private Date addDays(Date date, int i){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        
        return cal.getTime();     
    }
    
    
    //Recursively allocate rooms to all reservation at 2am every day. 
    @Schedule(hour = "2")
    public void allocateRoomsDaily(){
        List<ReservationRecordEntity> reservationsForToday = getAllReservationToday();
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        for(ReservationRecordEntity r : reservationsForToday){
            RoomTypeEntity type = r.getRoomType();
            ExceptionReportEntity report = new ExceptionReportEntity(today, "", r);
            report = allocateRoom(r, type, report);
            //Save the report if its filled in
            if(!report.getErrorReport().isEmpty()){
                em.persist(report);
            }
        }
    }
    
    private ExceptionReportEntity allocateRoom(ReservationRecordEntity reservation, RoomTypeEntity type, ExceptionReportEntity report){
        try{
            RoomEntity room = getAvailableRoom(type);
            reservationSessionBean.setAssignedRoom(room, reservation);
            return report;
        } catch (NoAvailableRoomException e) {
            try {
                RoomTypeEntity nextType = getNextRank(type);
                String error = "Allocation Exception : Upgraded from " + reservation.getRoomType().getTypeName() + " to " + nextType.getTypeName();
                report.setErrorReport(error);
                return allocateRoom(reservation, nextType, report);
            } catch (NoHigherRankException ex) {
                String error = "Allocation Exception : No rooms available.";
                report.setErrorReport(error);
                return report;
            }
        }
    }
    
    private RoomEntity getAvailableRoom(RoomTypeEntity type) throws NoAvailableRoomException{
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        Query q = em.createQuery("SELECT r FROM RoomEntity r WHERE r.occupancy = :unoccupied AND r.roomType = :type AND r.status = :available");
        q.setParameter("unoccupied", IsOccupiedEnum.UNOCCUPIED);
        q.setParameter("type", type);
        q.setParameter("available", StatusEnum.AVAILABLE);
        List<RoomEntity> list = q.getResultList();
        if(list.isEmpty()){
            throw new NoAvailableRoomException("No room of this type available currently");
        }
        
        return list.get(0);
    }
    
    private List<ReservationRecordEntity> getAllReservationToday(){
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        
        Query q = em.createQuery("SELECT r FROM ReservationRecordEntity r WHERE r.startDate = :today");
        q.setParameter("today", today);
        
        return q.getResultList();
    }
    
    private RoomTypeEntity getNextRank(RoomTypeEntity currentType) throws NoHigherRankException{
        Query query = em.createQuery("SELECT r FROM RoomRankingEntity r WHERE r.name = :name");
        query.setParameter("name", "rankings");
        RoomRankingEntity ranks = (RoomRankingEntity) query.getSingleResult();
        int currentIndex = ranks.getRoomTypeEntities().indexOf(currentType);
        currentIndex--;
        
        try{
            return ranks.getRoomTypeEntities().get(currentIndex);
        }catch(IndexOutOfBoundsException e){
            throw new NoHigherRankException("No Higher Ranks Available.");
        }
    }

}
