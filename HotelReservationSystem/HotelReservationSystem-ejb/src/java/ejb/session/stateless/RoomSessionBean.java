/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AvailabilityRecordEntity;
import entity.ExceptionReportEntity;
import entity.RoomEntity;
import entity.RoomRankingEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.RateTypeEnum;
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        
        return cal.getTime();     
    }
    
    public Date setHoursMinsToZero(Date date){
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        return cal.getTime();
    }
    
    @Override
    public RoomTypeEntity returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i) {
        RoomTypeEntity newRoomType = new RoomTypeEntity(typeName, description, bedType, capacity, amenities);
        em.persist(newRoomType);
            
        insertRoomRank(newRoomType, i);
        
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        date = setHoursMinsToZero(date);
        for(int j = 0; j <= 365; j++){ //create next 365 days of availability record in advance.
            date = addDays(date, 1);
            AvailabilityRecordEntity avail = new AvailabilityRecordEntity(date, newRoomType);
            em.persist(avail);
            em.flush();
            newRoomType.addNewAvailabilityRecord(avail);
        }
        
        return newRoomType;
    }
      
    @Override
    public String viewRoomTypeDetails(String typeName) throws RoomTypeNotFoundException {
        RoomTypeEntity roomType;
        try{
            roomType = retrieveRoomTypeByTypeName(typeName);
        }catch (RoomTypeNotFoundException e) {
            throw e;
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
        }catch (RoomTypeNotFoundException | ConstraintViolationException  e) {
            eJBContext.setRollbackOnly();
            throw e;
        }

    }
    
    /**
     *
     * @param typeName
     * @return false if roomType disabled, true if roomType deleted
     * @throws RoomTypeNotFoundException
     */
    @Override
    public Boolean deleteRoomType(String typeName) throws RoomTypeNotFoundException {
        RoomTypeEntity thisRoomType;
        try{
            thisRoomType = retrieveRoomTypeByTypeName(typeName);
        } catch (RoomTypeNotFoundException e) {
            throw e;
        }
        
        //get a list of rooms of type typeName that is occupied 
        Query query = em.createQuery("SELECT room FROM RoomEntity room WHERE room.roomType.typeName = :typename AND room.occupancy = :occupancy");
        query.setParameter("typename", typeName);
        query.setParameter("occupancy", IsOccupiedEnum.OCCUPIED);
        if(query.getResultList().isEmpty()) { //No rooms of the type are occupied
            deleteRoomRank(thisRoomType);
            em.remove(thisRoomType); 
            return true;
        }else { //Some room of the type are occupied
            thisRoomType.setStatus(StatusEnum.DISABLED);
            return false;
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
            em.flush();
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
            throw e;
        }
    }
    
    @Override
    public Boolean deleteRoom(String roomNumber) throws RoomNotFoundException {  
        try {
            RoomEntity thisRoom = retrieveRoomByRoomNumber(roomNumber);
            RoomTypeEntity thisRoomType = thisRoom.getRoomType();
            if(thisRoom.getOccupancy().equals(IsOccupiedEnum.UNOCCUPIED)) {
                thisRoomType.removeRoom(thisRoom);
                em.remove(thisRoom);
                return true;
            }else {
                thisRoom.setStatus(StatusEnum.DISABLED);
                return false;
            }           
        } catch(RoomNotFoundException e) {
            throw e;
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
    public void createNewPublishedRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        if(roomType == null){
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }        
        RoomRateEntity newPublishedRate = new RoomRateEntity(rateName, ratePerNight, RateTypeEnum.PUBLISHED, startDate, endDate);
        em.persist(newPublishedRate);

        roomType.addNewRoomRate(newPublishedRate);
        newPublishedRate.setRoomType(roomType);
        
    }
    
    @Override
    public void createNewNormalRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        if(roomType == null){
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }
        RoomRateEntity newNormalRate = new RoomRateEntity(rateName, ratePerNight, RateTypeEnum.NORMAL, startDate, endDate);
        em.persist(newNormalRate);

        roomType.addNewRoomRate(newNormalRate);
        newNormalRate.setRoomType(roomType);
    }
    
    @Override
    public void createNewPeakRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        if(roomType == null){
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }        
        RoomRateEntity newPeakRate = new RoomRateEntity(rateName, ratePerNight, RateTypeEnum.PEAK, startDate, endDate);
        em.persist(newPeakRate);

        roomType.addNewRoomRate(newPeakRate);
        newPeakRate.setRoomType(roomType);
    }
    
    @Override
    public void createNewPromotionRate(String rateName, BigDecimal ratePerNight, Date startDate, Date endDate, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        if(roomType == null){
            throw new RoomTypeNotFoundException("Room Type Not Found");
        }
        RoomRateEntity newPromotionRate = new RoomRateEntity(rateName, ratePerNight, RateTypeEnum.PROMOTION, startDate, endDate);
        em.persist(newPromotionRate);  

        roomType.addNewRoomRate(newPromotionRate);
        newPromotionRate.setRoomType(roomType);
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
    public List<RoomTypeEntity> getRoomRanks(){
        RoomRankingEntity ranks = em.find(RoomRankingEntity.class, new Long(1));
        ranks.getRoomTypeEntities().size();
        return ranks.getRoomTypeEntities();
    }

    
    public void insertRoomRank(RoomTypeEntity roomType, int index){
        //RoomRankingEntity ranks = em.find(RoomRankingEntity.class, new Long(1));
        Query query = em.createQuery("SELECT r FROM RoomRankingEntity r WHERE r.name = :name");
        query.setParameter("name", "rankings");
        RoomRankingEntity ranks = (RoomRankingEntity) query.getSingleResult();
        ranks.getRoomTypeEntities().add(index, roomType);
    }
    
    private void deleteRoomRank(RoomTypeEntity roomType){
        Query query = em.createQuery("SELECT r FROM RoomRankingEntity r WHERE r.name = :name");
        query.setParameter("name", "rankings");
        RoomRankingEntity ranks = (RoomRankingEntity) query.getSingleResult();
        
        ranks.getRoomTypeEntities().remove(roomType);

        
    }
    
    @Override
    public List<ExceptionReportEntity> getListOfExceptionReportsByDate(Date date){
        Query q = em.createQuery("SELECT e FROM ExceptionReportEntity e WHERE e.exceptionDate = :date");
        q.setParameter("date", date);
        
        return q.getResultList();
    }
    
    @Override
    public List<RoomRateEntity> retrieveAllRoomRates(){
        Query q = em.createQuery("SELECT r FROM RoomRateEntity r");
        return q.getResultList();
    }
    
    @Override
    public Boolean deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException, LastAvailableRateException{
        RoomRateEntity rate = em.find(RoomRateEntity.class, roomRateId);
        if (rate == null){
            throw new RoomRateNotFoundException("Room Rate Not Found");
        }
        
        Date today = new Date();
        if(rate.getRateType().equals(RateTypeEnum.NORMAL) || rate.getRateType().equals(RateTypeEnum.PUBLISHED)){
            Query q = em.createQuery("SELECT r FROM RoomRateEntity r WHERE r.rateType = :rateType");
            q.setParameter("rateType", rate.getRateType());
            
            List<RoomRateEntity> rates = q.getResultList();
            int validRates = 0;
            if(rates.size() == 1){
                throw new LastAvailableRateException("You cannot delete the last valid Normal or Published rate");
            }else{
                for(RoomRateEntity r :rates){
                    if(r.getStartDate().before(today)){
                        validRates++;
                    }
                }
                if(validRates <= 1){
                    throw new LastAvailableRateException("You cannot delete the last valid Normal or Published rate");
                }
            }
        }
        

        if(today.after(rate.getStartDate()) && ((rate.getEndDate() == null) || today.before(rate.getEndDate()))){ //rate still valid
            rate.setStatus(StatusEnum.DISABLED);
            return false;
        }else{
            rate.getRoomType().getRoomRate().remove(rate);
            em.remove(rate);
            return true;
        }
    }
    
    @Override
    public void updateRoomRate(Long roomRateId, BigDecimal ratePerNight, Date startDate, Date endDate, StatusEnum status) throws RoomRateNotFoundException{
        RoomRateEntity roomRate = em.find(RoomRateEntity.class, roomRateId);
        if(roomRate == null){
            throw new RoomRateNotFoundException("Room Rate not found.");
        }
        roomRate.setRatePerNight(ratePerNight);
        roomRate.setStartDate(startDate);
        roomRate.setEndDate(endDate);
        
    }
    
    @Override
    public Integer getNumberOfRoomsAvailable(RoomTypeEntity type, Date date) throws RoomTypeUnavailableException{
        Query query = em.createQuery("SELECT a FROM AvailabilityRecordEntity a WHERE a.roomType = :type AND a.availabiltyRecordDate =:date");
        query.setParameter("roomType", type);
        query.setParameter("date", date);
        
        try{
            AvailabilityRecordEntity avail = (AvailabilityRecordEntity) query.getSingleResult();
            return avail.getAvailableRooms();
        }catch(NoResultException e){
            throw new RoomTypeUnavailableException("Availability Record not found");
        }
        
    }
    
    @Override
    public BigDecimal getRatePerNight(RoomTypeEntity roomType, Date date) throws RoomRateNotFoundException{
        List<RoomRateEntity> normalList, peakList, promoList;
        normalList = getValidRateList(roomType, date, RateTypeEnum.NORMAL);
        peakList = getValidRateList(roomType, date, RateTypeEnum.PEAK);
        promoList = getValidRateList(roomType, date, RateTypeEnum.PROMOTION);
        
        if(!promoList.isEmpty()){
            return getPrevailingRatePerNight(promoList);
        }else if(!peakList.isEmpty()){
            return getPrevailingRatePerNight(peakList);
        }else if(!normalList.isEmpty()){
            return getPrevailingRatePerNight(normalList);
        }else{
            throw new RoomRateNotFoundException("No valid room rates found");
        }
    }
    
    @Override
    public List<RoomRateEntity> getValidRateList(RoomTypeEntity roomType, Date date, RateTypeEnum rateType){
        Query q = em.createQuery("SELECT r FROM RoomRateEntity r WHERE r.startDate <= :date AND r.endDate >= :date "
                                + "AND r.status = :status AND r.rateType = :rateType AND r.roomType = :roomType");
        q.setParameter("date", date);
        q.setParameter("status", StatusEnum.AVAILABLE);
        q.setParameter("rateType", rateType);
        q.setParameter("roomType", roomType);
        
        return q.getResultList();
    }
    
    @Override
    public BigDecimal getPrevailingRatePerNight(List<RoomRateEntity> rateList){
        BigDecimal prevailingRate = rateList.get(0).getRatePerNight();
        
        for(RoomRateEntity rate : rateList){
            prevailingRate = prevailingRate.min(rate.getRatePerNight());
        }
        return prevailingRate;
    }
    
    @Override
    public BigDecimal getPublishedRatePerNight(RoomTypeEntity roomType, Date date) throws RoomRateNotFoundException{
        
        List<RoomRateEntity> publishedRates = getValidRateList(roomType, date, RateTypeEnum.PUBLISHED);
        if(publishedRates.isEmpty()){
            throw new RoomRateNotFoundException("No valid room rates found.");
        }
        
        return getPrevailingRatePerNight(publishedRates);
        
    }
    
    
    //Generate a new avail record for all room type every day for today + 365 day
    /*@Schedule(hour = "0")
    public void addNewAvailRecordDaily(){
        Query q = em.createQuery("SELECT r FROM RoomTypeEntity r");
        List<RoomTypeEntity> roomTypes = q.getResultList();
        for(RoomTypeEntity r : roomTypes){
            AvailabilityRecordEntity avail = new AvailabilityRecordEntity(addDays(new Date(),365), r);
            r.addNewAvailabilityRecord(avail);
        }
    }*/
}
