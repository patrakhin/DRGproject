package com.drgproject.repair.entiny;

import jakarta.persistence.*;

@Entity
@Table(name = "system_name")
public class SystemName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sys_name")
    private String sysName;

    public SystemName() {}

    public SystemName(String sysName) {
        this.sysName = sysName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
}
