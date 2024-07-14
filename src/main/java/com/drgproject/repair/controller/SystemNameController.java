package com.drgproject.repair.controller;

import com.drgproject.repair.dto.SystemNameDTO;
import com.drgproject.repair.service.SystemNameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/system-names")
public class SystemNameController {

    private final SystemNameService systemNameService;

    public SystemNameController(SystemNameService systemNameService) {
        this.systemNameService = systemNameService;
    }

    @GetMapping
    public String getAllSystemNames(Model model) {
        List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
        model.addAttribute("systemNames", systemNames);
        return "system_name_1_main";
    }

    @GetMapping("/create")
    public String showCreateSystemNameForm(Model model) {
        model.addAttribute("systemName", new SystemNameDTO());
        return "system_name_2_add";
    }

    @PostMapping("/create")
    public String createSystemName(@ModelAttribute SystemNameDTO systemNameDTO, Model model) {
        SystemNameDTO createdSystemName = systemNameService.createSystemName(systemNameDTO);
        model.addAttribute("createdSystemName", createdSystemName);
        return "system_name_2_add_success";
    }

    @GetMapping("/edit")
    public String showEditSystemNameForm(@RequestParam long id, Model model) {
        SystemNameDTO systemNameDTO = systemNameService.getSystemNameById(id);
        if (systemNameDTO != null) {
            model.addAttribute("systemName", systemNameDTO);
            return "system_name_3_update";
        } else {
            model.addAttribute("errorMessage", "Системное имя с таким ID не найдено");
            return "system_name_3_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSystemName(@PathVariable long id, @ModelAttribute SystemNameDTO systemNameDTO, Model model) {
        SystemNameDTO updatedSystemName = systemNameService.updateSystemName(id, systemNameDTO);
        if (updatedSystemName != null) {
            model.addAttribute("updatedSystemName", updatedSystemName);
            return "system_name_3_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить системное имя");
            return "system_name_3_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteSystemNameForm(Model model) {
        return "system_name_4_delete";
    }

    @PostMapping("/deleteById")
    public String deleteSystemName(@RequestParam long id, Model model) {
        boolean isDeleted = systemNameService.deleteSystemName(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Системное имя успешно удалено");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении системного имени");
        }
        return "system_name_4_delete";
    }
}
