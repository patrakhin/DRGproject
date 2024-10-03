package com.drgproject.controller;

import com.drgproject.dto.LocoBlockDto;
import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.dto.SystemNameDTO;
import com.drgproject.repair.service.RegionService;
import com.drgproject.repair.service.SystemNameService;
import com.drgproject.service.LocoBlockService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/loco_blocks")
public class LocoBlockController {

    private static final String ADMIN = "Администратор";
    private static final String MANAGER = "Регионал";
    private static final String REGION = "region";
    private static final String LOCO_BLOCK = "locoBlock";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String LOC_BL_5_UP = "locoblock_5_update";

    private final LocoBlockService locoBlockService;
    private final SystemNameService systemNameService;
    private final RegionService regionService;

    public LocoBlockController(LocoBlockService locoBlockService, SystemNameService systemNameService, RegionService regionService) {
        this.locoBlockService = locoBlockService;
        this.systemNameService = systemNameService;
        this.regionService = regionService;
    }

    @GetMapping()
    public String getLocoBlocksPage() {
        return "locoblock_1_main";
    }

    @GetMapping("/all")
    public String getAllLocoBlocksPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<LocoBlockDto> locoBlocks = Collections.emptyList();
        if (ADMIN.equals(post)) {
            locoBlocks = locoBlockService.getAllLocoBlocks();
        } else if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            locoBlocks = locoBlockService.getLocoBlocksByRegion(region);
        }
        model.addAttribute("locoBlocks", locoBlocks);
        return "locoblock_2_list";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "locoblock_3_search";
    }

    @GetMapping("/byName")
    public String getLocoBlockByName(@RequestParam String name, Model model, HttpSession session) {
        String region = (String) session.getAttribute(REGION);
        String post = (String) session.getAttribute("post");
        List<LocoBlockDto> locoBlock = locoBlockService.getLocoBlockByName(name);
        if (locoBlock != null && ADMIN.equals(post)) {
            model.addAttribute(LOCO_BLOCK, locoBlock);
        } else if (locoBlock != null && MANAGER.equals(post)) {
            locoBlock = locoBlockService.getLocoBlockByRegionAndName(region, name);
            model.addAttribute(LOCO_BLOCK, locoBlock);
        } else {
            model.addAttribute(ERROR_MESSAGE, "Блок с таким именем не найден");
        }
        return "locoblock_3_search";
    }

    @GetMapping("/create")
    public String showCreateLocoBlockForm(Model model, HttpSession session) {
        LocoBlockDto locoBlockDto = new LocoBlockDto();
        String post = (String) session.getAttribute("post");
        if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            locoBlockDto.setRegion(region);
        }
        // Получение всех типов систем и добавление в модель
        List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
        model.addAttribute("systemNames", systemNames);
        // Получение всех регионов и добавление в модель
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions",regions);
        model.addAttribute(LOCO_BLOCK, locoBlockDto);
        model.addAttribute("post", post); // Добавляем переменную post в модель
        return "locoblock_4_add";
    }

    @PostMapping("/create")
    public String createLocoBlock(@ModelAttribute LocoBlockDto locoBlockDto, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            locoBlockDto.setRegion(region);
        }
        LocoBlockDto createdLocoBlock = locoBlockService.createLocoBlock(locoBlockDto);
        model.addAttribute("createdLocoBlock", createdLocoBlock);
        return "locoblock_4_add_success";
    }

    @GetMapping("/edit")
    public String showEditLocoBlockForm(@RequestParam long id, Model model) {
        LocoBlockDto locoBlockDto = locoBlockService.getLocoBlockById(id);
        if (locoBlockDto != null) {
            model.addAttribute(LOCO_BLOCK, locoBlockDto);
        } else {
            model.addAttribute(ERROR_MESSAGE, "Блок с таким ID не найден");
        }
        return LOC_BL_5_UP;
    }

    @PostMapping("/edit/{id}")
    public String updateLocoBlock(@PathVariable long id, @ModelAttribute LocoBlockDto locoBlockDto, Model model) {
        LocoBlockDto updatedLocoBlock = locoBlockService.updateLocoBlock(id, locoBlockDto);
        if (updatedLocoBlock != null) {
            model.addAttribute("updatedLocoBlock", updatedLocoBlock);
            return "locoblock_5_update_success";
        } else {
            model.addAttribute(ERROR_MESSAGE, "Не удалось обновить данные блока");
            return LOC_BL_5_UP;
        }
    }


    @GetMapping("/delete")
    public String showDeleteLocoBlockForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "locoblock_6_delete";
    }

    @PostMapping("/deleteByBlockNumber")
    public String deleteLocoBlockByBlockNumber(@RequestParam String blockNumber, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        String region = (String) session.getAttribute(REGION);
        try {
            if (ADMIN.equals(post)) {
                locoBlockService.deleteLocoBlockByBlockNumber(blockNumber);
                model.addAttribute("successMessage", "Блок успешно удален");
            } else if (REGION.equals(post)) {
                LocoBlockDto locoBlock = locoBlockService.getLocoBlockByBlockNumber(blockNumber);
                if (locoBlock != null && region.equals(locoBlock.getRegion())) {
                    locoBlockService.deleteLocoBlockByBlockNumber(blockNumber);
                    model.addAttribute("successMessage", "Блок успешно удален");
                } else {
                    model.addAttribute(ERROR_MESSAGE, "У вас нет прав на удаление этого блока");
                }
            } else {
                model.addAttribute(ERROR_MESSAGE, "Неверная роль пользователя");
            }
        } catch (Exception e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при удалении блока: " + e.getMessage());
        }
        return "locoblock_6_delete";
    }

    //AJAX
    @GetMapping("/blocks")
    @ResponseBody
    public List<String> getBlocksBySystemType(@RequestParam String systemType) {
        return locoBlockService.getBlocksBySystemType(systemType);
    }
}
