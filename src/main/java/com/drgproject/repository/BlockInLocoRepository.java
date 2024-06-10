package com.drgproject.repository;

import com.drgproject.entity.BlockInLoco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockInLocoRepository extends JpaRepository<BlockInLoco, Long> {
    Optional<BlockInLoco> findBlockInLocoByBlockNumber(String blockInLocoNumber);
    List<BlockInLoco> findBlockInLocoByBlockName(String blockInLockName);
    List<BlockInLoco> findBlockInLocoByBlockStatus(String blockStatus);
}
