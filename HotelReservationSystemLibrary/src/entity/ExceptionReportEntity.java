/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Hong Chew
 */
@Entity
public class ExceptionReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    
    @Temporal(TemporalType.DATE)
    private Date exceptionDate;
    
    @Column(length = 140)
    private String errorReport;    
    
    
    @OneToOne(optional = false)
    private ReservationRecordEntity reservation;

    public ExceptionReportEntity() {
    }

    public ExceptionReportEntity(Date exceptionDate, String errorReport, ReservationRecordEntity reservation) {
        this.exceptionDate = exceptionDate;
        this.errorReport = errorReport;
        this.reservation = reservation;
    }

        
    public Long getId() {
        return reportId;
    }

    public void setId(Long id) {
        this.reportId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportId != null ? reportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExceptionReportEntity)) {
            return false;
        }
        ExceptionReportEntity other = (ExceptionReportEntity) object;
        if ((this.reportId == null && other.reportId != null) || (this.reportId != null && !this.reportId.equals(other.reportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String report =  "Error encountered for Reservation " + reservation.getId() + ":/n" + errorReport;
        return report;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Date getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public String getErrorReport() {
        return errorReport;
    }

    public void setErrorReport(String errorReport) {
        this.errorReport = errorReport;
    }

    public ReservationRecordEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationRecordEntity reservation) {
        this.reservation = reservation;
    }
    
    
}
