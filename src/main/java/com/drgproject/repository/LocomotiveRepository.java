package com.drgproject.repository;

import com.drgproject.entity.Locomotive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocomotiveRepository extends JpaRepository<Locomotive, Long> {
    List<Locomotive> findLocomotiveByLocomotiveNumber(String locomotiveNumber);
    Optional<Locomotive> findLocomotiveByLocomotiveNumberAndSection(String locomotiveNumber, String section);
    List<Locomotive> findLocomotiveBySystemType(String systemType);
    List<Locomotive> findLocomotiveByHomeDepot(String homeDepot);
    void deleteLocomotiveByLocomotiveNumberAndSection(String locomotiveNumber, String section);
}
