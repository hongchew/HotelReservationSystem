/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.ReservationRecordEntity;
import java.util.ArrayList;
import java.util.Date;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationRecordNotFoundException;
import util.objects.ReservationTicket;


/**
 *
 * @author Hong Chew
 */

public interface RoomReservationControllerRemote {

    public String retrieveReservationDetails(Long resId) throws ReservationRecordNotFoundException, EntityMismatchException;

    public void guestLogin(String username, String password) throws InvalidLoginCredentialException;

    public void guestLogout();

    public ArrayList<ReservationRecordEntity> retrieveAllReservation();

    public ReservationTicket searchRooms(Date startDate, Date endDate);

    public ArrayList<ReservationRecordEntity> reserveRoom(ReservationTicket ticket);
    
}
