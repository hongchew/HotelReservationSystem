/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Hong Chew
 */
@Stateful
@Local(RoomReservationControllerLocal.class)
@Remote(RoomReservationControllerRemote.class)
public class RoomReservationController implements RoomReservationControllerRemote, RoomReservationControllerLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public RoomReservationController() {
    }

    
    
    
    
}
