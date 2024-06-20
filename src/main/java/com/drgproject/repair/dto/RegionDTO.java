package com.drgproject.repair.dto;

import java.util.List;

public class RegionDTO {
    private Long id;
    private String name;
    private List<Long> depotIds;

    public RegionDTO() {}

    public RegionDTO(Long id, String name, List<Long> depotIds) {
        this.id = id;
        this.name = name;
        this.depotIds = depotIds;
    }

    public RegionDTO(String name, List<Long> depotIds) {
        this.name = name;
        this.depotIds = depotIds;
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

    public List<Long> getDepotIds() {
        return depotIds;
    }

    public void setDepotIds(List<Long> depotIds) {
        this.depotIds = depotIds;
    }
}
