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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import util.enumeration.RateTypeEnum;
import util.enumeration.StatusEnum;

/**
 *
 * @author saranya
 */
@Entity
public class RoomRateEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;
    private BigDecimal ratePerNight;
    private String rateName;
    
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    
    @Enumerated(EnumType.STRING)
    private RateTypeEnum rateType;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    
    @ManyToOne
    private RoomTypeEntity roomType;

    public RoomRateEntity() {
    }

    public RoomRateEntity(String rateName, BigDecimal ratePerNight, StatusEnum status, RateTypeEnum rateType, Date startDate, Date endDate, RoomTypeEntity roomType) {   
        this.rateName = rateName;
        this.ratePerNight = ratePerNight;
        this.status = status;
        this.rateType = rateType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomType = roomType;   
    }

    public Long getRateId() {
        return rateId;
    }

    public String getRateName() {
        return rateName;
    }

    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public RateTypeEnum getRateType() {
        return rateType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public void setRateType(RateTypeEnum rateType) {
        this.rateType = rateType;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }
      
}
