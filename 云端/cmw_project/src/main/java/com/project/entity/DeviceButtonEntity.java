package com.project.entity;

import com.project.common.entity.DomainObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

/**
 * Created by xieyanhao on 16/9/20.
 */
@Entity
@Table(name = "device_button_tbl")
public class DeviceButtonEntity extends DomainObject {

    private String name; // 按键名称
    private String instructionCode; // 指令编码
    private DeviceEntity device;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "instruction_code")
    public String getInstructionCode() {
        return instructionCode;
    }

    public void setInstructionCode(String instructionCode) {
        this.instructionCode = instructionCode;
    }

    @ManyToOne
    @Cascade({CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.SAVE_UPDATE})
    @JoinColumn(name="device_id")
    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(DeviceEntity device) {
        this.device = device;
    }
}
