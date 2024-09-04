package com.drgproject.repair.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RepairedLocoDTO {
    private LocalDate repairDate;
    private String homeDepot;
    private String typeSystem;
    private String typeLoco;
    private String locoUnit;
    private String positionRepair;
    private long workCount;
    private Object employee; // Изменяем тип на Object

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public RepairedLocoDTO() {}

    public RepairedLocoDTO(LocalDate repairDate, String homeDepot, String typeSystem,
                           String typeLoco, String locoUnit, String positionRepair,
                           long workCount, Object employee) {
        this.repairDate = repairDate;
        this.homeDepot = homeDepot;
        this.typeSystem = typeSystem;
        this.typeLoco = typeLoco;
        this.locoUnit = locoUnit;
        this.positionRepair = positionRepair;
        this.workCount = workCount;
        this.employee = employee;
    }

    public LocalDate getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(LocalDate repairDate) {
        this.repairDate = repairDate;
    }

    public String getRepairDateString() {
        return repairDate != null ? repairDate.format(DATE_FORMATTER) : null;
    }

    public String getHomeDepot() {
        return homeDepot;
    }

    public void setHomeDepot(String homeDepot) {
        this.homeDepot = homeDepot;
    }

    public String getTypeSystem() {
        return typeSystem;
    }

    public void setTypeSystem(String typeSystem) {
        this.typeSystem = typeSystem;
    }

    public String getTypeLoco() {
        return typeLoco;
    }

    public void setTypeLoco(String typeLoco) {
        this.typeLoco = typeLoco;
    }

    public String getLocoUnit() {
        return locoUnit;
    }

    public void setLocoUnit(String locoUnit) {
        this.locoUnit = locoUnit;
    }

    public String getPositionRepair() {
        return positionRepair;
    }

    public void setPositionRepair(String positionRepair) {
        this.positionRepair = positionRepair;
    }

    public long getWorkCount() {
        return workCount;
    }

    public void setWorkCount(long workCount) {
        this.workCount = workCount;
    }

    public Object getEmployee() {
        return employee;
    }

    public void setEmployee(Object employee) {
        this.employee = employee;
    }

    public String getEmployeeString() {
        return employee != null ? employee.toString() : "";
    }
}
