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

/**
 *
 * @author saranya
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    private int numOfRooms;
    private int capacity;
    private String nameOfRoomType;
    private String description;
    private String amenities;
    @OneToMany(mappedBy = "roomType")
    @ManyToMany(mappedBy = "roomType")
    private List<RoomRate> roomRates;

    public RoomType() {
    }

    public RoomType(Long id, int numOfRooms, int capacity, String nameOfRoomType, String description, String amenities) {
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
    

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
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
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
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
