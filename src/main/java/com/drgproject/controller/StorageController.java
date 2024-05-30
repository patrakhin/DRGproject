package com.drgproject.controller;

import com.drgproject.dto.StorageDto;
import com.drgproject.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storages")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Получение списка всех Storage.
     *
     * @return список всех Storage
     */
    @GetMapping
    public ResponseEntity<List<StorageDto>> getAllStorages() {
        List<StorageDto> storages = storageService.getAllStorages();
        return ResponseEntity.ok(storages);
    }

    /**
     * Получение Storage по его ID.
     *
     * @param id идентификатор Storage
     * @return найденный Storage или статус 404, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<StorageDto> getStorageById(@PathVariable Long id) {
        StorageDto storage = storageService.getStorageById(id);
        if (storage != null) {
            return ResponseEntity.ok(storage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создание нового Storage.
     *
     * @param storageDto DTO с данными для нового Storage
     * @return созданный Storage
     */
    @PostMapping
    public ResponseEntity<StorageDto> createStorage(@RequestBody StorageDto storageDto) {
        StorageDto newStorage = storageService.createStorage(storageDto);
        return ResponseEntity.ok(newStorage);
    }

    /**
     * Обновление существующего Storage.
     *
     * @param id идентификатор обновляемого Storage
     * @param storageDto DTO с новыми данными для Storage
     * @return обновленный Storage или статус 404, если не найден
     */
    @PutMapping("/{id}")
    public ResponseEntity<StorageDto> updateStorage(@PathVariable Long id, @RequestBody StorageDto storageDto) {
        StorageDto updatedStorage = storageService.updateStorage(id, storageDto);
        if (updatedStorage != null) {
            return ResponseEntity.ok(updatedStorage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаление Storage по его ID.
     *
     * @param id идентификатор удаляемого Storage
     * @return статус 204 (No Content), если удаление прошло успешно
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }
}
