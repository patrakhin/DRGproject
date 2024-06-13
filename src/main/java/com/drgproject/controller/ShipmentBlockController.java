package com.drgproject.controller;

import com.drgproject.dto.ShipmentBlockDto;
import com.drgproject.dto.ShipmentBlockRequestDto;
import com.drgproject.service.ShipmentBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/shipment")
public class ShipmentBlockController {
    private final ShipmentBlockService shipmentBlockService;

    public ShipmentBlockController(ShipmentBlockService shipmentBlockService) {
        this.shipmentBlockService = shipmentBlockService;
    }

    /**
     * Получение списка всех записей отгрузок LocoBlock.
     *
     * @return список всех записей отгрузок
     */
    @GetMapping
    public ResponseEntity<List<ShipmentBlockDto>> getAllShipmentBlock(){
        List<ShipmentBlockDto> shipmentBlockDto = shipmentBlockService.getAllShipmentBlock();
        return ResponseEntity.ok(shipmentBlockDto);
    }

    /**
     * Получение записи отгрузки LocoBlock по её ID.
     *
     * @param id идентификатор записи
     * @return найденная запись или статус 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentBlockDto> getShipmentBlockDyId(@PathVariable Long id){
        ShipmentBlockDto shipmentBlockDto = shipmentBlockService.getShipmentBlockById(id);
        if(shipmentBlockDto != null){
            return ResponseEntity.ok(shipmentBlockDto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Удаление записи отгрузки LocoBlock по её ID при ошибочном добавлении на склад
     *
     * @param id идентификатор удаляемой записи
     * @return статус 204 (No Content), если удаление прошло успешно
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentBlockById(@PathVariable Long id){
        shipmentBlockService.deleteShipmentBlock(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Добавление записи об отгрузке LocoBlock со склада.
     *
     * @param shipmentBlockRequestDto DTO с данными для новой записи
     * @return созданная запись об отгрузке со склада
     */
    @PostMapping("/shipment-from-storage")
    public ResponseEntity<ShipmentBlockDto> shipmentLocoBlockFromStorage(
            @RequestBody ShipmentBlockRequestDto shipmentBlockRequestDto) {
        try {
            ShipmentBlockDto shipmentBlockDto = shipmentBlockService.shipmentLocoBlockFromStorage(
                    shipmentBlockRequestDto.getNumberTable(),
                    shipmentBlockRequestDto.getSystemType(),
                    shipmentBlockRequestDto.getNameBlock(),
                    shipmentBlockRequestDto.getBlockNumber());
            return ResponseEntity.ok(shipmentBlockDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
