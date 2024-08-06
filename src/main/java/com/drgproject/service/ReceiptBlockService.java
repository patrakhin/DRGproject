package com.drgproject.service;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.entity.*;
import com.drgproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReceiptBlockService {

    private final ReceiptBlockRepository receiptBlockRepository;
    private final LocoBlockRepository locoBlockRepository;
    private final StorageRepository storageRepository;
    private final ShipmentBlockRepository shipmentBlockRepository;
    private final MemberRepository userRepository;
    private final LocoBlockUniqNumService locoBlockUniqNumService;
    private final ShipmentBlockService shipmentBlockService;

    public ReceiptBlockService(ReceiptBlockRepository receiptBlockRepository,
                               LocoBlockRepository locoBlockRepository,
                               StorageRepository storageRepository,
                               ShipmentBlockRepository shipmentBlockRepository,
                               MemberRepository userRepository,
                               LocoBlockUniqNumService locoBlockUniqNumService,
                               ShipmentBlockService shipmentBlockService) {
        this.receiptBlockRepository = receiptBlockRepository;
        this.locoBlockRepository = locoBlockRepository;
        this.storageRepository = storageRepository;
        this.shipmentBlockRepository = shipmentBlockRepository;
        this.userRepository = userRepository;
        this.locoBlockUniqNumService = locoBlockUniqNumService;
        this.shipmentBlockService = shipmentBlockService;
    }

    @Transactional
    public List<ReceiptBlockDto> getAllReceiptBlock() {
        return receiptBlockRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public List<ReceiptBlockDto> getReceiptBlocksByRegion(String region){
        return receiptBlockRepository.findAll().stream()
                .filter(regions -> Objects.equals(regions.getRegion(), region))
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional //Ищем блоки по наименованию склада - это и есть наименование депо
    public List<ReceiptBlockDto> getReceiptBlocksByStorageName(String storageName){
        return  receiptBlockRepository.findAll().stream()
                .filter(storages -> Objects.equals(storages.getStorageName(), storageName))
                .map(this::convertToDTO)
                .toList();
    }


    // Метод для получения всех остатков блоков на складах регионов
    @Transactional
    public List<ReceiptBlockDto> getRemainingReceiptBlocks() {
        // Получаем все блоки на складе
        List<ReceiptBlock> allReceiptBlocks = receiptBlockRepository.findAll();
        // Получаем все отгруженные блоки
        List<Long> shippedBlockIds = shipmentBlockRepository.findAll().stream()
                .map(ShipmentBlock::getLocoBlockUniqueId)
                .toList();
        // Фильтруем блоки, которые находятся на складе и не были отгружены
        return allReceiptBlocks.stream()
                .filter(receiptBlock -> !shippedBlockIds.contains(receiptBlock.getLocoBlockUniqueId()))
                .map(this::convertToDTO)
                .toList();
    }

    // Метод для получения остатков блоков по региону
    @Transactional
    public List<ReceiptBlockDto> getRemainingReceiptBlocksByRegion(String region) {
        List<ReceiptBlock> allReceiptBlocks = receiptBlockRepository.findReceiptBlockByRegion(region);
        List<Long> shippedBlockIds = shipmentBlockRepository.findAll().stream()
                .map(ShipmentBlock::getLocoBlockUniqueId)
                .toList();

        return allReceiptBlocks.stream()
                .filter(receiptBlock -> !shippedBlockIds.contains(receiptBlock.getLocoBlockUniqueId()))
                .map(this::convertToDTO)
                .toList();
    }

    // Метод для получения остатков всех типов блоков по складу
    @Transactional
    public List<ReceiptBlockDto> getRemainingReceiptBlocksByStorageName(String storageName) {
        List<ReceiptBlock> allReceiptBlocks = receiptBlockRepository.findReceiptBlockByStorageName(storageName);
        List<Long> shippedBlockIds = shipmentBlockRepository.findAll().stream()
                .map(ShipmentBlock::getLocoBlockUniqueId)
                .toList();

        return allReceiptBlocks.stream()
                .filter(receiptBlock -> !shippedBlockIds.contains(receiptBlock.getLocoBlockUniqueId()))
                .map(this::convertToDTO)
                .toList();
    }

    // Метод для получения остатков всех типов блоков по складу для конкретного типа системы
    @Transactional
    public List<ReceiptBlockDto> getReceiptBlocksByStorageNameAndTypeSystem(String storageName, String typeSystem){
        List<ReceiptBlock> allReceiptBlocks = receiptBlockRepository.findReceiptBlockByStorageName(storageName);
        List<Long> shippedBlockIds = shipmentBlockRepository.findAll().stream()
                .map(ShipmentBlock::getLocoBlockUniqueId)
                .toList();

        return allReceiptBlocks.stream()
                .filter(receiptBlock -> !shippedBlockIds.contains(receiptBlock.getLocoBlockUniqueId()))
                .filter(typeBlocks -> typeSystem.equals(typeBlocks.getSystemType()))
                .map(this::convertToDTO)
                .toList();
    }


    @Transactional
    public ReceiptBlockDto getReceiptBlockById(Long id) {
        return receiptBlockRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public void deleteReceiptBlock(Long id) {
        receiptBlockRepository.deleteById(id);
    }

    @Transactional
    public ReceiptBlockDto prepareReceiptBlockDto(String storageName, String region, String numberTable,
                                                  String systemType, String nameBlock, String blockNumber) {
        Optional<Storage> storage = storageRepository.findStorageByStorageNameAndStorageRegion(storageName, region);
        Long getUniqueId = locoBlockUniqNumService.generateUniqueId(systemType, nameBlock, blockNumber);
        Optional<LocoBlock> locoBlockByUniqueId = locoBlockRepository.findLocoBlockByUniqueId(getUniqueId);
        Optional<ReceiptBlock> receiptBlock = receiptBlockRepository.findReceiptBlockByLocoBlockUniqueId(getUniqueId);

        Members user = userRepository.findByNumberTable(numberTable).orElse(null);

        if(receiptBlock.isPresent()){
            throw new IllegalArgumentException("Блок с номером  " + blockNumber + " уже на складе");
        }
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с табельным № " + numberTable + " не найден");
        }

        if (storage.isPresent() && locoBlockByUniqueId.isPresent()) {
            ReceiptBlockDto receiptBlockDto = new ReceiptBlockDto();
            receiptBlockDto.setStorageName(storage.get().getStorageName());
            receiptBlockDto.setRegion(storage.get().getStorageRegion());
            receiptBlockDto.setEmployeeNumber(user.getNumberTable());
            receiptBlockDto.setSystemType(systemType);
            receiptBlockDto.setBlockName(nameBlock);
            receiptBlockDto.setBlockNumber(blockNumber);
            receiptBlockDto.setLocoBlockUniqueId(getUniqueId);
            receiptBlockDto.setTransactionType("на складе");
            receiptBlockDto.setQuantity(1);

            return receiptBlockDto;
        }

        throw new IllegalArgumentException("Склад " + storageName +
                " Блок с номером " + blockNumber +  " не найден");
    }

    @Transactional
    public ReceiptBlockDto addLocoBlockToReceipt(ReceiptBlockDto receiptBlockDto) {
        ReceiptBlock receiptBlock = new ReceiptBlock();
        receiptBlock.setStorageName(receiptBlockDto.getStorageName());
        receiptBlock.setRegion(receiptBlockDto.getRegion());
        receiptBlock.setEmployeeNumber(receiptBlockDto.getEmployeeNumber());
        receiptBlock.setSystemType(receiptBlockDto.getSystemType());
        receiptBlock.setBlockName(receiptBlockDto.getBlockName());
        receiptBlock.setBlockNumber(receiptBlockDto.getBlockNumber());
        receiptBlock.setLocoBlockUniqueId(receiptBlockDto.getLocoBlockUniqueId());
        receiptBlock.setTransactionType(receiptBlockDto.getTransactionType());
        receiptBlock.setQuantity(receiptBlockDto.getQuantity());
        receiptBlock = receiptBlockRepository.save(receiptBlock);

        return convertToDTO(receiptBlock);
    }

    private ReceiptBlockDto convertToDTO(ReceiptBlock receiptBlock) {
        ReceiptBlockDto receiptBlockDto =  new ReceiptBlockDto(
                receiptBlock.getStorageName(),
                receiptBlock.getRegion(),
                receiptBlock.getEmployeeNumber(),
                receiptBlock.getSystemType(),
                receiptBlock.getBlockName(),
                receiptBlock.getBlockNumber(),
                receiptBlock.getLocoBlockUniqueId(),
                receiptBlock.getTransactionType(),
                receiptBlock.getQuantity()
        );
        receiptBlockDto.setId(receiptBlock.getId());
        receiptBlockDto.setDateCreate(receiptBlock.getDateCreate());
        return receiptBlockDto;
    }

    private ReceiptBlock convertToEntity(ReceiptBlockDto receiptBlockDto) {
        ReceiptBlock receiptBlock = new ReceiptBlock();
        receiptBlock.setStorageName(receiptBlockDto.getStorageName());
        receiptBlock.setRegion(receiptBlock.getRegion());
        receiptBlock.setEmployeeNumber(receiptBlockDto.getEmployeeNumber());
        receiptBlock.setSystemType(receiptBlockDto.getSystemType());
        receiptBlock.setBlockName(receiptBlockDto.getBlockName());
        receiptBlock.setBlockNumber(receiptBlockDto.getBlockNumber());
        receiptBlock.setLocoBlockUniqueId(receiptBlockDto.getLocoBlockUniqueId());
        receiptBlock.setTransactionType(receiptBlockDto.getTransactionType());
        receiptBlock.setQuantity(receiptBlockDto.getQuantity());
        return receiptBlock;
    }
}
