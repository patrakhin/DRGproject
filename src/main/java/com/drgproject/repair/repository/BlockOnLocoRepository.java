package com.drgproject.repair.repository;

import com.drgproject.repair.entity.BlockOnLoco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockOnLocoRepository extends JpaRepository<BlockOnLoco, Long> {
    Optional<List<BlockOnLoco>> findAllByLocoList_Id(Long id);
}
