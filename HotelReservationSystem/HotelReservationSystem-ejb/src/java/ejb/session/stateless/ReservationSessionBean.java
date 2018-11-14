/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AvailabilityRecordEntity;
import entity.GuestEntity;
import entity.PartnerEntity;
import entity.ReservationRecordEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;
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
    
    @Override
    public ReservationTicket searchRooms(Date startDate, Date endDate){
        ReservationTicket reservationTicket = new ReservationTicket(startDate, endDate);
        Query q = em.createQuery("SELECT r FROM RoomTypeEntity r WHERE r.status = :available");
        q.setParameter("available", StatusEnum.AVAILABLE);
        List<RoomTypeEntity> typeList = q.getResultList();
        for(RoomTypeEntity type : typeList){
            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            
            BigDecimal totalBill = new BigDecimal(0);
            Integer numRoomsRemaining = Integer.MAX_VALUE;
            boolean flag = false;
            for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                try{
                    Integer numAvail = roomSessionBean.getNumberOfRoomsAvailable(type, date);
                    if(numAvail <= 0){ //no room available for 1 day means room type is not available for that search
                        System.err.println("No room available");
                        flag = false;
                        break;
                    }else{
                        numRoomsRemaining = Math.min(numAvail, numRoomsRemaining);
                        totalBill = totalBill.add(roomSessionBean.getRatePerNight(type, date));
                        flag = true;
                    }
                }catch(RoomTypeUnavailableException | RoomRateNotFoundException e){
                    System.err.println(e.getMessage());
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
     
    @Override
    public ArrayList<ReservationRecordEntity> guestReserveRooms(ReservationTicket ticket, GuestEntity guest){
        ArrayList<ReservationRecordEntity> reservations = new ArrayList<>();
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){ //for each room types
            for(int j = 0; j < ticket.getRespectiveNumberReserved().get(i); j++){ //for each room booked
                ReservationRecordEntity record = new ReservationRecordEntity(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate(), guest);
                em.persist(record);
                record.setBill(ticket.getRespectiveTotalBill().get(i));
                guest.getReservationRecords().add(record);
                reservations.add(record);
                updateAvailabilityRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate());
                System.err.println("i = " + i + " j = " + j + "reserved");
            }
        }
        return reservations;
    }
    
    
    @Override
    public ArrayList<ReservationRecordEntity> frontOfficeReserveRooms(ReservationTicket ticket, String guestEmail){
        ArrayList<ReservationRecordEntity> reservations = new ArrayList<>();
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){ //for each room types
            for(int j = 0; j < ticket.getRespectiveNumberReserved().get(i); j++){ //for each room booked
                ReservationRecordEntity record = new ReservationRecordEntity(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate(), guestEmail);
                em.persist(record);
                record.setBill(ticket.getRespectiveTotalBill().get(i));
                reservations.add(record);
                updateAvailabilityRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate());
            }
        }
        return reservations;
    }

    @Override
    public ArrayList<ReservationRecordEntity> partnerReserveRooms(ReservationTicket ticket, PartnerEntity partner, String guestEmail){
        ArrayList<ReservationRecordEntity> reservations = new ArrayList<>();
        for(int i = 0; i < ticket.getAvailableRoomTypes().size(); i++){ //for each room types
            for(int j = 0; j < ticket.getRespectiveNumberReserved().get(i); j++){ //for each room booked
                ReservationRecordEntity record = new ReservationRecordEntity(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate(), guestEmail, partner);
                em.persist(record);
                record.setBill(ticket.getRespectiveTotalBill().get(i));
                partner.getReservationRecords().add(record);
                reservations.add(record);
                updateAvailabilityRecord(ticket.getAvailableRoomTypes().get(i), ticket.getStartDate(), ticket.getEndDate());
            }
        }
        return reservations;
    } 
    
    private void updateAvailabilityRecord(RoomTypeEntity type, Date startDate, Date endDate){
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()){
            Query q = em.createQuery("SELECT a FROM AvailabilityRecordEntity a WHERE a.availabiltyRecordDate = :date AND a.roomType = :type");
            q.setParameter("date", date);
            q.setParameter("type", type);
            AvailabilityRecordEntity avail = (AvailabilityRecordEntity) q.getSingleResult();
            avail.addOneReservation();
        }
        
    }
    
    @Override
    public void setAssignedRoom(RoomEntity room, ReservationRecordEntity res){
        
        res.setAssignedRoom(room);
        room.setOccupancy(IsOccupiedEnum.OCCUPIED);
        room.setIsOccupiedTo(res.getEndDate());
    }
    
    /*@Override
    public ReservationTicket frontOfficeSearchRooms(Date startDate, Date endDate){
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
                        totalBill = totalBill.add(roomSessionBean.getPublishedRatePerNight(type, date));
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
    }*/
    

    /*@Schedule(hour = "2")
    private void allocateRoomsDaily(){
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
            setAssignedRoom(room, reservation);
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
        Query q = em.createQuery("SELECT r FROM RoomEntity r WHERE r.occupancy = :unoccupied AND r.roomType = :type");
        q.setParameter("unoccupied", IsOccupiedEnum.UNOCCUPIED);
        q.setParameter("type", type);
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
    }*/
    

}
