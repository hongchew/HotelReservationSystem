/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
}
