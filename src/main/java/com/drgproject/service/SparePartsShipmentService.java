package com.drgproject.service;

import com.drgproject.dto.SparePartsReceiptDto;
import com.drgproject.dto.SparePartsShipmentDto;
import com.drgproject.entity.SparePartsReceipt;
import com.drgproject.entity.SparePartsShipment;
import com.drgproject.entity.Storage;
import com.drgproject.entity.User;
import com.drgproject.repository.SparePartsShipmentRepository;
import com.drgproject.repository.StorageRepository;
import com.drgproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SparePartsShipmentService {
    private final SparePartsShipmentRepository sparePartsShipmentRepository;
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;

    public SparePartsShipmentService(SparePartsShipmentRepository sparePartsShipmentRepository, StorageRepository storageRepository, UserRepository userRepository) {
        this.sparePartsShipmentRepository = sparePartsShipmentRepository;
        this.storageRepository = storageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<SparePartsShipmentDto> getAllSparePart(){
        return sparePartsShipmentRepository.findAll().stream().map(this::convertToDto).toList();
    }

    @Transactional
    public List<SparePartsShipmentDto> getSparePartsShipmentByRegion(String region){
        return sparePartsShipmentRepository.findAll().stream()
                .filter(regions -> Objects.equals(regions.getRegion(), region))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional //Ищем запчасти по наименованию склада - это и есть наименование депо
    public List<SparePartsShipmentDto> getSparePartsShipmentByStorageName(String storageName){
        return sparePartsShipmentRepository.findAll().stream()
                .filter(storages -> Objects.equals(storages.getStorageName(), storageName))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public SparePartsShipmentDto getSparePartsShipmentById(Long id){
        return sparePartsShipmentRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Transactional
    public void deleteSparePartsShipmentById(Long id){
        sparePartsShipmentRepository.deleteById(id);
    }

    //Валидация товара для отгрузки со склада
    @Transactional
    public SparePartsShipmentDto prepareSparePartsShipmentDto(String region, String storageName, String numberTable,
                                                              String sparePartsName, String measure, Long sparePartsNumber,
                                                              String transactionType, double quantity){
        Optional<Storage> storage = storageRepository.findStorageByStorageNameAndStorageRegion(storageName, region);
        User user = userRepository.findByNumberTable(numberTable).orElse(null);

        if(storage.isEmpty()){
            throw new IllegalArgumentException("Склад с наименованием  " + storageName + " не найден");
        }
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с табельным № " + numberTable + " не найден");
        }
        return new SparePartsShipmentDto();
    }

    //Валидация товара для поставки на склад
    @Transactional
    public SparePartsReceiptDto prepareSparePartsReceiptDto(String region, String storageName, String numberTable,
                                                            String sparePartsName, String measure, Long sparePartsNumber, String transactionType, double quantity){
        Optional<Storage> storage = storageRepository.findStorageByStorageNameAndStorageRegion(storageName, region);
        User user = userRepository.findByNumberTable(numberTable).orElse(null);

        if(storage.isEmpty()){
            throw new IllegalArgumentException("Склад с наименованием  " + storageName + " не найден");
        }
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с табельным № " + numberTable + " не найден");
        }

        SparePartsReceiptDto sparePartsReceiptDto = new SparePartsReceiptDto();
        sparePartsReceiptDto.setRegion(region);
        sparePartsReceiptDto.setStorageName(storageName);
        sparePartsReceiptDto.setEmployeeNumber(numberTable);
        sparePartsReceiptDto.setSparePartName(sparePartsName);
        sparePartsReceiptDto.setMeasure(measure);
        sparePartsReceiptDto.setSparePartNumber(sparePartsNumber);
        sparePartsReceiptDto.setTransactionType(transactionType);
        sparePartsReceiptDto.setQuantity(quantity);
        return sparePartsReceiptDto;
    }

    private SparePartsShipmentDto convertToDto(SparePartsShipment sparePartsShipment){
        SparePartsShipmentDto sparePartsShipmentDto = new SparePartsShipmentDto(
                sparePartsShipment.getRegion(),
                sparePartsShipment.getStorageName(),
                sparePartsShipment.getEmployeeNumber(),
                sparePartsShipment.getSparePartName(),
                sparePartsShipment.getMeasure(),
                sparePartsShipment.getSparePartNumber(),
                sparePartsShipment.getTransactionType(),
                sparePartsShipment.getQuantity()
        );
        sparePartsShipmentDto.setId(sparePartsShipment.getId());
        sparePartsShipmentDto.setDateCreate(sparePartsShipment.getDateCreate());
        return sparePartsShipmentDto;
    }

    private SparePartsShipment convertToEntity(SparePartsShipmentDto sparePartsShipmentDto){
        SparePartsShipment sparePartsShipment = new SparePartsShipment();
        sparePartsShipment.setRegion(sparePartsShipmentDto.getRegion());
        sparePartsShipment.setStorageName(sparePartsShipmentDto.getStorageName());
        sparePartsShipment.setEmployeeNumber(sparePartsShipmentDto.getEmployeeNumber());
        sparePartsShipment.setSparePartName(sparePartsShipmentDto.getSparePartName());
        sparePartsShipment.setMeasure(sparePartsShipmentDto.getMeasure());
        sparePartsShipment.setSparePartNumber(sparePartsShipmentDto.getSparePartNumber());
        sparePartsShipment.setTransactionType(sparePartsShipmentDto.getTransactionType());
        sparePartsShipment.setQuantity(sparePartsShipmentDto.getQuantity());
        return sparePartsShipment;
    }
}
