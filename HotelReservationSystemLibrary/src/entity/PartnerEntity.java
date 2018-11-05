/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 *
 * @author saranya
 */
@Entity
public class PartnerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long partnerId;
    
    @Column(unique = true, nullable = false) //1 partner -> 1 accound
    @Size(min = 1, max = 32)
    private String partnerName;
    
    @Column(unique = true, nullable = false)
    @Size(min = 6, max = 32)
    private String username;
    
    @Size(min = 6, max = 32)
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "reservedByPartner")
    private ArrayList<ReservationRecordEntity> reservationRecords;


    public PartnerEntity() {
        reservationRecords = new ArrayList<>();
    }

    public PartnerEntity(String partnerName, String username, String password) {
        this();
        
        this.partnerName = partnerName;
        this.username = username;
        this.password = password;

    }

    public ArrayList<ReservationRecordEntity> getReservationRecords() {
        return reservationRecords;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof PartnerEntity)) {
            return false;
        }
        PartnerEntity other = (PartnerEntity) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return partnerId + "    " + partnerName;
    }
    
}
