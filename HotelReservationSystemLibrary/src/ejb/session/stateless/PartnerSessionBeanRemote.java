/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;

/**
 *
 * @author Hong Chew
 */

public interface PartnerSessionBeanRemote {

    public void createNewPartner(String partnerName, String username, String password);

    public List<PartnerEntity> retrieveAllPartners();
    
}
