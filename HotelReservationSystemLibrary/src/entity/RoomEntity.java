/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author saranya
 */
@Entity
public class RoomEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private Integer roomNum;
    private Boolean isOccupied;
    // isUsable ensures that the room is in good condition for customers to use 
    private Boolean isUsable;
    
    //each room can only have one roomType attribute 
    @OneToOne(mappedBy = "roomEntity")
    private RoomTypeEntity roomType;
    
    
    public RoomEntity() {
        
    }
    
    public RoomEntity(Long roomId, Integer roomNum, Boolean isOccupied, Boolean isUsable, RoomTypeEntity roomType) {
        this.isOccupied = isOccupied;
        this.isUsable = isUsable;
        this.roomId = roomId;
        this.roomNum = roomNum;   
        this.roomType = roomType;
         
    }

    public Long getRoomId() {
        return roomId;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public Boolean getIsOccupied() {
        return isOccupied;
    }

    public Boolean getIsUsable() {
        return isUsable;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setIsUsable(boolean isUsable) {
        this.isUsable = isUsable;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof RoomEntity)) {
            return false;
        }
        RoomEntity other = (RoomEntity) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }
}


