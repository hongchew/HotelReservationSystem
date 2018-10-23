/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;

/**
 *
 * @author saranya
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomRateId;
    private String nameOfRate;
    @ManyToMany
    private ArrayList<RoomType> roomType;
    private BigDecimal ratePerNight;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date start;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date end;

    public RoomRate() {
    }

    public RoomRate(Long roomRateId, String nameOfRate, ArrayList<RoomType> roomType, BigDecimal ratePerNight, Date start, Date end) {
        this.roomRateId = roomRateId;
        this.nameOfRate = nameOfRate;
        this.roomType = roomType;
        this.ratePerNight = ratePerNight;
        this.start = start;
        this.end = end;
    }
    
    


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNameOfRate() {
        return nameOfRate;
    }

    public ArrayList<RoomType> getRoomType() {
        return roomType;
    }

    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setNameOfRate(String nameOfRate) {
        this.nameOfRate = nameOfRate;
    }

    public void setRoomType(ArrayList<RoomType> roomType) {
        this.roomType = roomType;
    }

    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    

    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + roomRateId + " ]";
    }
    
}
