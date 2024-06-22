package com.drgproject.repair.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "repair_history")
public class RepairHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repair_date", updatable = false)
    private LocalDate repairDate;

    @Column(name = "home_depot")
    private String homeDepot;

    @Column(name = "type_loco")
    private String typeLoco;

    @Column(name = "loco_number")
    private String locoNumber;

    @Column(name = "position_repair")
    private String positionRepair;

    @Column(name = "type_system")
    private String typeSystem;

    @Column(name = "employee")
    private String employee;

    @Column(name = "inspection_result")
    private String inspectionResult;

    @Column(name = "work_result")
    private String workResult;

    @Column(name = "contr_dlock_seal")
    private String controlBlockSeal;

    @Column(name = "power_block_seal")
    private String powerBlockSeal;

    @Column(name = "com_block_seal")
    private String comBlockSeal;

    @Column(name = "rem_contr_seal")
    private String remoteContrSeal;

    @Column(name = "repair_depot")
    private String repairDepot;

    public RepairHistory() {}

    public RepairHistory(String homeDepot, String typeLoco, String locoNumber,
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
