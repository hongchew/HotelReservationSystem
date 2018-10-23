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

/**
 *
 * @author saranya
 */
@Entity
public class RoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private int roomNum;
    private boolean isOccupied;
    // isUsable ensures that the room is in good condition for customers to use 
    private boolean isUsable;
    
    public RoomEntity() {
        
    }
    
    public RoomEntity(Long roomId, int roomNum, boolean isOccupied, boolean isUsable) {
        this.isOccupied = isOccupied;
        this.isUsable = isUsable;
        this.roomId = roomId;
        this.roomNum = roomNum;   
    }
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
   
    

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }

    public int getRoomNum() {
        return roomNum;
    }

    public boolean isIsOccupied() {
        return isOccupied;
    }

    public boolean isIsUsable() {
        return isUsable;
    }
    
}
