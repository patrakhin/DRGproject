package com.drgproject.repair.controller;

import com.drgproject.repair.dto.PositionRepairDTO;
import com.drgproject.repair.service.PositionRepairService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/position-repairs")
public class PositionRepairController {

    private final PositionRepairService positionRepairService;

    public PositionRepairController(PositionRepairService positionRepairService) {
        this.positionRepairService = positionRepairService;
    }

    @GetMapping
    public String getAllPositionRepairs(Model model) {
        List<PositionRepairDTO> positionRepairs = positionRepairService.getAllPositionRepairs();
        model.addAttribute("positionRepairs", positionRepairs);
        return "position_repair_1_main";
    }

    @GetMapping("/create")
    public String showCreatePositionRepairForm(Model model) {
        model.addAttribute("positionRepair", new PositionRepairDTO());
        return "position_repair_2_add";
    }

    @PostMapping("/create")
    public String createPositionRepair(@ModelAttribute PositionRepairDTO positionRepairDTO, Model model) {
        PositionRepairDTO createdPositionRepair = positionRepairService.createPositionRepair(positionRepairDTO);
        model.addAttribute("createdPositionRepair", createdPositionRepair);
        return "position_repair_2_add_success";
    }

    @GetMapping("/edit")
    public String showEditPositionRepairForm(@RequestParam long id, Model model) {
        PositionRepairDTO positionRepairDTO = positionRepairService.getPositionRepairById(id);
        if (positionRepairDTO != null) {
            model.addAttribute("positionRepair", positionRepairDTO);
            return "position_repair_3_update";
        } else {
            model.addAttribute("errorMessage", "Позиция ремонта с таким ID не найдена");
            return "position_repair_3_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updatePositionRepair(@PathVariable long id, @ModelAttribute PositionRepairDTO positionRepairDTO, Model model) {
        PositionRepairDTO updatedPositionRepair = positionRepairService.updatePositionRepair(id, positionRepairDTO);
        if (updatedPositionRepair != null) {
            model.addAttribute("updatedPositionRepair", updatedPositionRepair);
            return "position_repair_3_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить позицию ремонта");
            return "position_repair_3_update";
        }
    }

    @GetMapping("/delete")
    public String showDeletePositionRepairForm(Model model) {
        return "position_repair_4_delete";
    }

    @PostMapping("/deleteById")
    public String deletePositionRepair(@RequestParam long id, Model model) {
        boolean isDeleted = positionRepairService.deletePositionRepair(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Позиция ремонта успешно удалена");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении позиции ремонта");
        }
        return "position_repair_4_delete";
    }
}
