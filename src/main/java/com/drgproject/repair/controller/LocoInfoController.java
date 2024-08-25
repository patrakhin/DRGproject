package com.drgproject.repair.controller;

import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.dto.TypeLocoDTO;
import com.drgproject.repair.dto.LocoInfoDTO;
import com.drgproject.repair.entity.LocoInfo;
import com.drgproject.repair.service.HomeDepotService;
import com.drgproject.repair.service.RegionService;
import com.drgproject.repair.service.TypeLocoService;
import com.drgproject.repair.service.LocoInfoService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/loco_info")
public class LocoInfoController {

    private final LocoInfoService locoInfoService;
    private final TypeLocoService typeLocoService;
    private final RegionService regionService;
    private final HomeDepotService homeDepotService;

    public LocoInfoController(LocoInfoService locoInfoService, TypeLocoService typeLocoService,
                              RegionService regionService, HomeDepotService homeDepotService) {
        this.locoInfoService = locoInfoService;
        this.typeLocoService = typeLocoService;
        this.regionService = regionService;
        this.homeDepotService = homeDepotService;
    }

    @GetMapping("/manage")
    public String managePanel(){
        return "loco_info_1_manage";
    }

    @GetMapping("/create")
    public String showCreateLocoInfoForm( @RequestParam(required = false) String successMessage,
                                          @RequestParam(required = false) String errorMessage,
                                          Model model) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("locoInfo", new LocoInfoDTO());
        model.addAttribute("typeLocos", typeLocos);
        // Получение всех регионов и добавление в модель
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions",regions);

        // Добавление параметров в модель, если они присутствуют
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "loco_info_2_create";
    }

    @GetMapping("/search")
    public String locoInfoSearch(Model model){
        //Получение серий локомотива и добавление в модель
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        // Получение всех регионов и добавление в модель
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions",regions);

        return "loco_info_4_search";
    }

    @GetMapping("/search/results")
    public String searchLocoInfo(@RequestParam("region") String region,
                                 @RequestParam("locoType") String locoType,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {

        // Проверка валидности значения страницы
        if (page < 0) {
            page = 0; // Устанавливаем минимально допустимое значение
        }

        model.addAttribute("region", region);
        model.addAttribute("locoType", locoType);
        try {
            Page<LocoInfoDTO> locoInfoPage = locoInfoService.getLocoInfoByRegionAndType(region, locoType, page, size);
            model.addAttribute("locoInfoPage", locoInfoPage);
            return "loco_info_5_search_result_region";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "loco_info_5_search_result_region";
        }
    }


    @PostMapping("/create/tps")
    public String createLocoInfo(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String homeDepot,
            @RequestParam(required = false) String typeLoco,
            @RequestParam(required = false) String section1,
            @RequestParam(defaultValue = "false") boolean section1Status,
            @RequestParam(required = false) String section2,
            @RequestParam(defaultValue = "false") boolean section2Status,
            @RequestParam(required = false) String section3,
            @RequestParam(defaultValue = "false") boolean section3Status,
            @RequestParam(required = false) String section4,
            @RequestParam(defaultValue = "false") boolean section4Status,
            Model model
    ) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);

        try {
            // Заполнение недостающих полей строкой "нет" и установка статуса оборудования
            LocoInfoDTO locoInfoDTO = fillMissingFieldsWithDefault(
                    region, homeDepot, typeLoco, section1, section1Status,
                    section2, section2Status, section3, section3Status,
                    section4, section4Status
            );

            // создание локомотива
            locoInfoService.createLocoInfo(locoInfoDTO);

            model.addAttribute("locoInfo", new LocoInfoDTO()); // Очищаем форму
            model.addAttribute("successMessage", "Локомотив успешно создан");

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при создании локомотива: " + e.getMessage());
        }
        return "loco_info_2_create";
    }

    @GetMapping("/delete")
    public String showDeleteDepotForm(Model model) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        return "loco_info_3_delete";
    }

    @PostMapping("/deleteByName")
    public String deleteHomeDepot(@RequestParam String locoUnit, @RequestParam String locoType, Model model) {

        boolean isDeleted = locoInfoService.deleteLocoInfoByLocoUnit(locoUnit, locoType);
        if (isDeleted) {
            model.addAttribute("successMessage", "Локомотив успешно расформирован");
        } else {
            model.addAttribute("errorMessage", "Ошибка при расформировании локомотива");
        }
        return "loco_info_3_delete";
    }

    public LocoInfoDTO fillMissingFieldsWithDefault(
            String region,
            String homeDepot,
            String locoType,
            String section1,
            boolean section1Status,
            String section2,
            boolean section2Status,
            String section3,
            boolean section3Status,
            String section4,
            boolean section4Status
    ) {
        // Создаем новый объект LocoInfoDTO
        LocoInfoDTO locoInfoDTO = new LocoInfoDTO();

        // Устанавливаем значения региона, депо и типа локомотива
        locoInfoDTO.setRegion(region);
        locoInfoDTO.setHomeDepot(homeDepot);
        locoInfoDTO.setLocoType(locoType);

        // Проверка и установка значения по умолчанию для первой секции
        locoInfoDTO.setLocoSection1(section1 != null && !section1.isEmpty() ? section1 : "нет");
        // Установка значения в LocoFit1 на основе статуса переключателя
        locoInfoDTO.setLocoFit1(section1Status ? "оборудован" : "не оборудован");

        // Проверка и установка значения по умолчанию для второй секции
        locoInfoDTO.setLocoSection2(section2 != null && !section2.isEmpty() ? section2 : "нет");
        // Установка значения в LocoFit2 на основе статуса переключателя
        locoInfoDTO.setLocoFit2(section2Status ? "оборудован" : "не оборудован");

        // Проверка и установка значения по умолчанию для третьей секции
        locoInfoDTO.setLocoSection3(section3 != null && !section3.isEmpty() ? section3 : "нет");
        // Установка значения в LocoFit3 на основе статуса переключателя
        locoInfoDTO.setLocoFit3(section3Status ? "оборудован" : "не оборудован");

        // Проверка и установка значения по умолчанию для четвертой секции
        locoInfoDTO.setLocoSection4(section4 != null && !section4.isEmpty() ? section4 : "нет");
        // Установка значения в LocoFit4 на основе статуса переключателя
        locoInfoDTO.setLocoFit4(section4Status ? "оборудован" : "не оборудован");

        // Устанавливаем значение locoUnit
        locoInfoDTO.setLocoUnit(locoInfoService.calculateLocoUnit(section1, section2, section3, section4));

        return locoInfoDTO;
    }
}
