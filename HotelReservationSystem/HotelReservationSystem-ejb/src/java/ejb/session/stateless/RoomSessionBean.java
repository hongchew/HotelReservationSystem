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
    
    public String viewRoomTypeDetails(RoomTypeEntity roomType) {
        
    }
    
    public void createRoom(Long roomId, int roomNum, boolean isOccupied, boolean isUsable, RoomTypeEntity roomType) {
        RoomEntity room = new RoomEntity(roomId, roomNum, isOccupied, isUsable, roomType);
        em.persist(room);
    }
    
    public void 

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
