/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import javax.ejb.Local;

/**
 *
 * @author saranya
 */
@Local
public interface RoomSessionBeanLocal {
    public RoomTypeEntity returnNewRoomTypeEntity(String typeName, String description, String bedType, Integer capacity, String amenities, int i);
            
}
