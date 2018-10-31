/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationRecordEntity;
import java.util.ArrayList;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Hong Chew
 */

public interface PartnerSessionBeanLocal {

    public Long partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public ArrayList<ReservationRecordEntity> retrieveAllPartnerReservations(Long partnerId);

    public String viewReservationDetail(Long reservationId, Long partnerId);
    
}
