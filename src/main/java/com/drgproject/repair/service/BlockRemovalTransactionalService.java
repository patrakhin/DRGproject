package com.drgproject.repair.service;

import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.repository.BlockOnLocoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BlockRemovalTransactionalService {


    private BlockOnLocoRepository blockOnLocoRepository;

    public BlockRemovalTransactionalService(BlockOnLocoRepository blockOnLocoRepository) {
        this.blockOnLocoRepository = blockOnLocoRepository;
    }

    @Transactional
    public void deleteBlockOnLocoByBlNameAneBlNumber(String blockName, String blockNumber) {
        Optional<BlockOnLoco> blockOnLoco = blockOnLocoRepository.findBlockOnLocoByBlockNameAndBlockNumber(blockName, blockNumber);
        BlockOnLoco blockOnLoco1 = blockOnLoco.orElse(null);
        if (blockOnLoco1 == null) {
            throw new IllegalArgumentException("Блок с наименованием " + blockName + " и таким № " + blockNumber + " не найден");
        }
        Long blockId = blockOnLoco1.getId();
        blockOnLocoRepository.deleteById(blockId);
        //blockOnLocoRepository.deleteBlockOnLocoByBlockNameAndBlockNumber(blockName, blockNumber);
    }
}
