package com.drgproject.service;

import com.drgproject.dto.StorageDto;
import com.drgproject.entity.Storage;
import com.drgproject.repository.StorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StorageService {

    StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Transactional
    public List<StorageDto> getAllStorages() {
        return storageRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public StorageDto getStorageById(Long id) {
        return storageRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public StorageDto getStorageByName(String storageName) {
        return storageRepository.findStorageByStorageName(storageName).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public StorageDto createStorage(StorageDto storageDto) {
        Storage storage = convertToEntity(storageDto);
        storage = storageRepository.save(storage);
        return convertToDTO(storage);
    }

    @Transactional
    public StorageDto updateStorage(Long id, StorageDto storageDto) {
        Optional<Storage> optionalStorage = storageRepository.findById(id);
        if (optionalStorage.isPresent()) {
            Storage storage = optionalStorage.get();
            storage.setStorageName(storageDto.getStorageName());
            storage.setDateCreate(storageDto.getDateCreate());
            storage = storageRepository.save(storage);
            return convertToDTO(storage);
        }
        return null;
    }

    @Transactional
    public void deleteStorage(Long id) {
        storageRepository.deleteById(id);
    }

    private StorageDto convertToDTO(Storage storage) {
        StorageDto storageDto = new StorageDto(
                storage.getStorageName(),
                storage.getDateCreate()
        );
        storageDto.setId(storage.getId());
        return storageDto;
    }

    private Storage convertToEntity(StorageDto storageDto) {
        return new Storage(
                storageDto.getStorageName(),
                storageDto.getDateCreate()
        );
    }
}
