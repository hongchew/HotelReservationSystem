/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import entity.ReservationRecordEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.*;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationRecordNotFoundException;

/**
 *
 * @author Hong Chew
 */
@Stateless
@Local(PartnerSessionBeanLocal.class)
@Remote(PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public PartnerSessionBean() {
    }

    @Override
    public void createNewPartner(String partnerName, String username, String password){
        PartnerEntity partner = new PartnerEntity(partnerName, username, password);
        em.persist(partner);
        
    }
    
    @Override
    public List<PartnerEntity> retrieveAllPartners(){
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");
        List<PartnerEntity> list = query.getResultList();
        
        for(PartnerEntity p: list){
            p.getPartnerId();
            p.getPartnerName();
        }
        
        return list;
    }
    
    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException{
        try{
            PartnerEntity partner = retrievePartnerByUsername(username);
            if(partner.getPassword().equals(password)){
                return partner.getPartnerId();
            }else{
                throw new InvalidLoginCredentialException("Invalid Login Credentials");
            }

        }catch(PartnerNotFoundException ex){
            throw new InvalidLoginCredentialException("Invalid Login Credentials");
        }
    }
    

    private PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException{
        Query q = em.createQuery("SELECT p FROM PartnerEntity p WHERE p.username = :username");
        q.setParameter("username", username);
        
        try{
            return (PartnerEntity) q.getSingleResult();
        }catch(NonUniqueResultException | NoResultException e){
            throw new PartnerNotFoundException("Partner not found.");
        }
    }
    
    @Override
    public PartnerEntity retrievePartnerById(Long id){
        return em.find(PartnerEntity.class, id); 
    }
    
    @Override
    public ArrayList<ReservationRecordEntity> retrieveAllPartnerReservations(Long partnerId){
        PartnerEntity partner = em.find(PartnerEntity.class, partnerId);
        ArrayList<ReservationRecordEntity> reservationRecords = partner.getReservationRecords();
        for(ReservationRecordEntity r : reservationRecords){
            r.getId();
            r.getRoomType();
            r.getStartDate();
            r.getEndDate();
        }
        return reservationRecords;
    }
    
    @Override
    public String viewReservationDetail(Long reservationId, Long partnerId) throws EntityMismatchException, ReservationRecordNotFoundException{
        ReservationRecordEntity reservation = em.find(ReservationRecordEntity.class, reservationId);
        if(reservation == null){
            throw new ReservationRecordNotFoundException("Reservation not found");
        }else if(reservation.getReservedByPartner().getPartnerId().equals(partnerId)){
            String details = "Reservation id: " + reservationId +
                            "\nReserved by: " + reservation.getReservedByPartner() +
                            "\nStart date: " + reservation.getStartDateAsString()+
                            "\nEnd date: " + reservation.getEndDateAsString();
            return details;
        }else{
            throw new EntityMismatchException("Partner ID Provided does not match with Partner ID of Reservation Record.");
        }
    }
    
}
