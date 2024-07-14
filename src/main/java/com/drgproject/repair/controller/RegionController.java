package com.drgproject.repair.controller;

import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.service.RegionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public String getRegionsPage() {
        return "region_1_main";
    }

    @GetMapping("/all")
    public String getAllRegionsPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<RegionDTO> regions = Collections.emptyList();
        if ("Администратор".equals(post)) {
            regions = regionService.getAllRegions();
        }
        model.addAttribute("regions", regions);
        return "region_2_list";
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable Long id) {
        RegionDTO regionDTO = regionService.getRegionById(id);
        if (regionDTO != null) {
            return ResponseEntity.ok(regionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "region_3_search";
    }

    @GetMapping("/byName")
    public String getRegionByName(@RequestParam String name, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        RegionDTO region = regionService.getRegionByName(name);

        if (region != null && "Администратор".equals(post)) {
            model.addAttribute("region", region);
        } else {
            model.addAttribute("errorMessage", "Регион с таким именем не найден");
        }
        return "region_3_search";
    }

    @GetMapping("/create")
    public String showCreateRegionForm(Model model) {
        model.addAttribute("region", new RegionDTO());
        return "region_4_add";
    }

    @PostMapping("/create")
    public String createRegion(@ModelAttribute RegionDTO regionDTO, Model model) {
        RegionDTO createdRegion = regionService.createRegion(regionDTO);
        model.addAttribute("createdRegion", createdRegion);
        return "region_4_add_success";
    }

    @GetMapping("/edit")
    public String showEditRegionForm(@RequestParam long id, Model model) {
        RegionDTO regionDTO = regionService.getRegionById(id);
        if (regionDTO != null) {
            model.addAttribute("region", regionDTO);
        } else {
            model.addAttribute("errorMessage", "Регион с таким ID не найден");
        }
        return "region_5_update";
    }

    @PostMapping("/edit/{id}")
    public String updateRegion(@PathVariable long id, @ModelAttribute RegionDTO regionDTO, Model model) {
        RegionDTO updatedRegion = regionService.updateRegion(id, regionDTO);
        if (updatedRegion != null) {
            model.addAttribute("updatedRegion", updatedRegion);
            return "region_5_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные региона");
            return "region_5_update";
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/delete")
    public String showDeleteRegionForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "region_6_delete";
    }

    @PostMapping("/deleteByName")
    public String deleteRegionByName(@RequestParam String name, Model model) {
        try {
            regionService.deleteRegionByName(name);
            model.addAttribute("successMessage", "Регион успешно удален");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении региона");
        }
        return "region_6_delete";
    }
}
