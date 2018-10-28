/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.*;
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
    
    
    private EntityManager em;
    
    public void createNewRoomType(Long typeId, String typeName, Integer totalRooms, String description, String bedType, Integer capacity, String amenities, StatusEnum status) {
        RoomTypeEntity newRoomType = new RoomTypeEntity(typeId,  typeName,  totalRooms,  description,  bedType,  capacity,  amenities,status);
        em.persist(newRoomType);    
    }
    
    public String viewRoomTypeDetails(String typeName) {
        RoomTypeEntity roomType;
        try{
          roomType = retrieveByTypeName(typeName);
      } catch (RoomTypeNotFoundException e) {
          System.err.println("No such roomType found");
          return null;
      }
        
        String details = ("Type Name: " + roomType.getTypeName() +
                          "Amenities: " + roomType.getAmenities() + 
                          "Bed Type: " + roomType.getBedType() +
                          "Description: " + roomType.getDescription() +
                          "Capacity: " + roomType.getCapacity() +
                          "Status: " + roomType.getStatus() +
                          "Total Rooms: " + roomType.getTotalRooms());
        
        return details;
    }
    
    public void updateRoomType(String typeName, String newName, String newDescription, String newBedType, Integer newCapacity, String newAmenities,StatusEnum newStatus, Integer newTotalRooms) {
      RoomTypeEntity thisRoomType;
        try{
          thisRoomType = retrieveByTypeName(typeName);
      } catch (RoomTypeNotFoundException e) {
          System.err.println("No such roomType found");
          return;
      }
      thisRoomType.setAmenities(newAmenities);
      thisRoomType.setDescription(newDescription);
      thisRoomType.setTypeName(newName);
      thisRoomType.setBedType(newBedType);
      thisRoomType.setCapacity(newCapacity);
      thisRoomType.setAmenities(newAmenities);
      thisRoomType.setStatus(newStatus);
      thisRoomType.setTotalRooms(newTotalRooms);
    }
    
    public void deleteRoomType(String typeName) throws RoomTypeNotFoundException {
        RoomTypeEntity thisRoomType;
        try{
          thisRoomType = retrieveByTypeName(typeName);
      } catch (RoomTypeNotFoundException e) {
          throw e;
      }
        Query query = null;
        try {
            query = em.createQuery(" SELECT room FROM RoomTypeEntity type IN (type.rooms) room WHERE type.name = :typename AND room.occupancy = OCCUPIED");
        } catch (Exception e) {
            System.err.println("our damn query is wrong");
        }
        if(query.getResultList().isEmpty()) {
            em.remove(thisRoomType); 
        }
        else {
            thisRoomType.setStatus(StatusEnum.DISABLED);
        }
    }
    
    public List<String> viewAllRoomType() {
        Query query = em.createQuery("SELECT roomType FROM RoomTypeEntity roomTypeEntity");
        return query.getResultList();
    }
    
    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException { 
        RoomTypeEntity thisRoomType = retrieveByTypeName(roomType);
        RoomEntity newRoom = new RoomEntity(floor,unit,thisRoomType);
        em.persist(newRoom);
    }
    
    //update the status and roomtype of a room
    public void updateRoom(String roomNumber, String roomType, StatusEnum status ) throws RoomNotFoundException, RoomTypeNotFoundException {
        RoomEntity thisRoom = retrieveRoomByRoomNumber(roomNumber);
        RoomTypeEntity thisRoomType = retrieveByTypeName(roomType);
        thisRoom.setRoomType(thisRoomType);
        thisRoom.setStatus(status);  
    }
    
    
    
    
    
    public RoomTypeEntity retrieveByTypeName(String typeName) throws RoomTypeNotFoundException {
        Query q = em.createQuery("SELECT r FROM RoomTypeEntity r WHERE r.typeName = :typename");
        q.setParameter("typename", typeName);
        try{ 
            return (RoomTypeEntity) q.getSingleResult();
        }catch(NoResultException | NonUniqueResultException e) {  
            throw new RoomTypeNotFoundException("No such room type found");
        }
    }
    
    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomNumber = :roomnumber");
            try{
               return (RoomEntity) query.getSingleResult();
            } catch (NoResultException | NonUniqueResultException e) {
                  throw new RoomNotFoundException("No such room found");
                }        
    }
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
