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
    private String block1Seal;
    private String block2Seal;
    private String block3Seal;
    private String block4Seal;
    private String block5Seal;
    private String block6Seal;
    private String block7Seal;
    private String block8Seal;
    private String block9Seal;
    private String block10Seal;
    private String repairDepot;

    // Default constructor
    public RepairHistoryDto() {
    }

    // Parameterized constructor
    public RepairHistoryDto(String homeDepot, String typeLoco, String locoNumber,
                            String positionRepair, String typeSystem,
                            String employee, String inspectionResult,
                            String workResult, String block1Seal, String block2Seal,
                            String block3Seal, String block4Seal, String block5Seal,
                            String block6Seal, String block7Seal, String block8Seal,
                            String block9Seal, String block10Seal, String repairDepot) {
        this.repairDate = LocalDate.now();
        this.homeDepot = homeDepot;
        this.typeLoco = typeLoco;
        this.locoNumber = locoNumber;
        this.positionRepair = positionRepair;
        this.typeSystem = typeSystem;
        this.employee = employee;
        this.inspectionResult = inspectionResult;
        this.workResult = workResult;
        this.block1Seal = block1Seal;
        this.block2Seal = block2Seal;
        this.block3Seal = block3Seal;
        this.block4Seal = block4Seal;
        this.block5Seal = block5Seal;
        this.block6Seal = block6Seal;
        this.block7Seal = block7Seal;
        this.block8Seal = block8Seal;
        this.block9Seal = block9Seal;
        this.block10Seal = block10Seal;
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

    public String getBlock1Seal() {
        return block1Seal;
    }

    public void setBlock1Seal(String block1Seal) {
        this.block1Seal = block1Seal;
    }

    public String getBlock2Seal() {
        return block2Seal;
    }

    public void setBlock2Seal(String block2Seal) {
        this.block2Seal = block2Seal;
    }

    public String getBlock3Seal() {
        return block3Seal;
    }

    public void setBlock3Seal(String block3Seal) {
        this.block3Seal = block3Seal;
    }

    public String getBlock4Seal() {
        return block4Seal;
    }

    public void setBlock4Seal(String block4Seal) {
        this.block4Seal = block4Seal;
    }

    public String getBlock5Seal() {
        return block5Seal;
    }

    public void setBlock5Seal(String block5Seal) {
        this.block5Seal = block5Seal;
    }

    public String getBlock6Seal() {
        return block6Seal;
    }

    public void setBlock6Seal(String block6Seal) {
        this.block6Seal = block6Seal;
    }

    public String getBlock7Seal() {
        return block7Seal;
    }

    public void setBlock7Seal(String block7Seal) {
        this.block7Seal = block7Seal;
    }

    public String getBlock8Seal() {
        return block8Seal;
    }

    public void setBlock8Seal(String block8Seal) {
        this.block8Seal = block8Seal;
    }

    public String getBlock9Seal() {
        return block9Seal;
    }

    public void setBlock9Seal(String block9Seal) {
        this.block9Seal = block9Seal;
    }

    public String getBlock10Seal() {
        return block10Seal;
    }

    public void setBlock10Seal(String block10Seal) {
        this.block10Seal = block10Seal;
    }

    public String getRepairDepot() {
        return repairDepot;
    }

    public void setRepairDepot(String repairDepot) {
        this.repairDepot = repairDepot;
    }
}
