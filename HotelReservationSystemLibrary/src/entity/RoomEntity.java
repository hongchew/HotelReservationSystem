/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;

/**
 *
 * @author saranya
 */
@Entity
public class RoomEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private Integer floor;
    private Integer unit;
    private String roomNumber;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date isOccupiedTo;
    
    @Enumerated(EnumType.STRING)
    private StatusEnum isAvailable;
    @Enumerated(EnumType.STRING)
    private IsOccupiedEnum occupancy;
    @ManyToOne
    private RoomTypeEntity roomTypeEntity;

    //default no argument constructor
    public RoomEntity() {
    }

    public RoomEntity(Long roomId, Integer floor, Integer unit, String roomNumber, Date isOccupiedTo, StatusEnum isAvailable, IsOccupiedEnum occupancy) {
        this.roomId = roomId;
        this.floor = floor;
        this.unit = unit;
        this.roomNumber = roomNumber;
        this.isOccupiedTo = isOccupiedTo;
        this.isAvailable = isAvailable;
        this.occupancy = occupancy;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getUnit() {
        return unit;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Date getIsOccupiedTo() {
        return isOccupiedTo;
    }

    public StatusEnum getIsAvailable() {
        return isAvailable;
    }

    public IsOccupiedEnum getOccupancy() {
        return occupancy;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setIsOccupiedTo(Date isOccupiedTo) {
        this.isOccupiedTo = isOccupiedTo;
    }

    public void setIsAvailable(StatusEnum isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setOccupancy(IsOccupiedEnum occupancy) {
        this.occupancy = occupancy;
    }
    
}
    


