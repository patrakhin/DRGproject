package com.drgproject.repair.entiny;


import jakarta.persistence.*;

@Entity
@Table(name = "type_loco")
public class TypeLoco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_loco")
    private String locoType;

    public TypeLoco() {
    }

    public TypeLoco(String typeLoco) {
        this.locoType = typeLoco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeLoco() {
        return locoType;
    }

    public void setTypeLoco(String typeLoco) {
        this.locoType = typeLoco;
    }
}
