package com.drgproject.repair.controller;

import com.drgproject.repair.dto.AllTypeLocoUnitDTO;
import com.drgproject.repair.service.AllTypeLocoUnitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/all-type-unit")
public class AllTypeLocoUnitController {
    private final AllTypeLocoUnitService allTypeLocoUnitService;

    public AllTypeLocoUnitController(AllTypeLocoUnitService allTypeLocoUnitService){
        this.allTypeLocoUnitService = allTypeLocoUnitService;
    }

    @GetMapping
    public String getAllTypeLoco(Model model) {
        List<AllTypeLocoUnitDTO> allTypeLocoUnitDTOS = allTypeLocoUnitService.getAllTypeLocoUnit();
        model.addAttribute("allTypeLoco", allTypeLocoUnitDTOS);
        return "all_type_loco_1_main";
    }

    @GetMapping("/create")
    public String showCreateAllTypeLoco(Model model) {
        model.addAttribute("allTypeLoco", new AllTypeLocoUnitDTO());
        return "all_type_loco_2_add";
    }

    @PostMapping("/create")
    public String createSystemName(@ModelAttribute AllTypeLocoUnitDTO allTypeLocoUnitDTO, Model model) {
        AllTypeLocoUnitDTO createdTypeLoco = allTypeLocoUnitService.createAllTypeLocoUnit(allTypeLocoUnitDTO);
        model.addAttribute("createdTypeLoco", createdTypeLoco);
        return "all_type_loco_2_add_success";
    }

    @GetMapping("/edit")
    public String showEditTypeLocoForm(@RequestParam long id, Model model) {
        AllTypeLocoUnitDTO allTypeLocoUnitDTO = allTypeLocoUnitService.getAllTypeLocoUnitById(id);
        if (allTypeLocoUnitDTO != null) {
            model.addAttribute("allTypeLocoUnitDTO", allTypeLocoUnitDTO);
        } else {
            model.addAttribute("errorMessage", "Серия локомотива с таким ID не найдена");
        }
        return "all_type_loco_3_update";
    }

    @PostMapping("/edit/{id}")
    public String updateTypeLoco(@PathVariable long id, @ModelAttribute AllTypeLocoUnitDTO allTypeLoco, Model model) {
        AllTypeLocoUnitDTO allTypeLocoUnit = allTypeLocoUnitService.updateAllTypeLocoUnit(id, allTypeLoco);
        if (allTypeLocoUnit != null) {
            model.addAttribute("allTypeLocoUnit", allTypeLocoUnit);
            return "all_type_loco_3_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить серию локомотива");
            return "all_type_loco_3_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteTypeLoco(Model model) {
        return "all_type_loco_4_delete";
    }

    @PostMapping("/deleteById")
    public String deleteSystemName(@RequestParam long id, Model model) {
        boolean isDeleted = allTypeLocoUnitService.deleteAllTypeLocoUnit(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Серия успешно удалена");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении серии локомотива");
        }
        return "all_type_loco_4_delete";
    }
}
