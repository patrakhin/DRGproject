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
    private static final String ADMIN = "Администратор";
    private static final String MANAGER = "Регионал";
    private static final String REGION = "region";
    public static final String STORAGE = "storage";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String STORAGE_5_UPDATE = "storage_5_update";

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
        String region = (String) session.getAttribute(REGION);
        String post = (String) session.getAttribute("post");
        List<StorageDto> storages = Collections.emptyList();
        if (ADMIN.equals(post)) {
            storages = storageService.getAllStorages();
        } else if (MANAGER.equals(post)) {
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
        String region = (String) session.getAttribute(REGION);
        String post = (String) session.getAttribute("post");
        StorageDto storage = storageService.getStorageByName(name);

        if (storage != null && ADMIN.equals(post)) {
            model.addAttribute(STORAGE, storage);
        } else if (storage != null && MANAGER.equals(post)) {
            storage = storageService.getStorageByRegionAndName(region, name);
            model.addAttribute(STORAGE, storage);
        } else {
            model.addAttribute(ERROR_MESSAGE, "Склад с таким именем не найден");
        }
        return "storage_3_search";
    }

    @GetMapping("/create")
    public String showCreateStorageForm(Model model, HttpSession session) {
        StorageDto storageDto = new StorageDto();
        String post = (String) session.getAttribute("post");
        if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
            storageDto.setStorageRegion(region);
        }
        model.addAttribute(STORAGE, storageDto);
        model.addAttribute("post", post); // Добавляем переменную post в модель
        return "storage_4_add";
    }

    @PostMapping("/create")
    public String createStorage(@ModelAttribute StorageDto storageDto, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        if (MANAGER.equals(post)) {
            String region = (String) session.getAttribute(REGION);
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
            model.addAttribute(STORAGE, storageDto);
        } else {
            model.addAttribute(ERROR_MESSAGE, "Склад с таким ID не найден");
        }
        return STORAGE_5_UPDATE;
    }

    @PostMapping("/edit/{id}")
    public String updateStorage(@PathVariable long id, @ModelAttribute StorageDto storageDto, Model model) {
        StorageDto updatedStorage = storageService.updateStorage(id, storageDto);
        if (updatedStorage != null) {
            model.addAttribute("updatedStorage", updatedStorage);
            return "storage_5_update_success";
        } else {
            model.addAttribute(ERROR_MESSAGE, "Не удалось обновить данные склада");
            return STORAGE_5_UPDATE;
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
        String region = (String) session.getAttribute(REGION);
        try {
            if (ADMIN.equals(post)) {
                storageService.deleteStorageByName(name);
                model.addAttribute("successMessage", "Склад успешно удален");
            } else if (MANAGER.equals(post)) {
                StorageDto storage = storageService.getStorageByName(name);
                if (storage != null && region.equals(storage.getStorageRegion())) {
                    storageService.deleteStorageByName(name);
                    model.addAttribute("successMessage", "Склад успешно удален");
                } else {
                    model.addAttribute(ERROR_MESSAGE, "У вас нет прав на удаление этого склада");
                }
            } else {
                model.addAttribute(ERROR_MESSAGE, "Неверная роль пользователя");
            }
        } catch (Exception e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при удалении склада");
        }
        return "storage_6_delete";
    }
}
