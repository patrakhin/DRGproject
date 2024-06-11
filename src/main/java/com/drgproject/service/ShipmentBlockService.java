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
    private final EmployeeRepository employeeRepository;
    private final LocoBlockUniqNumService locoBlockUniqNumService;

    public ShipmentBlockService(ShipmentBlockRepository shipmentBlockRepository, ReceiptBlockRepository receiptBlockRepository,
                                EmployeeRepository employeeRepository, LocoBlockUniqNumService locoBlockUniqNumService) {
        this.shipmentBlockRepository = shipmentBlockRepository;
        this.receiptBlockRepository = receiptBlockRepository;
        this.employeeRepository = employeeRepository;
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
    public void deleteShipmentBlock(Long id) {
        shipmentBlockRepository.deleteById(id);
    }



    //Это для сущности "Отгрузка"
    @Transactional
    public ShipmentBlockDto shipmentLocoBlockFromStorage(String numberTable,
                                                         String systemType,
                                                         String nameBlock,
                                                         String blockNumber) {
        Long getUniqueId = locoBlockUniqNumService.generateUniqueId(systemType, nameBlock, blockNumber);
        Optional<ReceiptBlock> locoBlockByUniqueId = receiptBlockRepository.findReceiptBlockByLocoBlockUniqueId(getUniqueId);
        Employee employee = employeeRepository.findEmployeeByNumberTable(numberTable).orElse(null);
        if (employee == null){
            throw new IllegalArgumentException("Employee with number " + numberTable + " not found");
        }

        if (locoBlockByUniqueId.isPresent()) {
            ShipmentBlock shipmentBlock = new ShipmentBlock();
            shipmentBlock.setStorageName(locoBlockByUniqueId.get().getStorageName());
            shipmentBlock.setEmployeeNumber(employee.getNumberTable());
            shipmentBlock.setLocoBlockUniqueId(getUniqueId);
            shipmentBlock.setTransactionType("отгружен");
            shipmentBlock.setQuantity(1);
            shipmentBlock = shipmentBlockRepository.save(shipmentBlock);
            return convertToDTO(shipmentBlock);
        }
        throw new IllegalArgumentException(" LockBlock with unique number " + getUniqueId +  " not found");
    }


    private ShipmentBlockDto convertToDTO(ShipmentBlock shipmentBlock) {
        ShipmentBlockDto shipmentBlockDto = new ShipmentBlockDto(
                shipmentBlock.getStorageName(),
                shipmentBlock.getEmployeeNumber(),
                shipmentBlock.getLocoBlockUniqueId(),
                shipmentBlock.getTransactionType(),
                shipmentBlock.getQuantity()
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
        return shipmentBlock;
    }
}
