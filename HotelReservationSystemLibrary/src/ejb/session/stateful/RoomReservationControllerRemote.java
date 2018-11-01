/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;


/**
 *
 * @author Hong Chew
 */

public interface RoomReservationControllerRemote {

    public void setGuest(Long guestId);

    public String retrieveReservationDetails(Long resId, Long guestId) throws ReservationRecordNotFoundException, EntityMismatchException;
    
}
