package com.drgproject.repair.controller;

import com.drgproject.repair.dto.*;
import com.drgproject.repair.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/locos")
public class LocoListController {

    private final LocoListService locoListService;
    private final TypeLocoService typeLocoService;
    private final SystemNameService systemNameService;
    private final BlockOnLocoService blockOnLocoService;
    private final RegionService regionService;
    private final HomeDepotService homeDepotService;

    public LocoListController(LocoListService locoListService, TypeLocoService typeLocoService, SystemNameService systemNameService,
                              BlockOnLocoService blockOnLocoService, RegionService regionService, HomeDepotService homeDepotService) {
        this.locoListService = locoListService;
        this.typeLocoService = typeLocoService;
        this.systemNameService = systemNameService;
        this.blockOnLocoService = blockOnLocoService;
        this.regionService = regionService;
        this.homeDepotService = homeDepotService;
    }

    @GetMapping
    public String getLocosPage(Model model) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        return "locos_1_main";
    }

    @GetMapping("/all")
    public String getAllLocos(Model model) {
        List<LocoListDTO> locoLists = locoListService.getAllLocoLists();
        model.addAttribute("locoLists", locoLists);
        return "locos_2_list"; // Вернуть шаблон Thymeleaf для отображения списка локомотивов
    }

    @GetMapping("/detail/{id}")
    public String getLocoById(@PathVariable Long id, Model model) {
        LocoListDTO locoListDTO = locoListService.getLocoListById(id);
        String locoNumber = locoListDTO.getLocoNumber();
        String typeLoco = locoListDTO.getTypeLoco();
        List<BlockOnLocoDTO> blockOnLoco = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        model.addAttribute("locoList", locoListDTO);
        model.addAttribute("blocks", blockOnLoco);
        return "locos_3_detail"; // Вернуть шаблон Thymeleaf для отображения деталей локомотива
    }

    @GetMapping("/create")
    public String createLocoForm(Model model) {
        model.addAttribute("locoList", new LocoListDTO());

        // Получение всех типов локомотивов и добавление в модель
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);

        // Получение всех типов систем и добавление в модель
        List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
        model.addAttribute("systemNames", systemNames);

        // Получение всех регионов и добавление в модель
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions",regions);

        // Получение всех депо и добавление в модель
        List<HomeDepotDTO> homeDepots = homeDepotService.getAllHomeDepots();
        model.addAttribute("homeDepots",homeDepots);

        return "locos_4_create"; // Шаблон Thymeleaf для создания нового локомотива
    }

    @PostMapping("/create")
    public String createLoco(@ModelAttribute("locoList") LocoListDTO locoListDTO, RedirectAttributes redirectAttributes) {
        LocoListDTO createdLoco = locoListService.createLocoList(locoListDTO);
        if (createdLoco != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Локомотив успешно создан");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании локомотива");
        }
        return "redirect:/locos/all";
    }

    @GetMapping("/edit/{id}")
    public String editLocoForm(@PathVariable Long id, Model model) {
        LocoListDTO locoListDTO = locoListService.getLocoListById(id);
        model.addAttribute("locoList", locoListDTO);

        // Получение всех типов локомотивов и добавление в модель
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);

        // Получение всех типов систем и добавление в модель
        List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
        model.addAttribute("systemNames", systemNames);

        // Получение всех регионов и добавление в модель
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions",regions);

        // Получение всех депо и добавление в модель
        List<HomeDepotDTO> homeDepots = homeDepotService.getAllHomeDepots();
        model.addAttribute("homeDepots",homeDepots);

        return "locos_5_edit"; // Вернуть шаблон Thymeleaf для изменения локомотива
    }

    @PostMapping("/update/{id}")
    public String updateLoco(@PathVariable Long id, @ModelAttribute("locoList") LocoListDTO locoListDTO, Model model) {
        LocoListDTO updatedLoco = locoListService.updateLocoList(id, locoListDTO);
        if (updatedLoco != null) {
            model.addAttribute("updatedLoco", updatedLoco);
            return "locos_5_update_success"; // Страница успешного обновления локомотива
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить локомотив");
            return "locos_5_edit"; // Вернуться к странице редактирования с сообщением об ошибке
        }
    }

    @GetMapping("/delete/{id}")
    public String showDeleteLocoForm(@PathVariable Long id, Model model) {
        LocoListDTO locoListDTO = locoListService.getLocoListById(id);
        if (locoListDTO != null) {
            model.addAttribute("locoList", locoListDTO);
            return "locos_7_delete_confirm"; // Шаблон Thymeleaf для подтверждения удаления
        } else {
            return "locos_2_list"; // Или другое место, если локомотив не найден
        }
    }

    @PostMapping("/deleteById")
    public String deleteLoco(@RequestParam Long id, RedirectAttributes redirectAttributes, Model model) {
        boolean isDeleted = locoListService.deleteLocoList(id);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Локомотив успешно удален");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении локомотива");
        }
        List<LocoListDTO> locoLists = locoListService.getAllLocoLists();
        model.addAttribute("locoLists", locoLists);
        return "locos_2_list"; // Перенаправление на список локомотивов после удаления
    }

    @GetMapping("/search")
    public String getLocoByNumberLoco(@RequestParam("number_loco") String numberLoco, Model model) {
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLoco(numberLoco);
        if (locoListDTO != null) {
            model.addAttribute("locoList", locoListDTO);
            return "locos_7_detail_by_number"; // Вернуть шаблон Thymeleaf для отображения деталей локомотива
        } else {
            return "locos_6_not_found"; // Шаблон для отображения сообщения о том, что локомотив не найден
        }
    }

    @GetMapping("/search_num_type")
    public String getLocoByNumberLocoAndType(@RequestParam("number_loco") String numberLoco,
                                             @RequestParam(value = "type_loco", required = false) String typeLoco,
                                             Model model) {
        if (typeLoco == null || typeLoco.isEmpty()) {
            // Обработка случая, когда тип локомотива не был выбран
            model.addAttribute("errorMessage", "Пожалуйста, выберите тип локомотива.");
            //return "locos_1_main"; // Вернуть на главную страницу с сообщением об ошибке
        }

        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        if (locoListDTO != null) {
            model.addAttribute("locoList", locoListDTO);
            return "locos_8_detail_by_number_and_type"; // Вернуть шаблон Thymeleaf для отображения деталей локомотива
        } else {
            return "locos_6_not_found"; // Шаблон для отображения сообщения о том, что локомотив не найден
        }
    }

    // Новый метод для обработки AJAX-запроса
    @GetMapping("/depots")
    @ResponseBody
    public List<HomeDepotDTO> getDepotsByRegion(@RequestParam String regionName) {
        return homeDepotService.getDepotsByRegion(regionName);
    }
}
