/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.*;

/**
 *
 * @author Hong Chew
 */
@Entity
public class ReservationRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private RoomTypeEntity roomType;
    
    private RoomEntity assignedRoom;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutTime;
    
    @Column(scale = 2)
    private BigDecimal bill;
    
    @ManyToOne
    private GuestEntity reservedByGuest;
    
    @ManyToOne
    private PartnerEntity reservedByPartner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public RoomEntity getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(RoomEntity assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public GuestEntity getReservedByGuest() {
        return reservedByGuest;
    }

    public void setReservedByGuest(GuestEntity reservedByGuest) {
        this.reservedByGuest = reservedByGuest;
    }

    public PartnerEntity getReservedByPartner() {
        return reservedByPartner;
    }

    public void setReservedByPartner(PartnerEntity reservedByPartner) {
        this.reservedByPartner = reservedByPartner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationRecordEntity)) {
            return false;
        }
        ReservationRecordEntity other = (ReservationRecordEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationRecordEntity[ id=" + id + " ]";
    }
    
}
