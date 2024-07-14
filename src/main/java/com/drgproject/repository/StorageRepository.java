package com.drgproject.repository;

import com.drgproject.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Optional<Storage> findStorageByStorageName(String storageName);
    Optional<Storage> findStorageByStorageNameAndStorageRegion(String storageName, String storageRegion);
    Optional<List<Storage>> findStorageByStorageRegion(String region);
    void deleteStorageByStorageName(String storageName);
}