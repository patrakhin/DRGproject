package com.drgproject.repair.controller;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.repair.dto.*;
import com.drgproject.repair.service.*;
import com.drgproject.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/block_spare")
public class BlockAndSparePartsController {

    private static final String TYPE_LOCO = "typeLoco";

    private final LocoListService locoListService;
    private final BlockOnLocoService blockOnLocoService;
    private final ReceiptBlockService receiptBlockService;
    private final BlockRemovalService blockRemovalService;
    private final PositionRepairService positionRepairService;
    private final ShipmentBlockService shipmentBlockService;

    public BlockAndSparePartsController(LocoListService locoListService,
                                        BlockOnLocoService blockOnLocoService,
                                        ReceiptBlockService receiptBlockService,
                                        BlockRemovalService blockRemovalService,
                                        PositionRepairService positionRepairService,
                                        ShipmentBlockService shipmentBlockService) {
        this.locoListService = locoListService;
        this.blockOnLocoService = blockOnLocoService;
        this.receiptBlockService = receiptBlockService;
        this.blockRemovalService = blockRemovalService;
        this.positionRepairService = positionRepairService;
        this.shipmentBlockService = shipmentBlockService;
    }

    // Монтаж/демонтаж блоков  НЕ ДЕЙСТВУЮЩИЙ
    @PostMapping("/install_removal")
    public String showInstallBlocks(Model model, HttpSession session){
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();
        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<BlockRemovalDto> removedBlocks = blockRemovalService.getBlockRemovalByTypeLocoAndNumberLoco(numberLoco, typeLoco);

        String depot = (String) session.getAttribute("unit");
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageNameAndTypeSystem(depot, systemType);

        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("systemType", systemType);
        model.addAttribute("positionRepairDTOS", positionRepairDTOS); // Добавляем позиции ремонта в модель
        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("removedBlocks", removedBlocks);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);

        return "repair_history_4_install_loco";
    }


    // Методы для отгрузки блока со склада и монтажа блока на локомотив
    @GetMapping("/install_block")
    public String showInstallBlockPage(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");
        String sectionNumber = (String) session.getAttribute("sectionNumber"); //Нужно получить номер секции из @Param здесь пока null!
        String depot = (String) session.getAttribute("unit");

        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageNameAndTypeSystem(depot, systemType);

        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("systemType", systemType);

        return "repair_history_10_install_block";
    }

    @PostMapping("/install_block")
    public String installBlock(@RequestParam("blockName") String nameBlock,
                               @RequestParam("blockNumber") String blockNumber,
                               RedirectAttributes redirectAttributes, // Изменено с Model на RedirectAttributes
                               HttpSession session) {
        String numberTable = (String) session.getAttribute("number_table");
        String typeLoco = (String) session.getAttribute("typeLoco");
        String numberLoco = (String) session.getAttribute("numberLoco");
        String region = (String) session.getAttribute("region");

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);

        List<BlockOnLocoDTO> filteredBlock = blockOnLocoDTOS.stream()
                .filter(filtered -> nameBlock.equals(filtered.getBlockName()))
                .toList();
        if (!filteredBlock.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Такой блок уже установлен на локомотиве.");
            return "redirect:/repair_history/install_block";
        }


        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        try {
            shipmentBlockService.shipmentLocoBlockFromStorage(numberTable, systemType, nameBlock, blockNumber, region);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/repair_history/install_block";
        }

        BlockOnLocoDTO blockOnLocoDTO = new BlockOnLocoDTO();
        blockOnLocoDTO.setLocoListId(locoListDTO.getId());
        blockOnLocoDTO.setBlockName(nameBlock);
        blockOnLocoDTO.setBlockNumber(blockNumber);
        blockOnLocoDTO.setTypeLoco(typeLoco);
        blockOnLocoDTO.setLocoNumber(numberLoco);
        blockOnLocoService.createBlockOnLoco(blockOnLocoDTO);

        redirectAttributes.addFlashAttribute("success", "Блок успешно установлен.");
        return "redirect:/repair_history/install_block";
    }

    // Методы демонтажа блока с локомотива
    @GetMapping("/remove_block/prepare")
    public String showRemoveBlockPage(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();

        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("positionRepairDTOS", positionRepairDTOS);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);

        return "repair_history_8_remove_block";
    }

    @PostMapping("/remove_block")
    public ResponseEntity<Void> removeBlock(@RequestParam String nameBlock,
                                            @RequestParam String blockNumber,
                                            @RequestParam("positionRepair") Long positionRepairId,
                                            HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        // Получение позиции ремонта по ID
        PositionRepairDTO positionRepairDTO = positionRepairService.getPositionRepairById(positionRepairId);
        String positionRepair = positionRepairDTO.getPosRepair();

        String homeDepot = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco).getHomeDepot();
        String systemType = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco).getTypeSystem();

        // Формирование DTO для записи
        BlockRemovalDto blockRemovalDto = new BlockRemovalDto();
        blockRemovalDto.setTypeLoco(typeLoco);
        blockRemovalDto.setLocoNumber(numberLoco);
        blockRemovalDto.setRegion((String) session.getAttribute("region"));
        blockRemovalDto.setHomeDepot(homeDepot);
        blockRemovalDto.setSystemType(systemType);
        blockRemovalDto.setBlockName(nameBlock);
        blockRemovalDto.setBlockNumber(blockNumber);
        blockRemovalDto.setNumberTable((String) session.getAttribute("number_table"));
        blockRemovalDto.setPosition(positionRepair);

        // Логика добавления записи в "Демонтированные блоки"
        blockRemovalService.createBlockRemoval(blockRemovalDto);

        return ResponseEntity.ok().build();
    }


    // Метод отмены демонтажа блока
    @GetMapping("/cancel_remove_block")
    public String showCancelRemoveBlockPage(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        if (typeLoco == null || numberLoco == null) {
            model.addAttribute("error", "Нет данных о типе или номере локомотива.");
            return "repair_history_9_cancel_remove_block";
        }

        List<BlockRemovalDto> removedBlocks = blockRemovalService.getBlockRemovalByTypeLocoAndNumberLoco(typeLoco, numberLoco);
        model.addAttribute("removedBlocks", removedBlocks);

        return "repair_history_9_cancel_remove_block";
    }

    @PostMapping("/cancel_remove_block")
    public String cancelRemoveBlock(@RequestParam("nameBlock") String nameBlock,
                                    @RequestParam("blockNumber") String blockNumber,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");
        String region = (String) session.getAttribute("region");
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String homeDepot = locoListDTO.getHomeDepot();
        String systemType = locoListDTO.getTypeSystem();
        String numberTable = (String) session.getAttribute("number_table");

        BlockRemovalDto blockRemovalDto = new BlockRemovalDto();
        blockRemovalDto.setTypeLoco(typeLoco);
        blockRemovalDto.setLocoNumber(numberLoco);
        blockRemovalDto.setBlockNumber(blockNumber);
        blockRemovalDto.setRegion(region);
        blockRemovalDto.setHomeDepot(homeDepot);
        blockRemovalDto.setSystemType(systemType);
        blockRemovalDto.setBlockName(nameBlock);
        blockRemovalDto.setNumberTable(numberTable);

        try {
            blockRemovalService.cancelBlockRemovalFromLoco(blockRemovalDto);
            redirectAttributes.addFlashAttribute("success", "Демонтаж блока успешно отменен.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/repair_history/cancel_remove_block";
    }
}
