package com.drgproject.service;

import com.drgproject.dto.ShipmentBlockDto;
import com.drgproject.entity.*;
import com.drgproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ShipmentBlockService {
    private final ShipmentBlockRepository shipmentBlockRepository;
    private final ReceiptBlockRepository receiptBlockRepository;
    private final UserRepository userRepository;
    private final LocoBlockUniqNumService locoBlockUniqNumService;

    public ShipmentBlockService(ShipmentBlockRepository shipmentBlockRepository, ReceiptBlockRepository receiptBlockRepository,
                                UserRepository userRepository, LocoBlockUniqNumService locoBlockUniqNumService) {
        this.shipmentBlockRepository = shipmentBlockRepository;
        this.receiptBlockRepository = receiptBlockRepository;
        this.userRepository = userRepository;
        this.locoBlockUniqNumService = locoBlockUniqNumService;
    }

    @Transactional
    public List<ShipmentBlockDto> getAllShipmentBlock() {
        return shipmentBlockRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public ShipmentBlockDto getShipmentBlockById(Long id) {
        return shipmentBlockRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public List<ShipmentBlockDto> getShipmentBlocksByRegion(String region){
        return shipmentBlockRepository.findShipmentBlockByRegion(region).stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public List<ShipmentBlockDto> getShipmentBlocksByStorageName(String storageName){
        return shipmentBlockRepository.findShipmentBlockByStorageName(storageName).stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public void deleteShipmentBlock(Long id) {
        shipmentBlockRepository.deleteById(id);
    }

    //Это для сущности "Отгрузка"
    @Transactional
    public ShipmentBlockDto shipmentLocoBlockFromStorage(String numberTable,
                                                         String systemType,
                                                         String nameBlock,
                                                         String blockNumber,
                                                         String region) {
        Long getUniqueId = locoBlockUniqNumService.generateUniqueId(systemType, nameBlock, blockNumber);
        Optional<ReceiptBlock> locoBlockByUniqueId = receiptBlockRepository.findReceiptBlockByLocoBlockUniqueId(getUniqueId);
        User user = userRepository.findByNumberTable(numberTable).orElse(null);
        Optional<ShipmentBlock> shippedBlock = shipmentBlockRepository.findShipmentBlockByLocoBlockUniqueId(getUniqueId);
        if (shippedBlock.isPresent()){
            throw new IllegalArgumentException("Блок с № " + blockNumber + " уже отгружен");
        }
        if (user == null){
            throw new IllegalArgumentException("Сотрудник с табельным № " + numberTable + " не найден");
        }

        if (locoBlockByUniqueId.isPresent()) {
            ShipmentBlock shipmentBlock = new ShipmentBlock();
            shipmentBlock.setStorageName(locoBlockByUniqueId.get().getStorageName());
            shipmentBlock.setEmployeeNumber(user.getNumberTable());
            shipmentBlock.setLocoBlockUniqueId(getUniqueId);
            shipmentBlock.setTransactionType("отгружен");
            shipmentBlock.setQuantity(1);
            shipmentBlock.setRegion(region);
            shipmentBlock = shipmentBlockRepository.save(shipmentBlock);
            return convertToDTO(shipmentBlock);
        }
        throw new IllegalArgumentException(" Блок с номером" + blockNumber +  " не найден");
    }


    private ShipmentBlockDto convertToDTO(ShipmentBlock shipmentBlock) {
        ShipmentBlockDto shipmentBlockDto = new ShipmentBlockDto(
                shipmentBlock.getStorageName(),
                shipmentBlock.getEmployeeNumber(),
                shipmentBlock.getSystemType(),
                shipmentBlock.getBlockName(),
                shipmentBlock.getBlockNumber(),
                shipmentBlock.getLocoBlockUniqueId(),
                shipmentBlock.getTransactionType(),
                shipmentBlock.getQuantity(),
                shipmentBlock.getRegion()
        );
        shipmentBlockDto.setId(shipmentBlock.getId());
        shipmentBlockDto.setDateCreate(shipmentBlock.getDateCreate());
        return shipmentBlockDto;
    }

    private ShipmentBlock convertToEntity(ShipmentBlockDto shipmentBlockDto) {
        ShipmentBlock shipmentBlock = new ShipmentBlock();
        shipmentBlock.setStorageName(shipmentBlockDto.getStorageName());
        shipmentBlock.setEmployeeNumber(shipmentBlockDto.getEmployeeNumber());
        shipmentBlock.setLocoBlockUniqueId(shipmentBlockDto.getLocoBlockUniqueId());
        shipmentBlock.setTransactionType(shipmentBlockDto.getTransactionType());
        shipmentBlock.setQuantity(shipmentBlockDto.getQuantity());
        shipmentBlock.setRegion(shipmentBlockDto.getRegion());
        return shipmentBlock;
    }
}
