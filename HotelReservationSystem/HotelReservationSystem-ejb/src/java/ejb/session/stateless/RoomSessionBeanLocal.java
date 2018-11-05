/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RateTypeEnum;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;

/**
 *
 * @author saranya
 */
@Local
public interface RoomSessionBeanLocal {
    public RoomTypeEntity returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i);

    public void createNewPublishedRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewNormalRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPeakRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public void createNewPromotionRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException;

    public Integer getNumberOfRoomsAvailable(RoomTypeEntity type, Date date) throws RoomTypeUnavailableException;

    public BigDecimal getRatePerNight(RoomTypeEntity roomType, Date date) throws RoomRateNotFoundException;

    public List<RoomRateEntity> getValidRateList(RoomTypeEntity roomType, Date date, RateTypeEnum rateType);

    public BigDecimal getPrevailingRatePerNight(List<RoomRateEntity> rateList);

    public BigDecimal getPublishedRatePerNight(RoomTypeEntity roomType, Date date) throws RoomRateNotFoundException;
    
    public void createNewRoom(Integer floor, Integer unit, String roomType) throws RoomTypeNotFoundException;

}
