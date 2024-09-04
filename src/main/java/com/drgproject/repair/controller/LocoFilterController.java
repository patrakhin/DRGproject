package com.drgproject.repair.controller;

import com.drgproject.repair.dto.HomeDepotDTO;
import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.service.LocoFilterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class LocoFilterController {

    private final LocoFilterService locoFilterService;

    public LocoFilterController(LocoFilterService locoFilterService) {
        this.locoFilterService = locoFilterService;
    }

    // Новый метод для обработки AJAX-запроса (получение списка свободных секций)
/*    @GetMapping("/depots")
    @ResponseBody
    public List<LocoListDTO> getLocosByRegion(@RequestParam String homeRegion,
                                              @RequestParam String homeDepot,
                                              @RequestParam String locoType) {
        return locoFilterService.getLocosByRegion(homeRegion, homeDepot, locoType);
    }*/
}
