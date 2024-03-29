/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    
    @OneToOne
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomType;
    
    @ManyToOne
    private RoomEntity assignedRoom;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutTime;
    
    @Column(scale = 2)
    private BigDecimal bill;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservedOn;
    
    @Email
    @Column(nullable = false)
    private String guestEmail;
    
    @ManyToOne
    private GuestEntity reservedByGuest;
    
    @ManyToOne
    private PartnerEntity reservedByPartner;
    
    @OneToOne(mappedBy = "reservation", cascade = {CascadeType.REMOVE})
    private ExceptionReportEntity exception;

    public ReservationRecordEntity() {
        Calendar cal = Calendar.getInstance();
        this.reservedOn = cal.getTime(); //current timestamp
    }
    
    //HORS Reservation client
    public ReservationRecordEntity(RoomTypeEntity roomType, Date startDate, Date endDate, GuestEntity reservedByGuest) {
        this();
        
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestEmail = reservedByGuest.getEmailAddress();
        this.reservedByGuest = reservedByGuest;
    }
    
    //Holiday Reservation System
    public ReservationRecordEntity(RoomTypeEntity roomType, Date startDate, Date endDate, String guestEmail, PartnerEntity reservedByPartner) {
        this();
        
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestEmail = guestEmail;
        this.reservedByPartner = reservedByPartner;
    }
    
    //Front Office Module
    public ReservationRecordEntity(RoomTypeEntity roomType, Date startDate, Date endDate, String guestEmail) {
        this();
        
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestEmail = guestEmail;
    }
    
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
    
    public Date getStartDate(){
        return startDate;
    }
    
    public String getStartDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate(){
        return endDate;
    }
    
    public String getEndDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReservedOnAsString(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, hh:mm");
        return dateFormat.format(getReservedOn());
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
        return "Reservation ID: " + getId() + "\n" +
                "Start Date: " + getStartDateAsString() + "\n" +
                "End Date: " + getEndDateAsString() + "\n" +
                "Room Type Reserved: " + getRoomType().getTypeName() + "\n" +
                "Bill: $" + getBill();
    }

    public ExceptionReportEntity getException() {
        return exception;
    }

    public void setException(ExceptionReportEntity exception) {
        this.exception = exception;
    }

    /**
     * @return the reservedOn
     */
    public Date getReservedOn() {
        return reservedOn;
    }

    /**
     * @param reservedOn the reservedOn to set
     */
    public void setReservedOn(Date reservedOn) {
        this.reservedOn = reservedOn;
    }

    /**
     * @return the guestEmail
     */
    public String getGuestEmail() {
        return guestEmail;
    }

    /**
     * @param guestEmail the guestEmail to set
     */
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }
    
    
}
