package com.drgproject.repair.dto;

public class TypeLocoDTO {
    private Long id;
    private String locoType;

    public TypeLocoDTO() {}

    public TypeLocoDTO(Long id, String locoType) {
        this.id = id;
        this.locoType = locoType;
    }

    public TypeLocoDTO(String locoType) {
        this.locoType = locoType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocoType() {
        return locoType;
    }

    public void setLocoType(String locoType) {
        this.locoType = locoType;
    }
}
