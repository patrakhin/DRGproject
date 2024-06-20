package com.drgproject.repair.dto;

public class SystemNameDTO {
    private Long id;
    private String sysName;

    public SystemNameDTO() {}

    public SystemNameDTO(Long id, String sysName) {
        this.id = id;
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
