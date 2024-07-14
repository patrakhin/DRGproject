package com.drgproject.controller;

import com.drgproject.dto.ShipmentBlockDto;
import com.drgproject.dto.ShipmentBlockRequestDto;
import com.drgproject.service.ShipmentBlockService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/shipment_blocks")
public class ShipmentBlockController {

    private final ShipmentBlockService shipmentBlockService;

    public ShipmentBlockController(ShipmentBlockService shipmentBlockService) {
        this.shipmentBlockService = shipmentBlockService;
    }

    @GetMapping()
    public String getShipmentBlocksPage() {
        return "shipmentblock_1_main";
    }

    @GetMapping("/all")
    public String getAllShipmentBlocksPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<ShipmentBlockDto> shipmentBlocks = Collections.emptyList();
        if ("Администратор".equals(post)) {
            shipmentBlocks = shipmentBlockService.getAllShipmentBlock();
        } else if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            shipmentBlocks = shipmentBlockService.getShipmentBlocksByRegion(region);
        } else if ("Бригадир".equals(post)) {
            String depot = (String) session.getAttribute("unit");
            shipmentBlocks = shipmentBlockService.getShipmentBlocksByStorageName(depot);
        }
        model.addAttribute("shipmentBlocks", shipmentBlocks);
        return "shipmentblock_2_list";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "shipmentblock_3_search";
    }

    @GetMapping("/byId")
    public String getShipmentBlockById(@RequestParam Long id, Model model, HttpSession session) {
        String region = (String) session.getAttribute("region");
        String post = (String) session.getAttribute("post");
        String depot = (String) session.getAttribute("unit");

        ShipmentBlockDto shipmentBlock = shipmentBlockService.getShipmentBlockById(id);

        if (shipmentBlock != null && "Администратор".equals(post)) {
            model.addAttribute("shipmentBlock", shipmentBlock);
        } else if (shipmentBlock != null && "Регионал".equals(post) && region.equals(shipmentBlock.getRegion())) {
            model.addAttribute("shipmentBlock", shipmentBlock);
        } else if (shipmentBlock != null && "Бригадир".equals(post) && region.equals(shipmentBlock.getRegion()) && depot.equals(shipmentBlock.getStorageName())) {
            model.addAttribute("shipmentBlock", shipmentBlock);
        } else {
            model.addAttribute("errorMessage", "Запись с таким ID не найдена или у вас нет прав на просмотр");
        }
        return "shipmentblock_3_search";
    }

    @GetMapping("/create")
    public String showAddShipmentBlockForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        if ("Администратор".equals(post) || "Регионал".equals(post)) {
            model.addAttribute("shipmentBlock", new ShipmentBlockRequestDto());
            return "shipmentblock_4_add";
        } else if ("Бригадир".equals(post)) {
            String numberTable = (String) session.getAttribute("numberTable");
            String storageName = (String) session.getAttribute("unit");
            String region = (String) session.getAttribute("region");
            ShipmentBlockRequestDto shipmentBlockRequestDto = new ShipmentBlockRequestDto();
            shipmentBlockRequestDto.setNumberTable(numberTable);
            shipmentBlockRequestDto.setStorageName(storageName);
            shipmentBlockRequestDto.setRegion(region);
            model.addAttribute("shipmentBlock", shipmentBlockRequestDto);
            return "shipmentblock_4_add";
        } else {
            model.addAttribute("errorMessage", "У вас нет прав на выполнение данной операции");
            return "shipmentblock_1_main"; // или другая страница с ошибкой доступа
        }
    }

    @PostMapping("/create")
    public String createShipmentBlock(@ModelAttribute ShipmentBlockRequestDto shipmentBlockRequestDto, Model model, HttpSession session) {
        String numberTable = (String) session.getAttribute("number_table");
        try {
            ShipmentBlockDto shipmentBlockDto = shipmentBlockService.shipmentLocoBlockFromStorage(
                    numberTable,
                    shipmentBlockRequestDto.getSystemType(),
                    shipmentBlockRequestDto.getNameBlock(),
                    shipmentBlockRequestDto.getBlockNumber(),
                    shipmentBlockRequestDto.getRegion()
            );

            model.addAttribute("createdShipmentBlock", shipmentBlockDto);
            return "shipmentblock_4_add_success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Ошибка при добавлении: " + e.getMessage());
            model.addAttribute("shipmentBlock", shipmentBlockRequestDto);
            return "shipmentblock_4_add";
        }
    }

    @GetMapping("/delete")
    public String showDeleteShipmentBlockForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "shipmentblock_6_delete";
    }

    @PostMapping("/deleteById")
    public String deleteShipmentBlockById(@RequestParam Long id, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        try {
            ShipmentBlockDto shipmentBlock = shipmentBlockService.getShipmentBlockById(id);
            if (shipmentBlock != null && ("Администратор".equals(post) || "Регионал".equals(post) || "Бригадир".equals(post))) {
                shipmentBlockService.deleteShipmentBlock(id);
                model.addAttribute("successMessage", "Запись успешно удалена");
            } else {
                model.addAttribute("errorMessage", "У вас нет прав на удаление этой записи");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении записи: " + e.getMessage());
        }

        return "shipmentblock_6_delete";
    }
}
