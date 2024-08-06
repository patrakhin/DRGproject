package com.drgproject.repository;

import com.drgproject.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByNumberTable(String numberTable);
    void deleteByNumberTable(String numberTable);
    Optional<Members> findUserByLogin(String login);
    Optional<Members> findUserByFio(String fio);
    Optional<Members> findUserByRegionAndNumberTable(String region, String numberTable);
}
