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

    public static final String ADMIN = "Администратор";
    public static final String MANAGER = "Регионал";
    public static final String REGION = "region";
    public static final String BRIG = "Бригадир";
    public static final String SPARE_PARTS_RECEIPTS = "sparePartsReceipts";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String NUMBER_TABLE = "number_table";
    public static final String SPAREPARTSRECEIPT_5_WRITEOFF = "sparepartsreceipt_5_writeoff";
    public static final String ON_STORAGE = "на складе";
    public static final String SPAREPARTSRECEIPT_4_ADD = "sparepartsreceipt_4_add";
    public static final String SPAREPARTSRECEIPT_1_MAIN = "sparepartsreceipt_1_main";
    private final SparePartsReceiptService sparePartsReceiptService;

    public SparePartsReceiptController(SparePartsReceiptService sparePartsReceiptService) {
        this.sparePartsReceiptService = sparePartsReceiptService;
    }

    @GetMapping()
    public String getSparePartsReceiptsPage() {
        return SPAREPARTSRECEIPT_1_MAIN;
    }

    @GetMapping("/all")
    public String getAllSparePartsReceiptsPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<SparePartsReceiptDto> sparePartsReceipts = Collections.emptyList();
        if (ADMIN.equals(post)) {
            sparePartsReceipts = sparePartsReceiptService.getAllSparePart();
        } else if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            sparePartsReceipts = sparePartsReceiptService.getSparePartsReceiptByRegion(region);
        } else if (BRIG.equals(post)) {
            String storageName = (String) session.getAttribute("unit");
            sparePartsReceipts = sparePartsReceiptService.getSparePartsReceiptByStorageName(storageName);
        }
        model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceipts);
        return "sparepartsreceipt_2_list";
    }

    @GetMapping("/stock")
    public String getAllSparePartsReceiptsStock(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<SparePartsReceiptDto> sparePartsReceipts = Collections.emptyList();
        if (ADMIN.equals(post)) {
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsStock();
        } else if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsStockByRegion(region);
        } else if (BRIG.equals(post)) {
            String storageName = (String) session.getAttribute("unit");
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsStockByStorageName(storageName);
        }
        model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceipts);
        return "sparepartsreceipt_2_stock";
    }

    //Расход запасных частей
    @GetMapping("/shipped")
    public String getAllSparePartsReceiptsShipped(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<SparePartsReceiptDto> sparePartsReceipts = Collections.emptyList();
        if (ADMIN.equals(post)) {
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsShipped();
        } else if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsShippedByRegion(region);
        } else if (BRIG.equals(post)) {
            String storageName = (String) session.getAttribute("unit");
            sparePartsReceipts = sparePartsReceiptService.getAllSparePartsShippedByRegion(storageName);
        }
        model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceipts);
        return "sparepartsreceipt_2_shipped";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "sparepartsreceipt_3_search";
    }

    @GetMapping("/byId")
    public String getSparePartsReceiptById(@RequestParam Long id, Model model, HttpSession session) {
        String region = (String) session.getAttribute(REGION);
        String post = (String) session.getAttribute("post");
        String storageName = (String) session.getAttribute("unit");

        SparePartsReceiptDto sparePartsReceipt = sparePartsReceiptService.getSparePartsReceiptById(id);

        if (sparePartsReceipt != null && ADMIN.equals(post)) {
            model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceipt);
        } else if (sparePartsReceipt != null && MANAGER.equals(post) && region.equals(sparePartsReceipt.getRegion())) {
            model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceipt);
        } else if (sparePartsReceipt != null && BRIG.equals(post) && region.equals(sparePartsReceipt.getRegion()) && storageName.equals(sparePartsReceipt.getStorageName())) {
            model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceipt);
        } else {
            model.addAttribute(ERROR_MESSAGE, "Запись с таким ID не найдена или у вас нет прав на просмотр");
        }
        return "sparepartsreceipt_3_search";
    }

    @GetMapping("/write_off")
    public String showWriteOffSparePartsReceiptForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        if (ADMIN.equals(post) || MANAGER.equals(post)) {
            model.addAttribute(SPARE_PARTS_RECEIPTS, new SparePartsReceiptDto());
            return SPAREPARTSRECEIPT_5_WRITEOFF;
        } else if (BRIG.equals(post)) {
            String numberTable = (String) session.getAttribute(NUMBER_TABLE);
            String storageName = (String) session.getAttribute("unit");
            String region = (String) session.getAttribute(REGION);
            SparePartsReceiptDto sparePartsReceiptDto = new SparePartsReceiptDto();
            sparePartsReceiptDto.setEmployeeNumber(numberTable);
            sparePartsReceiptDto.setStorageName(storageName);
            sparePartsReceiptDto.setRegion(region);
            sparePartsReceiptDto.setTransactionType(ON_STORAGE);
            model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceiptDto);
            return SPAREPARTSRECEIPT_5_WRITEOFF;
        } else {
            model.addAttribute(ERROR_MESSAGE, "У вас нет прав на выполнение данной операции");
            return SPAREPARTSRECEIPT_1_MAIN;
        }
    }

    @PostMapping("/write_off")
    public String writeOffSparePartsReceipt(@ModelAttribute SparePartsReceiptDto sparePartsReceiptDto, Model model, HttpSession session) {
        String numberTable = (String) session.getAttribute(NUMBER_TABLE);
        try {
            SparePartsReceiptDto preparedWriteOffSparePartsReceiptDto = sparePartsReceiptService.prepareWriteOffSparePartDto(
                    sparePartsReceiptDto.getRegion(),
                    sparePartsReceiptDto.getStorageName(),
                    numberTable,
                    sparePartsReceiptDto.getSparePartName(),
                    sparePartsReceiptDto.getMeasure(),
                    sparePartsReceiptDto.getSparePartNumber(),
                    ON_STORAGE,
                    sparePartsReceiptDto.getQuantity()
            );

            SparePartsReceiptDto writeOffSparePartsReceipt = sparePartsReceiptService.writeOffSparePartDto(preparedWriteOffSparePartsReceiptDto);

            model.addAttribute("writeOffSparePartsReceipt", writeOffSparePartsReceipt);
            return "sparepartsreceipt_5_write_off_success";
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при списании: " + e.getMessage());
            model.addAttribute(SPARE_PARTS_RECEIPTS, sparePartsReceiptDto);
            return SPAREPARTSRECEIPT_5_WRITEOFF;
        }
    }

    @GetMapping("/create")
    public String showAddSparePartsReceiptForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        if (ADMIN.equals(post) || MANAGER.equals(post)) {
            model.addAttribute(SPARE_PARTS_RECEIPTS, new SparePartsReceiptDto());
            return SPAREPARTSRECEIPT_4_ADD;
        } else if (BRIG.equals(post)) {
            String numberTable = (String) session.getAttribute(NUMBER_TABLE);
            String storageName = (String) session.getAttribute("unit");
            String region = (String) session.getAttribute(REGION);
            SparePartsReceiptDto sparePartsReceiptDto = new SparePartsReceiptDto();
            sparePartsReceiptDto.setEmployeeNumber(numberTable);
            sparePartsReceiptDto.setStorageName(storageName);
            sparePartsReceiptDto.setRegion(region);
            sparePartsReceiptDto.setTransactionType(ON_STORAGE);
            model.addAttribute("sparePartsReceipt", sparePartsReceiptDto);
            return SPAREPARTSRECEIPT_4_ADD;
        } else {
            model.addAttribute(ERROR_MESSAGE, "У вас нет прав на выполнение данной операции");
            return SPAREPARTSRECEIPT_1_MAIN;
        }
    }

    @PostMapping("/create")
    public String createSparePartsReceipt(@ModelAttribute SparePartsReceiptDto sparePartsReceiptDto, Model model, HttpSession session) {
        String numberTable = (String) session.getAttribute(NUMBER_TABLE);
        try {
            SparePartsReceiptDto preparedSparePartsReceiptDto = sparePartsReceiptService.prepareSparePartsReceiptDto(
                    sparePartsReceiptDto.getRegion(),
                    sparePartsReceiptDto.getStorageName(),
                    numberTable,
                    sparePartsReceiptDto.getSparePartName(),
                    sparePartsReceiptDto.getMeasure(),
                    sparePartsReceiptDto.getSparePartNumber(),
                    ON_STORAGE,
                    sparePartsReceiptDto.getQuantity()
            );

            SparePartsReceiptDto createdSparePartsReceipt = sparePartsReceiptService.addSparePartsToReceipt(preparedSparePartsReceiptDto);

            model.addAttribute("createdSparePartsReceipt", createdSparePartsReceipt);
            return "sparepartsreceipt_4_add_success";
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при добавлении: " + e.getMessage());
            model.addAttribute("sparePartsReceipt", sparePartsReceiptDto);
            return SPAREPARTSRECEIPT_4_ADD;
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
            if (sparePartsReceipt != null && (ADMIN.equals(post) || MANAGER.equals(post) || BRIG.equals(post))) {
                sparePartsReceiptService.deleteSparePartsReceiptById(id);
                model.addAttribute("successMessage", "Запись успешно удалена");
            } else {
                model.addAttribute(ERROR_MESSAGE, "У вас нет прав на удаление этой записи");
            }
        } catch (Exception e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при удалении записи: " + e.getMessage());
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
            if (sparePartsReceipt != null && (ADMIN.equals(post) || MANAGER.equals(post) || BRIG.equals(post))) {
                sparePartsReceiptService.deleteSparePartsStockById(id);
                model.addAttribute("successMessage", "Запись успешно удалена");
            } else {
                model.addAttribute(ERROR_MESSAGE, "У вас нет прав на удаление этой записи");
            }
        } catch (Exception e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при удалении записи: " + e.getMessage());
        }

        return "sparepartsreceipt_6_delete_stock";
    }

}
