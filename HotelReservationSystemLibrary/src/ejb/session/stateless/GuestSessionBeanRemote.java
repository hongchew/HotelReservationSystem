/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Hong Chew
 */

public interface GuestSessionBeanRemote {
    public void registerNewGuest(String name, String username, String password, String emailAddress);

    public GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException;
}
