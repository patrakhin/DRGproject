package com.drgproject.repair.dto;

public class HomeDepotDTO {
    private Long id;
    private String depot;
    private Long regionId;
    private String regionName; // Новое поле

    public HomeDepotDTO() {}

    public HomeDepotDTO(Long id, String depot, Long regionId, String regionName) {
        this.id = id;
        this.depot = depot;
        this.regionId = regionId;
        this.regionName = regionName;
    }

    public HomeDepotDTO(String depot, Long regionId, String regionName) {
        this.depot = depot;
        this.regionId = regionId;
        this.regionName = regionName;
    }

    // Геттеры и сеттеры

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

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
