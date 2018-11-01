/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AvailabilityRecordEntity;
import entity.RoomEntity;
import entity.RoomRankingEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.persistence.EntityManager;
import javax.persistence.*;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author saranya
 */
@Stateless
@Local(RoomSessionBeanLocal.class)
@Remote(RoomSessionBeanRemote.class)
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Resource
    private EJBContext eJBContext;
    

    public RoomSessionBean() {
    }
    
    @Override
    public Long createNewRoomType(String typeName, String description, String bedType, Integer capacity, String amenities, int i) {
        RoomTypeEntity newRoomType = returnNewRoomTypeEntity(typeName, description, bedType, capacity, amenities, i);
        
        return newRoomType.getTypeId();
    }
    
    public Date addDays(Date date, int i){
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        
        return cal.getTime();     
    }
    
    @Override
    public RoomTypeEntity returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i) {
        RoomTypeEntity newRoomType = new RoomTypeEntity(typeName, description, bedType, capacity, amenities);
        em.persist(newRoomType);
            
        insertRoomRank(newRoomType, i);
        
        Date date;
        for(int j = 0; j <= 365; j++){ //create next 365 days of availability record in advance.
            date = addDays(new Date(), j);
            AvailabilityRecordEntity avail = new AvailabilityRecordEntity(date, newRoomType);
            em.persist(avail);
            newRoomType.addNewAvailabilityRecord(avail);
        }
        
        return newRoomType;
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
    
    @Override
    public String viewRoomTypeDetails(String typeName) {
        RoomTypeEntity roomType;
        try{
            roomType = retrieveRoomTypeByTypeName(typeName);
        }catch (RoomTypeNotFoundException e) {
            System.err.println("No such roomType found");
            return null;
        }
        
        String details = ("Type Name: " + roomType.getTypeName() +
                          "\nDescription: " + roomType.getDescription() +
                          "\nAmenities: " + roomType.getAmenities() + 
                          "\nBed Type: " + roomType.getBedType() +
                          "\nCapacity: " + roomType.getCapacity() +
                          "\nStatus: " + roomType.getStatus() +
                          "\nTotal Rooms: " + roomType.getTotalRooms()) + "\n";
        
        return details;
    }
    
    @Override
    public void updateRoomType(String typeName, String newDescription, String newBedType, Integer newCapacity, String newAmenities) throws RoomTypeNotFoundException {

        try{
            RoomTypeEntity thisRoomType = retrieveRoomTypeByTypeName(typeName);
            thisRoomType.setAmenities(newAmenities);
            thisRoomType.setDescription(newDescription);
            thisRoomType.setBedType(newBedType);
            thisRoomType.setCapacity(newCapacity);
        }catch (RoomTypeNotFoundException e) {
            eJBContext.setRollbackOnly();
            throw e;
        }

    }
    
    @Override
    public void deleteRoomType(String typeName) throws RoomTypeNotFoundException {
        RoomTypeEntity thisRoomType;
        try{
            thisRoomType = retrieveRoomTypeByTypeName(typeName);
      } catch (RoomTypeNotFoundException e) {
            throw e;
      }
        
        try {
            //get a list of rooms of type typeName that is occupied 
            Query query = em.createQuery("SELECT room FROM RoomTypeEntity roomType, IN (roomType.rooms) room WHERE roomType.typeName = :typename AND room.occupancy = :occupancy ");
            query.setParameter("typename", typeName);
            query.setParameter("occupancy", IsOccupiedEnum.OCCUPIED);
            if(query.getResultList().isEmpty()) { //No rooms of the type are occupied
                em.remove(thisRoomType); 
            }else { //Some room of the type are occupied
                thisRoomType.setStatus(StatusEnum.DISABLED);
            }
        } catch (Exception e) {
            System.err.println("our query is wrong");
        }
    }
    
    @Override
    public List<RoomTypeEntity> retrieveListOfRoomTypes() {
        Query query = em.createQuery("SELECT roomType FROM RoomTypeEntity roomType");
        return query.getResultList();
    }
    
    @Override
    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException { 
        try {
            RoomTypeEntity thisRoomType = retrieveRoomTypeByTypeName(roomType);
            RoomEntity newRoom = new RoomEntity(floor, unit, thisRoomType);
            em.persist(newRoom);
            thisRoomType.addRoom(newRoom);
        } catch (RoomTypeNotFoundException roomTypeNotFoundException) {
            eJBContext.setRollbackOnly();
            throw roomTypeNotFoundException;
        }
    }
    
    //update the status and roomtype of a room
    @Override
    public void updateRoom(String roomNumber, String roomType, StatusEnum status ) throws RoomNotFoundException, RoomTypeNotFoundException {
        try {
            RoomEntity thisRoom = retrieveRoomByRoomNumber(roomNumber);
            RoomTypeEntity oldRoomType = thisRoom.getRoomType();
            RoomTypeEntity newRoomType = retrieveRoomTypeByTypeName(roomType);
            oldRoomType.removeRoom(thisRoom);
            newRoomType.addRoom(thisRoom);
            thisRoom.setRoomType(newRoomType);
            thisRoom.setStatus(status);            
        } catch (RoomNotFoundException | RoomTypeNotFoundException e) {
            eJBContext.setRollbackOnly();
            System.out.println(e.getMessage());
        }
    }
    
    
    
    
    
    @Override
    public RoomTypeEntity retrieveRoomTypeByTypeName(String typeName) throws RoomTypeNotFoundException {
        Query q = em.createQuery("SELECT r FROM RoomTypeEntity r WHERE r.typeName = :typename");
        q.setParameter("typename", typeName);
        try{ 
            return (RoomTypeEntity) q.getSingleResult();
        }catch(NoResultException | NonUniqueResultException e) {  
            throw new RoomTypeNotFoundException("No such room type found");
        }
    }
    
    @Override
    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomNumber = :roomnumber");
            try{
               return (RoomEntity) query.getSingleResult();
            } catch (NoResultException | NonUniqueResultException e) {
                  throw new RoomNotFoundException("No such room found");
                }        
    }

    @Override
    public List<RoomEntity> retrieveAllRooms(){
        Query q = em.createQuery("SELECT r FROM RoomEntity r ORDER BY r.roomNumber ASC");
        return q.getResultList();
    }
    
    
    @Override
    public String viewRoomDetails(RoomEntity room){
        return "Room Number " + room.getRoomNumber() + ", " + room.getRoomType() + ", " + room.getStatus();
    }
    
    @Override
    public ArrayList<RoomTypeEntity> getRoomRanks(){
        RoomRankingEntity ranks = em.find(RoomRankingEntity.class, new Long(1));
        ranks.getRankings().size();
        return ranks.getRankings();
    }
    
    
    public void insertRoomRank(RoomTypeEntity roomType, int index){
        RoomRankingEntity ranks = em.find(RoomRankingEntity.class, new Long(1));
        ranks.getRankings().add(index, roomType);
    }
}
