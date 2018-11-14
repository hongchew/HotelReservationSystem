/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ExceptionReportEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.StatusEnum;
import util.exception.LastAvailableRateException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;

/**
 *
 * @author saranya
 */
@Remote
public interface RoomSessionBeanRemote {

    public String viewRoomTypeDetails(String typeName) throws RoomTypeNotFoundException;

    public void updateRoomType(String typeName, String newDescription, String newBedType, Integer newCapacity, String newAmenities) throws RoomTypeNotFoundException;
    
    public Boolean deleteRoomType(String typeName) throws RoomTypeNotFoundException;


    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException;

    public void updateRoom(String roomNumber, String roomType, StatusEnum status) throws RoomNotFoundException, RoomTypeNotFoundException;

    public RoomTypeEntity retrieveRoomTypeByTypeName(String typeName) throws RoomTypeNotFoundException;

    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public List<RoomEntity> retrieveAllRooms();

    public String viewRoomDetails(RoomEntity room);

    public List<RoomTypeEntity> getRoomTypesByRanking();

    public Long createNewRoomType(String typeName, String description, String bedType, Integer capacity, String amenities, int i);
    
    public void createNewPublishedRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewNormalRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPeakRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPromotionRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;
 
    public List<ExceptionReportEntity> getListOfExceptionReportsByDate(Date date);

    public Boolean deleteRoom(String roomNumber) throws RoomNotFoundException;

    public List<RoomRateEntity> retrieveAllRoomRates();

    public Boolean deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, LastAvailableRateException;

    public void updateRoomRate(Long roomRateId, BigDecimal ratePerNight, Date startDate, Date endDate, StatusEnum status) throws RoomRateNotFoundException;

    public Integer getNumberOfRoomsAvailable(RoomTypeEntity type, Date date) throws RoomTypeUnavailableException;

    
}
