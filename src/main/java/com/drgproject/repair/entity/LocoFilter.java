package com.drgproject.repair.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "loco_filter")
public class LocoFilter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "home_region")
    private String homeRegion;
    @Column(name = "home_depot")
    private String homeDepot;
    @Column(name = "loco_type")
    private String locoType;
    @Column(name = "section_number")
    private String sectionNumber;
    @Column(name = "free_section")
    private String freeSection;
    @Column(name = "busy_section")
    private String busySection;

    public LocoFilter() {}

    public LocoFilter(String homeRegion, String homeDepot, String locoType, String sectionNumber, String freeSection, String busySection) {
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
