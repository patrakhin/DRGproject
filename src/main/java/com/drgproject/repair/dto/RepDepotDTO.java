package com.drgproject.repair.dto;

public class RepDepotDTO {
    private Long id;
    private String name;
    private String regionName;

    // Конструкторы
    public RepDepotDTO() {}

    public RepDepotDTO(Long id, String name, String regionName) {
        this.id = id;
        this.name = name;
        this.regionName = regionName;
    }

    // Геттеры и сеттеры
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

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
