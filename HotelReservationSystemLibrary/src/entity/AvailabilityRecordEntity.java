/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Hong Chew
 */
@Entity
public class AvailabilityRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date availabiltyRecordDate;
    
    private Integer totalReservations;
    
    @ManyToOne
    private RoomTypeEntity roomType;

    public AvailabilityRecordEntity() {
    }

    public AvailabilityRecordEntity(Date date, RoomTypeEntity roomType) {
        this.availabiltyRecordDate = date;
        this.totalReservations = 0;
        this.roomType = roomType;
    }
    
    public Integer getAvailableRooms(){
        return roomType.getTotalRooms() - totalReservations;
    }
    
    public Boolean getAvailability(){
        int i = roomType.getTotalRooms() - totalReservations;
        if(i <= 0){
            return false;
        }else{
        return true;
        }
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof AvailabilityRecordEntity)) {
            return false;
        }
        AvailabilityRecordEntity other = (AvailabilityRecordEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AvailabilityRecordEntity[ id=" + id + " ]";
    }

    public Date getAvailabiltyRecordDate() {
        return availabiltyRecordDate;
    }

    public void setAvailabiltyRecordDate(Date availabiltyRecordDate) {
        this.availabiltyRecordDate = availabiltyRecordDate;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }
    
    public void addOneReservation(){
        this.totalReservations++;
    }
}
