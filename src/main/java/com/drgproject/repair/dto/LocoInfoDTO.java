package com.drgproject.repair.dto;

public class LocoInfoDTO {
    private Long id;
    private String region;
    private String homeDepot;
    private String locoType;
    private String locoSection1;
    private String locoFit1;
    private String locoSection2;
    private String locoFit2;
    private String locoSection3;
    private String locoFit3;
    private String locoSection4;
    private String locoFit4;
    private String locoUnit;

    public LocoInfoDTO() {}

    public LocoInfoDTO(Long id, String region, String homeDepot, String locoType,
                       String locoSection1, String locoFit1,
                       String locoSection2, String locoFit2,
                       String locoSection3, String locoFit3,
                       String locoSection4, String locoFit4,
                       String locoUnit) {
        this.id = id;
        this.region = region;
        this.homeDepot = homeDepot;
        this.locoType = locoType;
        this.locoSection1 = locoSection1;
        this.locoFit1 = locoFit1;
        this.locoSection2 = locoSection2;
        this.locoFit2 = locoFit2;
        this.locoSection3 = locoSection3;
        this.locoFit3 = locoFit3;
        this.locoSection4 = locoSection4;
        this.locoFit4 = locoFit4;
        this.locoUnit = locoUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getLocoSection1() {
        return locoSection1;
    }

    public void setLocoSection1(String locoSection1) {
        this.locoSection1 = locoSection1;
    }

    public String getLocoFit1() {
        return locoFit1;
    }

    public void setLocoFit1(String locoFit1) {
        this.locoFit1 = locoFit1;
    }

    public String getLocoSection2() {
        return locoSection2;
    }

    public void setLocoSection2(String locoSection2) {
        this.locoSection2 = locoSection2;
    }

    public String getLocoFit2() {
        return locoFit2;
    }

    public void setLocoFit2(String locoFit2) {
        this.locoFit2 = locoFit2;
    }

    public String getLocoSection3() {
        return locoSection3;
    }

    public void setLocoSection3(String locoSection3) {
        this.locoSection3 = locoSection3;
    }

    public String getLocoFit3() {
        return locoFit3;
    }

    public void setLocoFit3(String locoFit3) {
        this.locoFit3 = locoFit3;
    }

    public String getLocoSection4() {
        return locoSection4;
    }

    public void setLocoSection4(String locoSection4) {
        this.locoSection4 = locoSection4;
    }

    public String getLocoFit4() {
        return locoFit4;
    }

    public void setLocoFit4(String locoFit4) {
        this.locoFit4 = locoFit4;
    }

    public String getLocoUnit() {
        return locoUnit;
    }

    public void setLocoUnit(String locoUnit) {
        this.locoUnit = locoUnit;
    }
}

