/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.StatusEnum;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author saranya
 */
@Remote
public interface RoomSessionBeanRemote {

    public String viewRoomTypeDetails(String typeName);

    public void updateRoomType(String typeName, String newDescription, String newBedType, Integer newCapacity, String newAmenities) throws RoomTypeNotFoundException;
    
    public void deleteRoomType(String typeName) throws RoomTypeNotFoundException;

    public List<RoomTypeEntity> retrieveListOfRoomTypes();

    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException;

    public void updateRoom(String roomNumber, String roomType, StatusEnum status) throws RoomNotFoundException, RoomTypeNotFoundException;

    public RoomTypeEntity retrieveRoomTypeByTypeName(String typeName) throws RoomTypeNotFoundException;

    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public void createNewRoomType(String typeName, String description, String bedType, Integer capacity, String amenities);

    public List<RoomEntity> retrieveAllRooms();

    public String viewRoomDetails(RoomEntity room);
    
}
