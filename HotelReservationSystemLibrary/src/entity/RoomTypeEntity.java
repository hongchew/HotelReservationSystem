/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author saranya
 */
@Entity
public class RoomTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    private int numOfRooms;
    private int capacity;
    private String nameOfRoomType;
    private String description;
    private String amenities;
    private String size;

    @OneToMany(mappedBy = "roomTypeEntity")
    private List<RoomRateEntity> roomRates;
    @OneToMany(mappedBy = "roomType")
    private List<RoomEntity> roomEntitys;
    @OneToOne
    private RoomEntity roomEntity;


    public RoomTypeEntity() {
    }

    public RoomTypeEntity(Long id, int numOfRooms, int capacity, String nameOfRoomType, String description, String amenities, String size) {
        this.roomTypeId = id;
        this.numOfRooms = numOfRooms;
        this.capacity = capacity;
        this.nameOfRoomType = nameOfRoomType;
        this.description = description;
        this.amenities = amenities;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getNameOfRoomType() {
        return nameOfRoomType;
    }

    public String getDescription() {
        return description;
    }

    public String getAmenities() {
        return amenities;
    }

    public String getSize() {
        return size;
    }

    public List<RoomRateEntity> getRoomRates() {
        return roomRates;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setNameOfRoomType(String nameOfRoomType) {
        this.nameOfRoomType = nameOfRoomType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setRoomRates(List<RoomRateEntity> roomRates) {
        this.roomRates = roomRates;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }
    
}
