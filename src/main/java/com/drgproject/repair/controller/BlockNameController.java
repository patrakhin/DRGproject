package com.drgproject.repair.controller;

import com.drgproject.repair.dto.BlockNameDTO;
import com.drgproject.repair.dto.SystemNameDTO;
import com.drgproject.repair.service.BlockNameService;
import com.drgproject.repair.service.SystemNameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/block-names")
public class BlockNameController {

    private final BlockNameService blockNameService;
    private final SystemNameService systemNameService;

    public BlockNameController(BlockNameService blockNameService, SystemNameService systemNameService) {
        this.blockNameService = blockNameService;
        this.systemNameService = systemNameService;
    }

    @GetMapping
    public String getAllBlockNames(Model model) {
        List<BlockNameDTO> blockNames = blockNameService.getAllBlockNames();

        // Получение всех систем, чтобы сопоставить ID с наименованием
        List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
        // Создание маппинга ID системы на её наименование
        Map<Long, String> systemNameMap = systemNames.stream()
                .collect(Collectors.toMap(SystemNameDTO::getId, SystemNameDTO::getSysName));

        // Добавление данных в модель
        model.addAttribute("systemNameMap", systemNameMap);
        model.addAttribute("blockNames", blockNames);

        return "block_name_1_main"; // Страница со списком всех BlockNames
    }

    @GetMapping("/create")
    public String showCreateBlockNameForm(Model model) {
        List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
        model.addAttribute("systemNames", systemNames);
        model.addAttribute("blockName", new BlockNameDTO());
        return "block_name_2_add";
    }

    @PostMapping("/create")
    public String createBlockName(@ModelAttribute BlockNameDTO blockNameDTO, Model model) {
        BlockNameDTO createdBlockName = blockNameService.createBlockName(blockNameDTO);
        model.addAttribute("createdBlockName", createdBlockName);
        return "block_name_2_add_success"; // Страница с сообщением об успешном добавлении
    }

    @GetMapping("/edit/{id}")
    public String showEditBlockNameForm(@PathVariable Long id, Model model) {
        BlockNameDTO blockNameDTO = blockNameService.getBlockNameById(id);
        if (blockNameDTO != null) {
            List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
            model.addAttribute("systemNames", systemNames);
            model.addAttribute("blockName", blockNameDTO);
            return "block_name_3_update";
        } else {
            model.addAttribute("errorMessage", "Наименование блока с таким ID не найдено");
            return "block_name_3_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateBlockName(@PathVariable long id, @ModelAttribute BlockNameDTO blockNameDTO, Model model) {
        BlockNameDTO updatedBlockName = blockNameService.updateBlockName(id, blockNameDTO);
        if (updatedBlockName != null) {
            model.addAttribute("updatedBlockName", updatedBlockName);
            return "block_name_3_update_success"; // Страница с сообщением об успешном обновлении
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить наименование блока");
            return "block_name_3_update"; // Страница с сообщением об ошибке
        }
    }

    @GetMapping("/delete/{id}")
    public String showDeleteBlockNameForm(@PathVariable long id, Model model) {
        model.addAttribute("blockNameId", id); // Передаем ID блока на страницу
        return "block_name_4_delete";
    }

    @PostMapping("/deleteById")
    public String deleteBlockName(@RequestParam long id, Model model) {
        boolean isDeleted = blockNameService.deleteBlockName(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Наименование блока успешно удалено");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении наименования");
        }
        model.addAttribute("blockNameId", id); // Сохраняем ID для возможности возврата на страницу подтверждения
        return "redirect:/block-names";
    }
}
