package com.project.entity;

import com.project.common.entity.DomainObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

/**
 * Created by xieyanhao on 16/4/24.
 */
@Entity
@Table(name = "room_tbl")
public class RoomEntity extends DomainObject {

    private String name;
    private String image;
    private String regional;
    private FloorEntity floor;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "regional")
    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    @ManyToOne
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})
    @JoinColumn(name="floor_id")
    public FloorEntity getFloor() {
        return floor;
    }

    public void setFloor(FloorEntity floor) {
        this.floor = floor;
    }

}
