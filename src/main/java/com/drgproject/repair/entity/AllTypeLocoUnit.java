package com.drgproject.repair.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "all_type_loco_unit")
public class AllTypeLocoUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type_loco_unit")
    private String typeLocoUnit;

    public AllTypeLocoUnit(){}

    public AllTypeLocoUnit(String typeLocoUnit) {
        this.typeLocoUnit = typeLocoUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeLocoUnit() {
        return typeLocoUnit;
    }

    public void setTypeLocoUnit(String typeLocoUnit) {
        this.typeLocoUnit = typeLocoUnit;
    }
}
