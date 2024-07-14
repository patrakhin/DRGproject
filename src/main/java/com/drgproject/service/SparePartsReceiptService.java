package com.drgproject.service;

import com.drgproject.dto.SparePartsReceiptDto;
import com.drgproject.entity.*;
import com.drgproject.repository.SparePartsReceiptRepository;
import com.drgproject.repository.StorageRepository;
import com.drgproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SparePartsReceiptService {
    private final SparePartsReceiptRepository sparePartsReceiptRepository;
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;

    public SparePartsReceiptService(SparePartsReceiptRepository sparePartsReceiptRepository, StorageRepository storageRepository, UserRepository userRepository) {
        this.sparePartsReceiptRepository = sparePartsReceiptRepository;
        this.storageRepository = storageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<SparePartsReceiptDto> getAllSparePart(){
        return sparePartsReceiptRepository.findAll().stream().map(this::convertToDto).toList();
    }

    @Transactional
    public List<SparePartsReceiptDto> getSparePartsReceiptByRegion(String region){
        return sparePartsReceiptRepository.findAll().stream()
                .filter(regions -> Objects.equals(regions.getRegion(), region))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional //Ищем запчасти по наименованию склада - это и есть наименование депо
    public List<SparePartsReceiptDto> getSparePartsReceiptByStorageName(String storageName){
        return sparePartsReceiptRepository.findAll().stream()
                .filter(storages -> Objects.equals(storages.getStorageName(), storageName))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public SparePartsReceiptDto getSparePartsReceiptById(Long id){
        return sparePartsReceiptRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    //Остатки на складе по типу транзакции "на складе"
    @Transactional
    public List<SparePartsReceiptDto> getAllSparePartsStock() {
        return sparePartsReceiptRepository.findAll().stream()
                .filter(stocks -> "на складе".equals(stocks.getTransactionType()))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public List<SparePartsReceiptDto> getAllSparePartsStockByRegion(String region){
        return sparePartsReceiptRepository.findAll().stream()
                .filter(regions -> Objects.equals(regions.getRegion(), region))
                .filter(stocks -> "на складе".equals(stocks.getTransactionType()))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public List<SparePartsReceiptDto> getAllSparePartsStockByStorageName(String storageName){
        return sparePartsReceiptRepository.findAll().stream()
                .filter(storages -> Objects.equals(storages.getStorageName(), storageName))
                .filter(stocks -> "на складе".equals(stocks.getTransactionType()))
                .map(this::convertToDto)
                .toList();
    }

    //Расход зап частей по типу  "отгружено"
    @Transactional
    public List<SparePartsReceiptDto> getAllSparePartsShipped() {
        return sparePartsReceiptRepository.findAll().stream()
                .filter(stocks -> "отгружено".equals(stocks.getTransactionType()))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public List<SparePartsReceiptDto> getAllSparePartsShippedByRegion(String region){
        return sparePartsReceiptRepository.findAll().stream()
                .filter(regions -> Objects.equals(regions.getRegion(), region))
                .filter(stocks -> "отгружено".equals(stocks.getTransactionType()))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public List<SparePartsReceiptDto> getAllSparePartsShippedByStorageName(String storageName){
        return sparePartsReceiptRepository.findAll().stream()
                .filter(storages -> Objects.equals(storages.getStorageName(), storageName))
                .filter(stocks -> "отгружено".equals(stocks.getTransactionType()))
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public void deleteSparePartsReceiptById(Long id){
        sparePartsReceiptRepository.deleteById(id);
    }

    @Transactional
    public void deleteSparePartsStockById(Long id){
        List <SparePartsReceiptDto>  sparePartsReceiptForDelete = sparePartsReceiptRepository.findAll().stream()
                .filter(delStock -> "отгружено".equals(delStock.getTransactionType()))
                .filter(delStock -> id.equals(delStock.getId()))
                .map(this::convertToDto)
                .toList();
        SparePartsReceiptDto sparePartsReceiptDto = sparePartsReceiptForDelete.get(0);
        String sparePartsName = sparePartsReceiptDto.getSparePartName();
        Long sparePartsNumber = sparePartsReceiptDto.getSparePartNumber();
        double quantity = sparePartsReceiptDto.getQuantity();
        String transactionType = "на складе";
        SparePartsReceipt sparePartsReceiptInStorage = sparePartsReceiptRepository
                .findSparePartsReceiptBySparePartNameAndSparePartNumberAndTransactionType(sparePartsName, sparePartsNumber, transactionType);
        sparePartsReceiptInStorage.setQuantity(sparePartsReceiptInStorage.getQuantity() + quantity);
        sparePartsReceiptRepository.save(sparePartsReceiptInStorage);
        sparePartsReceiptRepository.deleteById(id);
    }

    //Валидация Списание со склада
    @Transactional
    public SparePartsReceiptDto prepareWriteOffSparePartDto(String region, String storageName, String numberTable,
                                                            String sparePartsName, String measure, Long sparePartsNumber, String transactionType, double quantity){
        Optional<Storage> storage = storageRepository.findStorageByStorageNameAndStorageRegion(storageName, region);
        User user = userRepository.findByNumberTable(numberTable).orElse(null);
        Optional<List<SparePartsReceipt>> sparePartsReceiptInStorage = sparePartsReceiptRepository.findSparePartsReceiptBySparePartNameAndSparePartNumber(sparePartsName, sparePartsNumber);
        // Фильтруем только те записи, которые находятся "на складе"
        List<SparePartsReceipt> filteredSparePartsReceipts = Collections.emptyList();
        if (sparePartsReceiptInStorage.get().size() > 0) {
            filteredSparePartsReceipts = sparePartsReceiptInStorage.get().stream()
                    .filter(spareParts -> "на складе".equals(spareParts.getTransactionType()))
                    .toList();
        }
        if(storage.isEmpty()){
            throw new IllegalArgumentException("Склад с наименованием  " + storageName + " не найден");
        }
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с табельным № " + numberTable + " не найден");
        }
        if (filteredSparePartsReceipts.isEmpty()){
            throw new IllegalArgumentException("Зап.часть с наименованием  " + sparePartsName + " и номером " + sparePartsNumber + " не найдена");
        }
        SparePartsReceipt sparePartsReceipt = filteredSparePartsReceipts.get(0); // Берем первый найденный элемент на складе
        if (sparePartsReceipt.getQuantity() == 0){
            throw new IllegalArgumentException("Зап.часть с наименованием  " + sparePartsName + " и номером " + sparePartsNumber + " остаток 0");
        }
        if (sparePartsReceipt.getQuantity() < quantity){
            throw new IllegalArgumentException("Зап.часть с наименованием  " + sparePartsName + " и номером " + sparePartsNumber + " остаток меньше, и равен " + sparePartsReceipt.getQuantity());
        }
        if(sparePartsReceipt.getQuantity() >= quantity){
            sparePartsReceipt.setQuantity(sparePartsReceipt.getQuantity()-quantity); //Списываем нужное количество товара
            sparePartsReceiptRepository.save(sparePartsReceipt);
        }
        SparePartsReceiptDto sparePartsReceiptDto = new SparePartsReceiptDto();
        sparePartsReceiptDto.setRegion(region);
        sparePartsReceiptDto.setStorageName(storageName);
        sparePartsReceiptDto.setEmployeeNumber(numberTable);
        sparePartsReceiptDto.setSparePartName(sparePartsName);
        sparePartsReceiptDto.setMeasure(measure);
        sparePartsReceiptDto.setSparePartNumber(sparePartsNumber);
        sparePartsReceiptDto.setTransactionType("отгружено"); //Отгружено
        sparePartsReceiptDto.setQuantity(quantity);
        return sparePartsReceiptDto;
    }

    //Списание со склада
    @Transactional
    public SparePartsReceiptDto writeOffSparePartDto(SparePartsReceiptDto sparePartsReceiptDto){
        SparePartsReceipt sparePartsReceipt = new SparePartsReceipt();
        sparePartsReceipt.setRegion(sparePartsReceiptDto.getRegion());
        sparePartsReceipt.setStorageName(sparePartsReceiptDto.getStorageName());
        sparePartsReceipt.setEmployeeNumber(sparePartsReceiptDto.getEmployeeNumber());
        sparePartsReceipt.setSparePartName(sparePartsReceiptDto.getSparePartName());
        sparePartsReceipt.setMeasure(sparePartsReceiptDto.getMeasure());
        sparePartsReceipt.setSparePartNumber(sparePartsReceiptDto.getSparePartNumber());
        sparePartsReceipt.setTransactionType(sparePartsReceiptDto.getTransactionType());
        sparePartsReceipt.setQuantity(sparePartsReceiptDto.getQuantity());
        sparePartsReceipt = sparePartsReceiptRepository.save(sparePartsReceipt);
        return convertToDto(sparePartsReceipt);
    }

    //Валидация товара для поставки на склад
    @Transactional
    public SparePartsReceiptDto prepareSparePartsReceiptDto(String region, String storageName, String numberTable,
                                                            String sparePartsName, String measure, Long sparePartsNumber, String transactionType, double quantity){
        Optional<Storage> storage = storageRepository.findStorageByStorageNameAndStorageRegion(storageName, region);
        User user = userRepository.findByNumberTable(numberTable).orElse(null);
        Optional<List<SparePartsReceipt>> sparePartsReceiptInStorage = sparePartsReceiptRepository.findSparePartsReceiptBySparePartNameAndSparePartNumber(sparePartsName, sparePartsNumber);
        if(storage.isEmpty()){
            throw new IllegalArgumentException("Склад с наименованием  " + storageName + " не найден");
        }
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с табельным № " + numberTable + " не найден");
        }
        if (sparePartsReceiptInStorage.get().size() == 0){ // Если такого товара нет, заводим на склад
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

        List<SparePartsReceipt> sparePartsReceiptsInStorage = sparePartsReceiptInStorage.get().stream()
                .filter(spareParts -> "на складе".equals(spareParts.getTransactionType()))
                .toList();

        SparePartsReceipt sparePartsReceipt = sparePartsReceiptsInStorage.get(0);
        sparePartsReceipt.setQuantity(sparePartsReceipt.getQuantity() + quantity); //Такой товар есть, выполнили пополнение
        sparePartsReceipt.setTransactionType("пополнено");
        sparePartsReceiptRepository.save(sparePartsReceipt);
        return convertToDto(sparePartsReceipt);

    }

    //Поставка товара на склад
    @Transactional
    public SparePartsReceiptDto addSparePartsToReceipt(SparePartsReceiptDto sparePartsReceiptDto){
        if (sparePartsReceiptDto.getTransactionType().equals("пополнено")){
            SparePartsReceipt sparePartsReceipt = sparePartsReceiptRepository.findSparePartsReceiptBySparePartNameAndSparePartNumberAndTransactionType(sparePartsReceiptDto.getSparePartName(),
                    sparePartsReceiptDto.getSparePartNumber(), sparePartsReceiptDto.getTransactionType());
            sparePartsReceipt.setTransactionType("на складе");
            sparePartsReceiptRepository.save(sparePartsReceipt);
            return sparePartsReceiptDto;
        }
        SparePartsReceipt sparePartsReceipt = new SparePartsReceipt();
        sparePartsReceipt.setRegion(sparePartsReceiptDto.getRegion());
        sparePartsReceipt.setStorageName(sparePartsReceiptDto.getStorageName());
        sparePartsReceipt.setEmployeeNumber(sparePartsReceiptDto.getEmployeeNumber());
        sparePartsReceipt.setSparePartName(sparePartsReceiptDto.getSparePartName());
        sparePartsReceipt.setMeasure(sparePartsReceiptDto.getMeasure());
        sparePartsReceipt.setSparePartNumber(sparePartsReceiptDto.getSparePartNumber());
        sparePartsReceipt.setTransactionType(sparePartsReceiptDto.getTransactionType());
        sparePartsReceipt.setQuantity(sparePartsReceiptDto.getQuantity());
        sparePartsReceipt = sparePartsReceiptRepository.save(sparePartsReceipt);
        return convertToDto(sparePartsReceipt);
    }

    private SparePartsReceiptDto convertToDto(SparePartsReceipt sparePartsReceipt){
        SparePartsReceiptDto sparePartsReceiptDto = new SparePartsReceiptDto(
                sparePartsReceipt.getRegion(),
                sparePartsReceipt.getStorageName(),
                sparePartsReceipt.getEmployeeNumber(),
                sparePartsReceipt.getSparePartName(),
                sparePartsReceipt.getMeasure(),
                sparePartsReceipt.getSparePartNumber(),
                sparePartsReceipt.getTransactionType(),
                sparePartsReceipt.getQuantity()
        );
        sparePartsReceiptDto.setId(sparePartsReceipt.getId());
        sparePartsReceiptDto.setDateCreate(sparePartsReceipt.getDateCreate());
        return sparePartsReceiptDto;
    }

    private SparePartsReceipt convertToEntity(SparePartsReceiptDto sparePartsReceiptDto){
        SparePartsReceipt sparePartsReceipt = new SparePartsReceipt();
        sparePartsReceipt.setRegion(sparePartsReceiptDto.getRegion());
        sparePartsReceipt.setStorageName(sparePartsReceiptDto.getStorageName());
        sparePartsReceipt.setEmployeeNumber(sparePartsReceiptDto.getEmployeeNumber());
        sparePartsReceipt.setSparePartName(sparePartsReceiptDto.getSparePartName());
        sparePartsReceipt.setMeasure(sparePartsReceiptDto.getMeasure());
        sparePartsReceipt.setSparePartNumber(sparePartsReceiptDto.getSparePartNumber());
        sparePartsReceipt.setTransactionType(sparePartsReceiptDto.getTransactionType());
        sparePartsReceipt.setQuantity(sparePartsReceiptDto.getQuantity());
        return sparePartsReceipt;
    }
}
