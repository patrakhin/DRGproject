package com.drgproject.repair.controller;

import com.drgproject.repair.dto.TypeLocoDTO;
import com.drgproject.repair.service.TypeLocoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/type-locos")
public class TypeLocoController {

    private final TypeLocoService typeLocoService;

    public TypeLocoController(TypeLocoService typeLocoService) {
        this.typeLocoService = typeLocoService;
    }

    @GetMapping
    public String getAllTypeLocos(Model model) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        return "type_loco_1_main";
    }

    @GetMapping("/create")
    public String showCreateTypeLocoForm(Model model) {
        model.addAttribute("typeLoco", new TypeLocoDTO());
        return "type_loco_2_add";
    }

    @PostMapping("/create")
    public String createTypeLoco(@ModelAttribute TypeLocoDTO typeLocoDTO, Model model) {
        TypeLocoDTO createdTypeLoco = typeLocoService.createTypeLoco(typeLocoDTO);
        model.addAttribute("createdTypeLoco", createdTypeLoco);
        return "type_loco_2_add_success";
    }

    @GetMapping("/edit")
    public String showEditTypeLocoForm(@RequestParam long id, Model model) {
        TypeLocoDTO typeLocoDTO = typeLocoService.getTypeLocoById(id);
        if (typeLocoDTO != null) {
            model.addAttribute("typeLoco", typeLocoDTO);
            return "type_loco_3_update";
        } else {
            model.addAttribute("errorMessage", "Серия локомотива с таким ID не найдена");
            return "type_loco_3_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateTypeLoco(@PathVariable long id, @ModelAttribute TypeLocoDTO typeLocoDTO, Model model) {
        TypeLocoDTO updatedTypeLoco = typeLocoService.updateTypeLoco(id, typeLocoDTO);
        if (updatedTypeLoco != null) {
            model.addAttribute("updatedTypeLoco", updatedTypeLoco);
            return "type_loco_3_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить серию локомотива");
            return "type_loco_3_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteTypeLocoForm(Model model) {
        return "type_loco_4_delete";
    }

    @PostMapping("/deleteById")
    public String deleteTypeLoco(@RequestParam long id, Model model) {
        boolean isDeleted = typeLocoService.deleteTypeLoco(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Серия локомотива успешно удалена");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении серии локомотива");
        }
        return "type_loco_4_delete";
    }
}
