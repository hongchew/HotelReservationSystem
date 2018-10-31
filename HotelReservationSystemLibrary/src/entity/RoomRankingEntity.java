/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Hong Chew
 * Room Ranking entity for keeping track of room ranks. Only 1 instance will be created and updated constantly. id = 1
 * Rank 0 = most premium, Rank rankings.size()-1 = least premium
 * 
 */
@Entity
public class RoomRankingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Id
    private Long id;
    
    @OneToMany
    private ArrayList<RoomTypeEntity> rankings;

    public RoomRankingEntity() {
        rankings = new ArrayList<>();
    }

    public RoomRankingEntity(Long id) {
        this.id = id;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomRankingEntity)) {
            return false;
        }
        RoomRankingEntity other = (RoomRankingEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRankingEntity[ id=" + id + " ]";
    }

    public ArrayList<RoomTypeEntity> getRankings() {
        return rankings;
    }
    
    
}
