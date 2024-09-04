package com.drgproject.repair.repository;

import com.drgproject.repair.dto.RepairedLocoDTO;
import com.drgproject.repair.entity.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long> {

    Optional<List<RepairHistory>> findRepairHistoriesByTypeLocoAndLocoNumber(String typeLoco, String locoNumber);
    Optional<RepairHistory> findRepairHistoriesByTypeLocoAndLocoNumberAndRepairDate(String typeLoco, String locoNumber, LocalDate repairDate);
    void deleteRepairHistoryByTypeLocoAndLocoNumberAndRepairDate(String typeLoco, String locoNumber, LocalDate repairDate);
    // Метод для подсчета количества записей по типу и номеру локомотива
    int countByTypeLocoAndLocoNumber(String typeLoco, String numberLoco);

    // Общий отчет по всем Регионам и депо
    @Query("SELECT new com.drgproject.repair.dto.RepairedLocoDTO(" +
            "rh.repairDate, li.homeDepot, rh.typeSystem, li.locoType, li.locoUnit, rh.positionRepair, " +
            "COUNT(rh), STRING_AGG(rh.employee, ', ')) " +
            "FROM RepairHistory rh " +
            "JOIN LocoInfo li ON rh.homeDepot = li.homeDepot " +
            "WHERE rh.locoNumber IN (li.locoSection1, li.locoSection2, li.locoSection3, li.locoSection4) " +
            "GROUP BY li.locoUnit, rh.repairDate, li.homeDepot, rh.typeSystem, rh.positionRepair, li.locoType")
    List<RepairedLocoDTO> findAllRepairedLocos();

    // Метод для отчета с учетом типа системы "СПСТ"
    @Query("SELECT new com.drgproject.repair.dto.RepairedLocoDTO(" +
            "rh.repairDate, li.homeDepot, rh.typeSystem, li.locoType, li.locoUnit, rh.positionRepair, " +
            "COUNT(rh), STRING_AGG(rh.employee, ', ')) " +
            "FROM RepairHistory rh " +
            "JOIN LocoInfo li ON rh.homeDepot = li.homeDepot " +
            "WHERE rh.locoNumber IN (li.locoSection1, li.locoSection2, li.locoSection3, li.locoSection4) " +
            "AND rh.repairDepot = :repairDepot " +
            "AND rh.typeSystem = 'СПСТ Эл4-04' " +
            "GROUP BY li.locoUnit, rh.repairDate, li.homeDepot, rh.typeSystem, rh.positionRepair, li.locoType")
    List<RepairedLocoDTO> findRepairedLocosWithTypeSPS(String repairDepot);

    // Метод для отчета без учета типа системы "СПСТ"
    @Query("SELECT new com.drgproject.repair.dto.RepairedLocoDTO(" +
            "rh.repairDate, li.homeDepot, rh.typeSystem, li.locoType, li.locoUnit, rh.positionRepair, " +
            "COUNT(rh), STRING_AGG(rh.employee, ', ')) " +
            "FROM RepairHistory rh " +
            "JOIN LocoInfo li ON rh.homeDepot = li.homeDepot " +
            "WHERE rh.locoNumber IN (li.locoSection1, li.locoSection2, li.locoSection3, li.locoSection4) " +
            "AND rh.repairDepot = :repairDepot " +
            "AND rh.typeSystem <> 'СПСТ Эл4-04' " +
            "GROUP BY li.locoUnit, rh.repairDate, li.homeDepot, rh.typeSystem, rh.positionRepair, li.locoType")
    List<RepairedLocoDTO> findRepairedLocosWithoutTypeSPS(String repairDepot);

    // Метод для отчета с учетом типа системы "СПСТ" и фильтрацией по дате
    @Query("SELECT new com.drgproject.repair.dto.RepairedLocoDTO(" +
            "rh.repairDate, li.homeDepot, rh.typeSystem, li.locoType, li.locoUnit, rh.positionRepair, " +
            "COUNT(rh), STRING_AGG(rh.employee, ', ')) " +
            "FROM RepairHistory rh " +
            "JOIN LocoInfo li ON rh.homeDepot = li.homeDepot " +
            "WHERE rh.locoNumber IN (li.locoSection1, li.locoSection2, li.locoSection3, li.locoSection4) " +
            "AND rh.repairDepot = :repairDepot " +
            "AND rh.typeSystem = 'СПСТ Эл4-04' " +
            "AND rh.repairDate BETWEEN :startDate AND :endDate " +
            "GROUP BY li.locoUnit, rh.repairDate, li.homeDepot, rh.typeSystem, rh.positionRepair, li.locoType")
    List<RepairedLocoDTO> findRepairedLocosWithTypeSPS(
            @Param("repairDepot") String repairDepot,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Метод для отчета с учетом без системы "СПСТ" и фильтрацией по дате
    @Query("SELECT new com.drgproject.repair.dto.RepairedLocoDTO(" +
            "rh.repairDate, li.homeDepot, rh.typeSystem, li.locoType, li.locoUnit, rh.positionRepair, " +
            "COUNT(rh), STRING_AGG(rh.employee, ', ')) " +
            "FROM RepairHistory rh " +
            "JOIN LocoInfo li ON rh.homeDepot = li.homeDepot " +
            "WHERE rh.locoNumber IN (li.locoSection1, li.locoSection2, li.locoSection3, li.locoSection4) " +
            "AND rh.repairDepot = :repairDepot " +
            "AND rh.typeSystem <> 'СПСТ Эл4-04' " +
            "AND rh.repairDate BETWEEN :startDate AND :endDate " +
            "GROUP BY li.locoUnit, rh.repairDate, li.homeDepot, rh.typeSystem, rh.positionRepair, li.locoType")
    List<RepairedLocoDTO> findRepairedLocosWithoutTypeSPS(
            @Param("repairDepot") String repairDepot,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
