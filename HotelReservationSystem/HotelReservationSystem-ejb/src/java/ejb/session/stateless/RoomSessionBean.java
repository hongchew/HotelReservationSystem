/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.RoomEntity;
import entity.RoomTypeEntity;
import javax.ejb.Stateless;
import entity.RoomRateEntity;
import entity.RoomRateEntity;
import java.math.BigDecimal;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author saranya
 */

@Stateless
@Local(RoomSessionBeanLocal.class)
@Remote(RoomSessionBeanRemote.class)
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;
    
    
    
    public void createRoomType(Long roomId, String nameOfRoomType, String description, String amenities, String size, int capacityPerRoom, int numOfRooms) {
        
        RoomTypeEntity roomType = new RoomTypeEntity(roomId,numOfRooms, capacityPerRoom,  nameOfRoomType,  description, amenities,  size);
                em.persist(roomType);
    }
    public void updateRoomType(Long roomId, String nameOfRoomType, String description, String amenities, String size, int capacityPerRoom, int numOfRooms) {
        RoomTypeEntity thisRoomType = em.find(RoomTypeEntity.class, roomId);
        thisRoomType.setNameOfRoomType(nameOfRoomType);
        thisRoomType.setDescription(description);
        thisRoomType.setAmenities(amenities);
        thisRoomType.setCapacity(capacityPerRoom);
        thisRoomType.setSize(size);
        thisRoomType.setNumOfRooms(numOfRooms);
        //update the database
        em.merge(thisRoomType);
   
    }
    
    public List<String> viewAllRoomType() {
        Query query = em.createQuery("SELECT DISTINCT rte FROM RoomTypeEntity rte");
        return query.getResultList();
          
    }
    
   public void deleteRoomType(int roomTypeId) {
        RoomTypeEntity thisRoomType = em.find(RoomTypeEntity.class, roomTypeId);
        em.remove(thisRoomType);
   }
   
  
   public void createRoom(Long roomId, int roomNum, boolean isOccupied, boolean isUsable, RoomTypeEntity roomType) {
        RoomEntity room = new RoomEntity(roomId, roomNum, isOccupied, isUsable, roomType);
        em.persist(room);
   }
    
    public void updateRoom(Long roomId, int roomNum, boolean isOccupied, boolean isUsable, RoomTypeEntity roomType) {
        RoomEntity thisRoom = em.find(RoomEntity.class, roomId);
        thisRoom.setIsOccupied(isOccupied);
        thisRoom.setRoomNum(roomNum);
        thisRoom.setIsUsable(isUsable);
        thisRoom.setRoomType(roomType);
        em.merge(thisRoom);
        
    }
    
    public void deleteRoom(int roomId) {
        RoomEntity thisRoom = em.find(RoomEntity.class, roomId);
        em.remove(thisRoom);
    }
    
    public List<String> viewAllRooms() {
        Query query = em.createQuery("SELECT DISTINCT rm FROM RoomEntity rm");
        return query.getResultList();
    }
    
    public void createNewRoomRate(Long roomRateId, String nameOfRate, ArrayList<RoomTypeEntity> roomType, BigDecimal ratePerNight, Date start, Date end) {
        RoomRateEntity newRoomRate = new RoomRateEntity(roomRateId, nameOfRate, roomType, ratePerNight, start,end);
        em.persist(newRoomRate);
    }
    
    public void updateRoomRate(Long roomRateId, String nameOfRate, ArrayList<RoomTypeEntity> roomType, BigDecimal ratePerNight, Date start, Date end) {
        RoomRateEntity thisRoomRate = em.find(RoomRateEntity.class, roomRateId);
        thisRoomRate.setNameOfRate(nameOfRate);
        thisRoomRate.setRoomType(roomType);
        thisRoomRate.setRatePerNight(ratePerNight);
        thisRoomRate.setStart(start);
        thisRoomRate.setEnd(end);
        em.merge(thisRoomRate);
    }
    
    public void deleteRoomRate(int roomRateId) {
        RoomRateEntity thisRoomRate = em.find(RoomRateEntity.class, roomRateId);
        em.remove(thisRoomRate);
        
    }
    
    public List<String> viewAllRoomRates() {
        Query query = em.createQuery("SELECT DISTINCT rr FROM RoomRateEntity rr");
        return query.getResultList();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
