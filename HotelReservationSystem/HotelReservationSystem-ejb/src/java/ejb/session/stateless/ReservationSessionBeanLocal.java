/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.Date;
import util.exception.EntityMismatchException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;


/**
 *
 * @author Hong Chew
 */

public interface ReservationSessionBeanLocal {

    public String retrieveReservationDetails(Long resId, Long guestId) throws ReservationRecordNotFoundException, EntityMismatchException;

    public ReservationTicket searchRooms(Date startDate, Date endDate);
    
}
