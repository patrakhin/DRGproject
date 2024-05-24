package com.drgproject.service;


import com.drgproject.entity.LocoBlock;
import com.drgproject.entity.Storage;
import com.drgproject.repository.LocoBlockRepository;
import com.drgproject.repository.StorageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    private final StorageRepository storageRepository;

    private final LocoBlockRepository locoBlockRepository;

    public StorageService(StorageRepository storageRepository, LocoBlockRepository locoBlockRepository) {
        this.storageRepository = storageRepository;
        this.locoBlockRepository = locoBlockRepository;
    }


    public Long getTotalBlockCount(String storageName) {
        return storageRepository.findByName(storageName)
                .map(storage -> locoBlockRepository.countByStorageId(storage.getId()))
                .orElse(0L);
    }

    public void addBlockToStorage(Long storageId, LocoBlock newBlock) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new EntityNotFoundException("Storage not found"));
        newBlock.setStorage(storage);
        locoBlockRepository.save(newBlock);
    }

    // Другие методы...
}

