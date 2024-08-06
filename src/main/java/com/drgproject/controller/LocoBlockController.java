package com.drgproject.controller;

import com.drgproject.dto.LocoBlockDto;
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

    private final LocoBlockService locoBlockService;

    public LocoBlockController(LocoBlockService locoBlockService) {
        this.locoBlockService = locoBlockService;
    }

    @GetMapping()
    public String getLocoBlocksPage() {
        return "locoblock_1_main"; // Имя шаблона Thymeleaf без .html
    }

    @GetMapping("/all")
    public String getAllLocoBlocksPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<LocoBlockDto> locoBlocks = Collections.emptyList();
        if ("Администратор".equals(post)) {
            locoBlocks = locoBlockService.getAllLocoBlocks();
        } else if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
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
        String region = (String) session.getAttribute("region");
        String post = (String) session.getAttribute("post");

        List<LocoBlockDto> locoBlock = locoBlockService.getLocoBlockByName(name);

        if (locoBlock != null && "Администратор".equals(post)) {
            model.addAttribute("locoBlock", locoBlock);
        } else if (locoBlock != null && "Регионал".equals(post)) {
            locoBlock = locoBlockService.getLocoBlockByRegionAndName(region, name);
            model.addAttribute("locoBlock", locoBlock);
        } else {
            model.addAttribute("errorMessage", "Блок с таким именем не найден");
        }
        return "locoblock_3_search";
    }

    @GetMapping("/create")
    public String showCreateLocoBlockForm(Model model, HttpSession session) {
        LocoBlockDto locoBlockDto = new LocoBlockDto();
        String post = (String) session.getAttribute("post");
        if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            locoBlockDto.setRegion(region);
        }
        model.addAttribute("locoBlock", locoBlockDto);
        model.addAttribute("post", post); // Добавляем переменную post в модель
        return "locoblock_4_add";
    }

    @PostMapping("/create")
    public String createLocoBlock(@ModelAttribute LocoBlockDto locoBlockDto, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
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
            model.addAttribute("locoBlock", locoBlockDto);
            return "locoblock_5_update";
        } else {
            model.addAttribute("errorMessage", "Блок с таким ID не найден");
            return "locoblock_5_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateLocoBlock(@PathVariable long id, @ModelAttribute LocoBlockDto locoBlockDto, Model model) {
        LocoBlockDto updatedLocoBlock = locoBlockService.updateLocoBlock(id, locoBlockDto);
        if (updatedLocoBlock != null) {
            model.addAttribute("updatedLocoBlock", updatedLocoBlock);
            return "locoblock_5_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные блока");
            return "locoblock_5_update";
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
        String region = (String) session.getAttribute("region");

        try {
            if ("Администратор".equals(post)) {
                locoBlockService.deleteLocoBlockByBlockNumber(blockNumber);
                model.addAttribute("successMessage", "Блок успешно удален");
            } else if ("Регионал".equals(post)) {
                LocoBlockDto locoBlock = locoBlockService.getLocoBlockByBlockNumber(blockNumber);
                if (locoBlock != null && region.equals(locoBlock.getRegion())) {
                    locoBlockService.deleteLocoBlockByBlockNumber(blockNumber);
                    model.addAttribute("successMessage", "Блок успешно удален");
                } else {
                    model.addAttribute("errorMessage", "У вас нет прав на удаление этого блока");
                }
            } else {
                model.addAttribute("errorMessage", "Неверная роль пользователя");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении блока: " + e.getMessage());
        }

        return "locoblock_6_delete";
    }
}
