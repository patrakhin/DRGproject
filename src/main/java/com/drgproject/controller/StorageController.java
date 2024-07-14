package com.drgproject.controller;

import com.drgproject.dto.StorageDto;
import com.drgproject.service.StorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/storages")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping()
    public String getStoragesPage() {
        return "storage_1_main"; // Имя шаблона Thymeleaf без .html
    }

    @GetMapping("/all")
    public String getAllStoragesPage(Model model, HttpSession session) {
        String region = (String) session.getAttribute("region");
        String post = (String) session.getAttribute("post");
        List<StorageDto> storages = Collections.emptyList();
        if ("Администратор".equals(post)) {
            storages = storageService.getAllStorages();
        } else if ("Регионал".equals(post)) {
            storages = storageService.getStoragesByRegion(region);
        }
        model.addAttribute("storages", storages);
        return "storage_2_list";
    }

    @GetMapping("/{id}")
    public ResponseEntity<StorageDto> getStorageById(@PathVariable Long id) {
        StorageDto storage = storageService.getStorageById(id);
        if (storage != null) {
            return ResponseEntity.ok(storage);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "storage_3_search";
    }

    @GetMapping("/byName")
    public String getStorageByName(@RequestParam String name, Model model, HttpSession session) {
        String region = (String) session.getAttribute("region");
        String post = (String) session.getAttribute("post");
        StorageDto storage = storageService.getStorageByName(name);

        if (storage != null && "Администратор".equals(post)) {
            model.addAttribute("storage", storage);
        } else if (storage != null && "Регионал".equals(post)) {
            storage = storageService.getStorageByRegionAndName(region, name);
            model.addAttribute("storage", storage);
        } else {
            model.addAttribute("errorMessage", "Склад с таким именем не найден");
        }
        return "storage_3_search";
    }

    @GetMapping("/create")
    public String showCreateStorageForm(Model model, HttpSession session) {
        StorageDto storageDto = new StorageDto();
        String post = (String) session.getAttribute("post");
        if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            storageDto.setStorageRegion(region);
        }
        model.addAttribute("storage", storageDto);
        model.addAttribute("post", post); // Добавляем переменную post в модель
        return "storage_4_add";
    }

    @PostMapping("/create")
    public String createStorage(@ModelAttribute StorageDto storageDto, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            storageDto.setStorageRegion(region);
        }
        StorageDto createdStorage = storageService.createStorage(storageDto);
        model.addAttribute("createdStorage", createdStorage);
        return "storage_4_add_success";
    }

    @GetMapping("/edit")
    public String showEditStorageForm(@RequestParam long id, Model model) {
        StorageDto storageDto = storageService.getStorageById(id);
        if (storageDto != null) {
            model.addAttribute("storage", storageDto);
            return "storage_5_update";
        } else {
            model.addAttribute("errorMessage", "Склад с таким ID не найден");
            return "storage_5_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateStorage(@PathVariable long id, @ModelAttribute StorageDto storageDto, Model model) {
        StorageDto updatedStorage = storageService.updateStorage(id, storageDto);
        if (updatedStorage != null) {
            model.addAttribute("updatedStorage", updatedStorage);
            return "storage_5_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные склада");
            return "storage_5_update";
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorage(@PathVariable long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/delete")
    public String showDeleteStorageForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "storage_6_delete";
    }

    @PostMapping("/deleteByName")
    public String deleteStorageByName(@RequestParam String name, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        String region = (String) session.getAttribute("region");

        try {
            if ("Администратор".equals(post)) {
                storageService.deleteStorageByName(name);
                model.addAttribute("successMessage", "Склад успешно удален");
            } else if ("Регионал".equals(post)) {
                StorageDto storage = storageService.getStorageByName(name);
                if (storage != null && region.equals(storage.getStorageRegion())) {
                    storageService.deleteStorageByName(name);
                    model.addAttribute("successMessage", "Склад успешно удален");
                } else {
                    model.addAttribute("errorMessage", "У вас нет прав на удаление этого склада");
                }
            } else {
                model.addAttribute("errorMessage", "Неверная роль пользователя");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении склада");
        }

        return "storage_6_delete";
    }
}
