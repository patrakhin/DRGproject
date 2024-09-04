package com.drgproject.repair.dto;

public class LocoFilterDTO {
    private Long id;
    private String homeRegion;
    private String homeDepot;
    private String locoType;
    private String sectionNumber;
    private String freeSection;
    private String busySection;

    public LocoFilterDTO() {}

    public LocoFilterDTO(Long id, String homeRegion, String homeDepot, String locoType, String sectionNumber, String freeSection, String busySection) {
        this.id = id;
        this.homeRegion = homeRegion;
        this.homeDepot = homeDepot;
        this.locoType = locoType;
        this.sectionNumber = sectionNumber;
        this.freeSection = freeSection;
        this.busySection = busySection;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomeRegion() {
        return homeRegion;
    }

    public void setHomeRegion(String homeRegion) {
        this.homeRegion = homeRegion;
    }

    public String getHomeDepot() {
        return homeDepot;
    }

    public void setHomeDepot(String homeDepot) {
        this.homeDepot = homeDepot;
    }

    public String getLocoType() {
        return locoType;
    }

    public void setLocoType(String locoType) {
        this.locoType = locoType;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getFreeSection() {
        return freeSection;
    }

    public void setFreeSection(String freeSection) {
        this.freeSection = freeSection;
    }

    public String getBusySection() {
        return busySection;
    }

    public void setBusySection(String busySection) {
        this.busySection = busySection;
    }
}
