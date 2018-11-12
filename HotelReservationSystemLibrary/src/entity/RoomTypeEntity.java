/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import util.enumeration.StatusEnum;

/**
 *
 * @author saranya
 */
@Entity
public class RoomTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;
    @Column(length = 100, nullable = false)
    private String typeName;
    private Integer totalRooms;
    @Column(length = 140, nullable = false)
    private String description;
    @Column(length = 140, nullable = false)
    private String bedType;
    private Integer capacity;
    @Column(length = 140, nullable = false)
    private String amenities;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;
    
    @OneToMany(mappedBy = "roomType", cascade = {CascadeType.REMOVE})
    private ArrayList<RoomEntity> rooms;
    
    @OneToMany(mappedBy = "roomType", cascade = {CascadeType.REMOVE})
    private ArrayList<RoomRateEntity> roomRate;
    
    @OneToMany(mappedBy = "roomType", cascade = {CascadeType.REMOVE})
    private ArrayList<AvailabilityRecordEntity> availabilityRecordEntitiess;

    
    public RoomTypeEntity() {
        rooms = new ArrayList<>();
        roomRate = new ArrayList<>();
        availabilityRecordEntitiess = new ArrayList<>();
    }

    public RoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities) {
        this();
        
        this.typeName = typeName;
        this.totalRooms = 0;
        this.description = description;
        this.bedType = bedType;
        this.capacity = capacity;
        this.amenities = amenities;
        this.status = StatusEnum.AVAILABLE;
    }
    

    public Long getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getTotalRooms() {
        return totalRooms;
    }

    public String getDescription() {
        return description;
    }

    public String getBedType() {
        return bedType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public StatusEnum getStatus() {
        return status;
    }
    public ArrayList<RoomRateEntity> getRoomRate() {
        return roomRate;
    }
    
    public ArrayList<RoomEntity> getRooms() {
        return rooms;
    }


    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public void setRooms(ArrayList<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    public List<AvailabilityRecordEntity> getAvailabilityRecordEntitiess() {
        return availabilityRecordEntitiess;
    }
 
    public void addRoom(RoomEntity room){
        rooms.add(room);
        totalRooms++;
    }
    
    public void removeRoom(RoomEntity room){
        rooms.remove(room);
        totalRooms--;
    }
    
    public void addNewAvailabilityRecord(AvailabilityRecordEntity avail){
        this.availabilityRecordEntitiess.add(avail);
    }
    
    public void addNewRoomRate(RoomRateEntity newRoomRate) {
        roomRate.add(newRoomRate);
    }
}
