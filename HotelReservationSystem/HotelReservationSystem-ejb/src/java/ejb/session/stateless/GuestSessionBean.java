/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;
import entity.GuestEntity;
import entity.ReservationRecordEntity;
import java.util.ArrayList;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.*;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Hong Chew
 */
@Stateless
@Local(GuestSessionBeanLocal.class)
@Remote(GuestSessionBeanRemote.class)
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public GuestSessionBean() {
    }
    
    @Override
    public GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException{
        Query q = em.createQuery("SELECT g FROM GuestEntity g WHERE g.username = :username");
        q.setParameter("username", username);
        try{
            GuestEntity guest = (GuestEntity) q.getSingleResult();
            if(guest.getPassword().equals(password)){
                ArrayList<ReservationRecordEntity> reservations = guest.getReservationRecords();
                for(ReservationRecordEntity r : reservations){
                    r.getBill();
                    r.getStartDate();
                    r.getEndDate();
                    r.getRoomType().getTypeName();
                }
                return guest;
            }else{
                throw new InvalidLoginCredentialException("Invalid Login Credential");
            }
        }catch(NoResultException | NonUniqueResultException e){
            throw new InvalidLoginCredentialException("Invalid Login Credential");
        }
    }
    
    @Override
    public void registerNewGuest(String name, String username, String password, String emailAddress){
        
        GuestEntity newGuest = new GuestEntity(name, username, password, emailAddress);
        em.persist(newGuest);
        
    }
    
}
