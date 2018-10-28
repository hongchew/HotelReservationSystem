/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.StatusEnum;

/**
 *
 * @author saranya
 */
@Entity
public class RoomTypeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;
    private String typeName;
    private Integer totalRooms;
    private String description;
    private String bedType;
    private Integer capacity;
    private String amenities;
    
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private ArrayList<RoomEntity> rooms;
    
    @OneToMany(mappedBy = "roomTypeEntity")
    private ArrayList<RoomRateEntity> roomRate;
    
    @OneToMany(mappedBy = "roomType")
    private ArrayList<AvailabilityRecordEntity> availabilityRecordEntitiess;

    
    public RoomTypeEntity() {
        rooms = new ArrayList<>();
        roomRate = new ArrayList<>();
        availabilityRecordEntitiess = new ArrayList<>();
    }

    public RoomTypeEntity(Long typeId, String typeName, Integer totalRooms, String description, String bedType, Integer capacity, String amenities, StatusEnum status) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.totalRooms = totalRooms;
        this.description = description;
        this.bedType = bedType;
        this.capacity = capacity;
        this.amenities = amenities;
        this.status = status;
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
    
    public ArrayList<RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<RoomRateEntity> getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(ArrayList<RoomRateEntity> roomRate) {
        this.roomRate = roomRate;
    }

    public List<AvailabilityRecordEntity> getAvailabilityRecordEntitiess() {
        return availabilityRecordEntitiess;
    }
 
}
