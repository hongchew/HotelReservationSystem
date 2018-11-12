/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;

/**
 *
 * @author Hong Chew
 */
@Entity
public class GuestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @Size(min = 1, max = 32, message = "Name should be between 1 and 32 characters.")
    private String guestName;
    
    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 32, message = "Username should be between 1 and 32 characters.")
    private String username;
            
    @Column(nullable = false)
    @Size(min = 5, max = 32, message = "Password should be between 1 and 32 characters.")
    private String password;
    
    @Column(nullable = false, unique = true)
    @Email(message = "Please enter a valid email address")
    private String emailAddress;
    
    @OneToMany(mappedBy = "reservedByGuest")
    private ArrayList<ReservationRecordEntity> reservationRecords;

    public GuestEntity() {
        this.reservationRecords = new ArrayList<>();
    }

    public GuestEntity(String guestName, String username, String password, String emailAddress) {
        this();
        
        this.guestName = guestName;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
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
        if (!(object instanceof GuestEntity)) {
            return false;
        }
        GuestEntity other = (GuestEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.guestName + "      " + this.emailAddress;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ArrayList<ReservationRecordEntity> getReservationRecords() {
        return reservationRecords;
    }

    public void setReservationRecords(ArrayList<ReservationRecordEntity> reservationRecords) {
        this.reservationRecords = reservationRecords;
    }
    
    
}
