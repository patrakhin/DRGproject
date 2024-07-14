package com.drgproject.repair.controller;

import com.drgproject.repair.dto.HomeDepotDTO;
import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.service.HomeDepotService;
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
@RequestMapping("/home-depots")
public class HomeDepotController {

    private final HomeDepotService homeDepotService;
    private final RegionService regionService;

    public HomeDepotController(HomeDepotService homeDepotService, RegionService regionService) {
        this.homeDepotService = homeDepotService;
        this.regionService = regionService;
    }

    @GetMapping
    public String getHomeDepotsPage() {
        return "home_depot_1_main";
    }

    @GetMapping("/all")
    public String getAllHomeDepotsPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<HomeDepotDTO> depots = Collections.emptyList();
        if ("Администратор".equals(post) || "Регионал".equals(post)) {
            depots = homeDepotService.getAllHomeDepots();
        }
        model.addAttribute("depots", depots);
        return "home_depot_2_list";
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomeDepotDTO> getHomeDepotById(@PathVariable Long id) {
        HomeDepotDTO homeDepotDTO = homeDepotService.getHomeDepotById(id);
        if (homeDepotDTO != null) {
            return ResponseEntity.ok(homeDepotDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<HomeDepotDTO>> getDepotsByRegionId(@PathVariable Long regionId) {
        List<HomeDepotDTO> depots = homeDepotService.getDepotsByRegionId(regionId);
        return ResponseEntity.ok(depots);
    }

    @GetMapping("/create")
    public String showCreateDepotForm(Model model) {
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("homeDepot", new HomeDepotDTO());
        model.addAttribute("regions", regions);
        return "home_depot_3_add";
    }

    @PostMapping("/create")
    public String createHomeDepot(@ModelAttribute HomeDepotDTO homeDepotDTO, Model model) {
        HomeDepotDTO createdHomeDepot = homeDepotService.createHomeDepot(homeDepotDTO);
        String regionName = homeDepotService.getRegionNameByDepotId(createdHomeDepot.getId());
        Long id = regionService.getRegionByName(regionName).getId();
        createdHomeDepot.setRegionId(id);
        createdHomeDepot.setRegionName(regionName); // Добавление имени региона в DTO
        if (createdHomeDepot != null) {
            model.addAttribute("createdHomeDepot", createdHomeDepot);
            model.addAttribute("regionName", regionName); // Добавление имени региона в модель
            return "home_depot_3_add_success";
        } else {
            model.addAttribute("errorMessage", "Ошибка при создании депо");
            return "home_depot_3_add";
        }
    }

    @GetMapping("/edit")
    public String showEditDepotForm(@RequestParam long id, Model model) {
        HomeDepotDTO homeDepotDTO = homeDepotService.getHomeDepotById(id);
        List<RegionDTO> regions = regionService.getAllRegions();
        if (homeDepotDTO != null) {
            model.addAttribute("homeDepot", homeDepotDTO);
            model.addAttribute("regions", regions);
            return "home_depot_4_update";
        } else {
            model.addAttribute("errorMessage", "Депо с таким ID не найдено");
            return "home_depot_4_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateHomeDepot(@PathVariable long id, @ModelAttribute HomeDepotDTO homeDepotDTO, Model model) {
        HomeDepotDTO updatedHomeDepot = homeDepotService.updateHomeDepot(id, homeDepotDTO);
        if (updatedHomeDepot != null) {
            model.addAttribute("updatedHomeDepot", updatedHomeDepot);
            return "home_depot_4_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные депо");
            return "home_depot_4_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteDepotForm(Model model) {
        return "home_depot_5_delete";
    }

    @PostMapping("/deleteById")
    public String deleteHomeDepot(@RequestParam long id, Model model) {
        boolean isDeleted = homeDepotService.deleteHomeDepot(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Депо успешно удалено");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении депо");
        }
        return "home_depot_5_delete";
    }
}
