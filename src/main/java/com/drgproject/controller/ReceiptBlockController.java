package com.drgproject.controller;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.dto.ReceiptBlockRequestDto;
import com.drgproject.service.ReceiptBlockService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/receipt_blocks")
public class ReceiptBlockController {

    private static final String ADMIN = "Администратор";
    private static final String MANAGER = "Регионал";
    private static final String REGION = "region";
    private static final String BRIG = "Бригадир";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String RECEIPT_BLOCK = "receiptBlock";
    private static final String REC_BL_1_MAIN = "receiptblock_1_main";
    private static final String REC_BL_4_ADD = "receiptblock_4_add";

    private final ReceiptBlockService receiptBlockService;

    public ReceiptBlockController(ReceiptBlockService receiptBlockService) {
        this.receiptBlockService = receiptBlockService;
    }

    @GetMapping()
    public String getReceiptBlocksPage() {
        return REC_BL_1_MAIN;
    }

    @GetMapping("/all")
    public String getAllReceiptBlocksPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<ReceiptBlockDto> receiptBlocks = Collections.emptyList();
        if (ADMIN.equals(post)) {
            receiptBlocks = receiptBlockService.getAllReceiptBlock();
        } else if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            receiptBlocks = receiptBlockService.getReceiptBlocksByRegion(region);
        } else if (BRIG.equals(post)) {
            String depot = (String) session.getAttribute("unit");
            receiptBlocks = receiptBlockService.getReceiptBlocksByStorageName(depot);
        }
        model.addAttribute("receiptBlocks", receiptBlocks);
        return "receiptblock_2_list";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "receiptblock_3_search";
    }

    @GetMapping("/byId")
    public String getReceiptBlockById(@RequestParam Long id, Model model, HttpSession session) {
        String region = (String) session.getAttribute(REGION);
        String post = (String) session.getAttribute("post");
        String depot = (String) session.getAttribute("unit");
        ReceiptBlockDto receiptBlock = receiptBlockService.getReceiptBlockById(id);
        if (receiptBlock != null && ADMIN.equals(post)) {
            model.addAttribute(RECEIPT_BLOCK, receiptBlock);
        } else if (receiptBlock != null && MANAGER.equals(post) && region.equals(receiptBlock.getRegion())) {
            model.addAttribute(RECEIPT_BLOCK, receiptBlock);
        } else if (receiptBlock != null && BRIG.equals(post) && region.equals(receiptBlock.getRegion()) && depot.equals(receiptBlock.getStorageName())) {
            model.addAttribute(RECEIPT_BLOCK, receiptBlock);
        } else {
            model.addAttribute(ERROR_MESSAGE, "Запись с таким ID не найдена или у вас нет прав на просмотр");
        }
        return "receiptblock_3_search";
    }

    // Метод для отображения формы добавления блока
    @GetMapping("/create")
    public String showAddLocoBlockForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        // Проверяем роль пользователя
        if (ADMIN.equals(post) || MANAGER.equals(post)) {
            model.addAttribute(RECEIPT_BLOCK, new ReceiptBlockRequestDto());
            return REC_BL_4_ADD;
        } else if
        (BRIG.equals(post)) {
            String numberTable = (String) session.getAttribute("number_table");
            String storageName = (String) session.getAttribute("unit");
            String region = (String) session.getAttribute(REGION);
            ReceiptBlockRequestDto receiptBlockRequestDto = new ReceiptBlockRequestDto();
            receiptBlockRequestDto.setNumberTable(numberTable);
            receiptBlockRequestDto.setStorageName(storageName);
            receiptBlockRequestDto.setRegion(region);
            model.addAttribute(RECEIPT_BLOCK, receiptBlockRequestDto);
            return REC_BL_4_ADD;
        } else {
            model.addAttribute(ERROR_MESSAGE, "У вас нет прав на выполнение данной операции");
            return REC_BL_1_MAIN; // или другая страница с ошибкой доступа
        }
    }


    // Метод для обработки POST запроса на добавление блока
    @PostMapping("/create")
    public String createReceiptBlock(@ModelAttribute ReceiptBlockRequestDto receiptBlockRequestDto, Model model, HttpSession session) {
        String numberTable = (String) session.getAttribute("number_table");
        try {
            ReceiptBlockDto preparedReceiptBlockDto = receiptBlockService.prepareReceiptBlockDto(
                    receiptBlockRequestDto.getStorageName(),
                    receiptBlockRequestDto.getRegion(),
                    numberTable,
                    receiptBlockRequestDto.getSystemType(),
                    receiptBlockRequestDto.getNameBlock(),
                    receiptBlockRequestDto.getBlockNumber());

            ReceiptBlockDto createdReceiptBlock = receiptBlockService.addLocoBlockToReceipt(preparedReceiptBlockDto);

            model.addAttribute("createdReceiptBlock", createdReceiptBlock);
            return "receiptblock_4_add_success";
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при добавлении: " + e.getMessage());
            model.addAttribute(RECEIPT_BLOCK, receiptBlockRequestDto);
            return REC_BL_4_ADD;
        }
    }

    @GetMapping("/stock")
    public String getStockByStoragePage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<ReceiptBlockDto> stockBlocks;

        if (ADMIN.equals(post)) {
            stockBlocks = receiptBlockService.getRemainingReceiptBlocks();
        } else if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            stockBlocks = receiptBlockService.getRemainingReceiptBlocksByRegion(region);
        } else if (BRIG.equals(post)) {
            String storageName = (String) session.getAttribute("unit");
            stockBlocks = receiptBlockService.getRemainingReceiptBlocksByStorageName(storageName);
        } else {
            model.addAttribute(ERROR_MESSAGE, "У вас нет прав на просмотр остатков блоков на складе");
            return REC_BL_1_MAIN;
        }

        model.addAttribute("stockBlocks", stockBlocks);
        return "receiptblock_5_stock";
    }

    @GetMapping("/delete")
    public String showDeleteReceiptBlockForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "receiptblock_6_delete";
    }

    @PostMapping("/deleteById")
    public String deleteReceiptBlockById(@RequestParam Long id, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        try {
            ReceiptBlockDto receiptBlock = receiptBlockService.getReceiptBlockById(id);
            if (receiptBlock != null && (ADMIN.equals(post) || MANAGER.equals(post) || BRIG.equals(post))) {
                receiptBlockService.deleteReceiptBlock(id);
                model.addAttribute("successMessage", "Запись успешно удалена");
            } else {
                model.addAttribute(ERROR_MESSAGE, "У вас нет прав на удаление этой записи");
            }
        } catch (Exception e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при удалении записи: " + e.getMessage());
        }

        return "receiptblock_6_delete";
    }
}
