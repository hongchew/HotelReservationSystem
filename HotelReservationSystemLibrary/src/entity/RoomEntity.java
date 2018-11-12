/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.*;
import util.enumeration.IsOccupiedEnum;
import util.enumeration.StatusEnum;

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
    
    @Column(nullable = false)
    private Integer floor;
    
    @Column(nullable = false)
    private Integer unit;
    
    @Column(unique = true)
    private String roomNumber;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date isOccupiedTo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IsOccupiedEnum occupancy;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomType;

    
    @OneToMany(mappedBy = "assignedRoom", cascade ={CascadeType.REMOVE})
    private ArrayList<ReservationRecordEntity> reservationRecords;
            
    //default no argument constructor
    public RoomEntity() {
    }

    public RoomEntity(Integer floor, Integer unit, RoomTypeEntity roomType) {
        this.floor = floor;
        this.unit = unit;
        this.roomNumber = floor.toString() + "-" + unit.toString();
        this.status = StatusEnum.AVAILABLE;
        this.occupancy = IsOccupiedEnum.UNOCCUPIED;
        this.roomType = roomType;
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

    public StatusEnum getStatus() {
        return status;
    }

    public IsOccupiedEnum getOccupancy() {
        return occupancy;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public ArrayList<ReservationRecordEntity> getReservationRecords() {
        return reservationRecords;
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

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public void setOccupancy(IsOccupiedEnum occupancy) {
        this.occupancy = occupancy;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public void setReservationRecords(ArrayList<ReservationRecordEntity> reservationRecords) {
        this.reservationRecords = reservationRecords;
    }
 
    
}
    


