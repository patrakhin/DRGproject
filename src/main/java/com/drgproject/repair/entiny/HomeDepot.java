package com.drgproject.repair.entiny;


import jakarta.persistence.*;

@Entity
@Table(name = "home_depot")
public class HomeDepot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "depot")
    private String depot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    public HomeDepot() {
    }

    public HomeDepot(String depot, Region region) {
        this.depot = depot;
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
