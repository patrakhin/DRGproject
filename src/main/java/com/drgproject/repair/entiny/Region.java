package com.drgproject.repair.entiny;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HomeDepot> depots;

    public Region() {
    }

    public Region(String name, List<HomeDepot> depots) {
        this.name = name;
        this.depots = depots;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HomeDepot> getDepots() {
        return depots;
    }

    public void setDepots(List<HomeDepot> depots) {
        this.depots = depots;
    }
}
