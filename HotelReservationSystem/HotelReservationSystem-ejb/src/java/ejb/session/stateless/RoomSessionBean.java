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

    public RoomSessionBean() {
    }
    
    @Override
    public void createNewRoomType(Long typeId, String typeName, Integer totalRooms, String description, String bedType, Integer capacity, String amenities, StatusEnum status) {
        RoomTypeEntity newRoomType = new RoomTypeEntity(typeId,  typeName,  totalRooms,  description,  bedType,  capacity,  amenities,status);
        em.persist(newRoomType);    
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public void deleteRoomType(String typeName) throws RoomTypeNotFoundException {
        RoomTypeEntity thisRoomType;
        try{
          thisRoomType = retrieveByTypeName(typeName);
      } catch (RoomTypeNotFoundException e) {
          throw e;
      }
        Query query;
        try {
            query = em.createQuery("SELECT room FROM RoomTypeEntity roomType, IN (roomType.rooms) room WHERE roomType.name = :typename AND room.occupancy = OCCUPIED");
            query.setParameter("typename", typeName);
            if(query.getResultList().isEmpty()) {
                em.remove(thisRoomType); 
            }else {
                thisRoomType.setStatus(StatusEnum.DISABLED);
            }
        } catch (Exception e) {
            System.err.println("our  query is wrong");
        }
        
    }
    
    @Override
    public List<String> viewAllRoomType() {
        Query query = em.createQuery("SELECT roomType FROM RoomTypeEntity roomType");
        return query.getResultList();
    }
    
    @Override
    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException { 
        RoomTypeEntity thisRoomType = retrieveByTypeName(roomType);
        RoomEntity newRoom = new RoomEntity(floor,unit,thisRoomType);
        em.persist(newRoom);
    }
    
    //update the status and roomtype of a room
    @Override
    public void updateRoom(String roomNumber, String roomType, StatusEnum status ) throws RoomNotFoundException, RoomTypeNotFoundException {
        RoomEntity thisRoom = retrieveRoomByRoomNumber(roomNumber);
        RoomTypeEntity thisRoomType = retrieveByTypeName(roomType);
        thisRoom.setRoomType(thisRoomType);
        thisRoom.setStatus(status);  
    }
    
    
    
    
    
    @Override
    public RoomTypeEntity retrieveByTypeName(String typeName) throws RoomTypeNotFoundException {
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
    
    

}
