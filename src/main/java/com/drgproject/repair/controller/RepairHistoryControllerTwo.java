package com.drgproject.repair.controller;


import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.repair.dto.BlockOnLocoDTO;
import com.drgproject.repair.dto.BlockRemovalDto;
import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.dto.RepairHistoryDto;
import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.entity.RepairHistory;
import com.drgproject.repair.service.BlockOnLocoService;
import com.drgproject.repair.service.BlockRemovalService;
import com.drgproject.repair.service.LocoListService;
import com.drgproject.repair.service.RepairHistoryService;
import com.drgproject.repository.ReceiptBlockRepository;
import com.drgproject.service.ReceiptBlockService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/repair_history")
public class RepairHistoryControllerTwo {

    private final RepairHistoryService repairHistoryService;
    private final LocoListService locoListService;
    private final BlockOnLocoService blockOnLocoService;
    private final ReceiptBlockService receiptBlockService;
    private final BlockRemovalService blockRemovalService;


    public RepairHistoryControllerTwo(RepairHistoryService repairHistoryService,
                                      LocoListService locoListService,
                                      BlockOnLocoService blockOnLocoService,
                                      ReceiptBlockService receiptBlockService,
                                      BlockRemovalService blockRemovalService) {
        this.repairHistoryService = repairHistoryService;
        this.locoListService = locoListService;
        this.blockOnLocoService = blockOnLocoService;
        this.receiptBlockService = receiptBlockService;
        this.blockRemovalService = blockRemovalService;
    }

    //Поиск локомотива
    @GetMapping("/search")
    public String showSearchForm() {
        return "repair_history_1_main";
    }

    @PostMapping("/search")
    public String searchLocoByTypeAndNumber(@RequestParam String typeLoco, @RequestParam String numberLoco, Model model) {
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        model.addAttribute("locoListDTO", locoListDTO);
        return "repair_history_2_work_bar";
    }

    @PostMapping("/repair_history")
    public String showRepairHistory(@RequestParam String typeLoco, @RequestParam String numberLoco, Model model){
        List<RepairHistoryDto> repairHistoryDtos = repairHistoryService.findByTypeLocoAndLocoNumber(typeLoco, numberLoco);
        model.addAttribute("repairHistoryDtos", repairHistoryDtos);
        return "repair_history_3_history_loco";
    }

    @PostMapping("/install_removal")
    public String showInstallBlocks(@RequestParam String typeLoco, @RequestParam String numberLoco, Model model, HttpSession session){
        String depot = (String) session.getAttribute("unit");
        // Установленные блоки на локомотиве
        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        // Блоки находящиеся на складе
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageName(depot);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);
        return "repair_history_4_install_loco";
    }

}

