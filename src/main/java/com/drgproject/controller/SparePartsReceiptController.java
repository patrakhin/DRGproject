package com.drgproject.controller;

import com.drgproject.dto.SparePartsReceiptDto;
import com.drgproject.service.SparePartsReceiptService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/spare_parts_receipts")
public class SparePartsReceiptController {

    private final SparePartsReceiptService sparePartsReceiptService;

    public SparePartsReceiptController(SparePartsReceiptService sparePartsReceiptService) {
        this.sparePartsReceiptService = sparePartsReceiptService;
    }

    @GetMapping()
    public String getSparePartsReceiptsPage() {
        return "sparepartsreceipt_1_main";
    }

    @GetMapping("/all")
    public String getAllSparePartsReceiptsPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<SparePartsReceiptDto> sparePartsReceipts = Collections.emptyList();
        if ("Администратор".equals(post)) {
            sparePartsReceipts = sparePartsReceiptService.getAllSparePart();
        } else if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            sparePartsReceipts = sparePartsReceiptService.getSparePartsReceiptByRegion(region);
        } else if ("Бригадир".equals(post)) {
            String storageName = (String) session.getAttribute("unit");
            sparePartsReceipts = sparePartsReceiptService.getSparePartsReceiptByStorageName(storageName);
        }
        model.addAttribute("sparePartsReceipts", sparePartsReceipts);
        return "sparepartsreceipt_2_list";
    }

    @GetMapping("/stock")
    public String getAllSparePartsReceiptsStock(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<SparePartsReceiptDto> sparePartsReceipts = Collections.emptyList();
        if ("Администратор".equals(post)) {
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsStock();
        } else if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsStockByRegion(region);
        } else if ("Бригадир".equals(post)) {
            String storageName = (String) session.getAttribute("unit");
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsStockByStorageName(storageName);
        }
        model.addAttribute("sparePartsReceipts", sparePartsReceipts);
        return "sparepartsreceipt_2_stock";
    }

    //Расход запасных частей
    @GetMapping("/shipped")
    public String getAllSparePartsReceiptsShipped(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<SparePartsReceiptDto> sparePartsReceipts = Collections.emptyList();
        if ("Администратор".equals(post)) {
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsShipped();
        } else if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsShippedByRegion(region);
        } else if ("Бригадир".equals(post)) {
            String storageName = (String) session.getAttribute("unit");
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsShippedByRegion(storageName);
        }
        model.addAttribute("sparePartsReceipts", sparePartsReceipts);
        return "sparepartsreceipt_2_shipped";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "sparepartsreceipt_3_search";
    }

    @GetMapping("/byId")
    public String getSparePartsReceiptById(@RequestParam Long id, Model model, HttpSession session) {
        String region = (String) session.getAttribute("region");
        String post = (String) session.getAttribute("post");
        String storageName = (String) session.getAttribute("unit");

        SparePartsReceiptDto sparePartsReceipt = sparePartsReceiptService.getSparePartsReceiptById(id);

        if (sparePartsReceipt != null && "Администратор".equals(post)) {
            model.addAttribute("sparePartsReceipt", sparePartsReceipt);
        } else if (sparePartsReceipt != null && "Регионал".equals(post) && region.equals(sparePartsReceipt.getRegion())) {
            model.addAttribute("sparePartsReceipt", sparePartsReceipt);
        } else if (sparePartsReceipt != null && "Бригадир".equals(post) && region.equals(sparePartsReceipt.getRegion()) && storageName.equals(sparePartsReceipt.getStorageName())) {
            model.addAttribute("sparePartsReceipt", sparePartsReceipt);
        } else {
            model.addAttribute("errorMessage", "Запись с таким ID не найдена или у вас нет прав на просмотр");
        }
        return "sparepartsreceipt_3_search";
    }

    @GetMapping("/write_off")
    public String showWriteOffSparePartsReceiptForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        if ("Администратор".equals(post) || "Регионал".equals(post)) {
            model.addAttribute("sparePartsReceipt", new SparePartsReceiptDto());
            return "sparepartsreceipt_5_writeoff";
        } else if ("Бригадир".equals(post)) {
            String numberTable = (String) session.getAttribute("number_table");
            String storageName = (String) session.getAttribute("unit");
            String region = (String) session.getAttribute("region");
            SparePartsReceiptDto sparePartsReceiptDto = new SparePartsReceiptDto();
            sparePartsReceiptDto.setEmployeeNumber(numberTable);
            sparePartsReceiptDto.setStorageName(storageName);
            sparePartsReceiptDto.setRegion(region);
            sparePartsReceiptDto.setTransactionType("на складе");
            model.addAttribute("sparePartsReceipt", sparePartsReceiptDto);
            return "sparepartsreceipt_5_writeoff";
        } else {
            model.addAttribute("errorMessage", "У вас нет прав на выполнение данной операции");
            return "sparepartsreceipt_1_main";
        }
    }

    @PostMapping("/write_off")
    public String writeOffSparePartsReceipt(@ModelAttribute SparePartsReceiptDto sparePartsReceiptDto, Model model, HttpSession session) {
        String numberTable = (String) session.getAttribute("number_table");
        try {
            SparePartsReceiptDto preparedWriteOffSparePartsReceiptDto = sparePartsReceiptService.prepareWriteOffSparePartDto(
                    sparePartsReceiptDto.getRegion(),
                    sparePartsReceiptDto.getStorageName(),
                    numberTable,
                    sparePartsReceiptDto.getSparePartName(),
                    sparePartsReceiptDto.getMeasure(),
                    sparePartsReceiptDto.getSparePartNumber(),
                    "на складе",
                    sparePartsReceiptDto.getQuantity()
            );

            SparePartsReceiptDto writeOffSparePartsReceipt = sparePartsReceiptService.writeOffSparePartDto(preparedWriteOffSparePartsReceiptDto);

            model.addAttribute("writeOffSparePartsReceipt", writeOffSparePartsReceipt);
            return "sparepartsreceipt_5_write_off_success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Ошибка при списании: " + e.getMessage());
            model.addAttribute("sparePartsReceipt", sparePartsReceiptDto);
            return "sparepartsreceipt_5_writeoff";
        }
    }

    @GetMapping("/create")
    public String showAddSparePartsReceiptForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        if ("Администратор".equals(post) || "Регионал".equals(post)) {
            model.addAttribute("sparePartsReceipt", new SparePartsReceiptDto());
            return "sparepartsreceipt_4_add";
        } else if ("Бригадир".equals(post)) {
            String numberTable = (String) session.getAttribute("number_table");
            String storageName = (String) session.getAttribute("unit");
            String region = (String) session.getAttribute("region");
            SparePartsReceiptDto sparePartsReceiptDto = new SparePartsReceiptDto();
            sparePartsReceiptDto.setEmployeeNumber(numberTable);
            sparePartsReceiptDto.setStorageName(storageName);
            sparePartsReceiptDto.setRegion(region);
            sparePartsReceiptDto.setTransactionType("на складе");
            model.addAttribute("sparePartsReceipt", sparePartsReceiptDto);
            return "sparepartsreceipt_4_add";
        } else {
            model.addAttribute("errorMessage", "У вас нет прав на выполнение данной операции");
            return "sparepartsreceipt_1_main";
        }
    }

    @PostMapping("/create")
    public String createSparePartsReceipt(@ModelAttribute SparePartsReceiptDto sparePartsReceiptDto, Model model, HttpSession session) {
        String numberTable = (String) session.getAttribute("number_table");
        try {
            SparePartsReceiptDto preparedSparePartsReceiptDto = sparePartsReceiptService.prepareSparePartsReceiptDto(
                    sparePartsReceiptDto.getRegion(),
                    sparePartsReceiptDto.getStorageName(),
                    numberTable,
                    sparePartsReceiptDto.getSparePartName(),
                    sparePartsReceiptDto.getMeasure(),
                    sparePartsReceiptDto.getSparePartNumber(),
                    "на складе",
                    sparePartsReceiptDto.getQuantity()
            );

            SparePartsReceiptDto createdSparePartsReceipt = sparePartsReceiptService.addSparePartsToReceipt(preparedSparePartsReceiptDto);

            model.addAttribute("createdSparePartsReceipt", createdSparePartsReceipt);
            return "sparepartsreceipt_4_add_success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Ошибка при добавлении: " + e.getMessage());
            model.addAttribute("sparePartsReceipt", sparePartsReceiptDto);
            return "sparepartsreceipt_4_add";
        }
    }

    @GetMapping("/delete")
    public String showDeleteSparePartsReceiptForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "sparepartsreceipt_6_delete";
    }

    @PostMapping("/deleteById")
    public String deleteSparePartsReceiptById(@RequestParam Long id, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        try {
            SparePartsReceiptDto sparePartsReceipt = sparePartsReceiptService.getSparePartsReceiptById(id);
            if (sparePartsReceipt != null && ("Администратор".equals(post) || "Регионал".equals(post) || "Бригадир".equals(post))) {
                sparePartsReceiptService.deleteSparePartsReceiptById(id);
                model.addAttribute("successMessage", "Запись успешно удалена");
            } else {
                model.addAttribute("errorMessage", "У вас нет прав на удаление этой записи");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении записи: " + e.getMessage());
        }

        return "sparepartsreceipt_6_delete";
    }

    @GetMapping("/deleteStock")
    public String showDeleteStockSparePartsReceiptForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "sparepartsreceipt_6_delete_stock";
    }

    @PostMapping("/deleteStockById")
    public String deleteStockSparePartsReceiptById(@RequestParam Long id, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        try {
            SparePartsReceiptDto sparePartsReceipt = sparePartsReceiptService.getSparePartsReceiptById(id);
            if (sparePartsReceipt != null && ("Администратор".equals(post) || "Регионал".equals(post) || "Бригадир".equals(post))) {
                sparePartsReceiptService.deleteSparePartsStockById(id);
                model.addAttribute("successMessage", "Запись успешно удалена");
            } else {
                model.addAttribute("errorMessage", "У вас нет прав на удаление этой записи");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении записи: " + e.getMessage());
        }

        return "sparepartsreceipt_6_delete_stock";
    }

}
