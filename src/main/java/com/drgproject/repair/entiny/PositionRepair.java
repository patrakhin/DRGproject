package com.drgproject.repair.entiny;

import jakarta.persistence.*;

@Entity
@Table(name = "position_repair")
public class PositionRepair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pos_repair")
    private String posRepair;

    public PositionRepair(){}

    public PositionRepair(String posRepair) {
        this.posRepair = posRepair;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosRepair() {
        return posRepair;
    }

    public void setPosRepair(String posRepair) {
        this.posRepair = posRepair;
    }
}
