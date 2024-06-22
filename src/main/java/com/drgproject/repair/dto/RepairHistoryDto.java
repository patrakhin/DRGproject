package com.drgproject.repair.dto;

import java.time.LocalDate;

/**
 * DTO for {@link com.drgproject.repair.entity.RepairHistory}
 */
public class RepairHistoryDto {
    private Long id;
    private LocalDate repairDate;
    private String homeDepot;
    private String typeLoco;
    private String locoNumber;
    private String positionRepair;
    private String typeSystem;
    private String employee;
    private String inspectionResult;
    private String workResult;
    private String controlBlockSeal;
    private String powerBlockSeal;
    private String comBlockSeal;
    private String remoteContrSeal;
    private String repairDepot;

    // Default constructor
    public RepairHistoryDto() {
    }

    // Parameterized constructor
    public RepairHistoryDto(String homeDepot, String typeLoco, String locoNumber,
                            String positionRepair, String typeSystem,
                            String employee, String inspectionResult,
                            String workResult, String controlBlockSeal, String powerBlockSeal,
                            String comBlockSeal, String remoteContrSeal, String repairDepot) {
        this.repairDate = LocalDate.now();
        this.homeDepot = homeDepot;
        this.typeLoco = typeLoco;
        this.locoNumber = locoNumber;
        this.positionRepair = positionRepair;
        this.typeSystem = typeSystem;
        this.employee = employee;
        this.inspectionResult = inspectionResult;
        this.workResult = workResult;
        this.controlBlockSeal = controlBlockSeal;
        this.powerBlockSeal = powerBlockSeal;
        this.comBlockSeal = comBlockSeal;
        this.remoteContrSeal = remoteContrSeal;
        this.repairDepot = repairDepot;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(LocalDate repairDate) {
        this.repairDate = repairDate;
    }

    public String getHomeDepot() {
        return homeDepot;
    }

    public void setHomeDepot(String homeDepot) {
        this.homeDepot = homeDepot;
    }

    public String getTypeLoco() {
        return typeLoco;
    }

    public void setTypeLoco(String typeLoco) {
        this.typeLoco = typeLoco;
    }

    public String getLocoNumber() {
        return locoNumber;
    }

    public void setLocoNumber(String locoNumber) {
        this.locoNumber = locoNumber;
    }

    public String getPositionRepair() {
        return positionRepair;
    }

    public void setPositionRepair(String positionRepair) {
        this.positionRepair = positionRepair;
    }

    public String getTypeSystem() {
        return typeSystem;
    }

    public void setTypeSystem(String typeSystem) {
        this.typeSystem = typeSystem;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public String getWorkResult() {
        return workResult;
    }

    public void setWorkResult(String workResult) {
        this.workResult = workResult;
    }

    public String getControlBlockSeal() {
        return controlBlockSeal;
    }

    public void setControlBlockSeal(String controlBlockSeal) {
        this.controlBlockSeal = controlBlockSeal;
    }

    public String getPowerBlockSeal() {
        return powerBlockSeal;
    }

    public void setPowerBlockSeal(String powerBlockSeal) {
        this.powerBlockSeal = powerBlockSeal;
    }

    public String getComBlockSeal() {
        return comBlockSeal;
    }

    public void setComBlockSeal(String comBlockSeal) {
        this.comBlockSeal = comBlockSeal;
    }

    public String getRemoteContrSeal() {
        return remoteContrSeal;
    }

    public void setRemoteContrSeal(String remoteContrSeal) {
        this.remoteContrSeal = remoteContrSeal;
    }

    public String getRepairDepot() {
        return repairDepot;
    }

    public void setRepairDepot(String repairDepot) {
        this.repairDepot = repairDepot;
    }
}
