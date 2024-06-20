package com.drgproject.repair.service;

import com.drgproject.repair.dto.HomeDepotDTO;
import com.drgproject.repair.entity.HomeDepot;
import com.drgproject.repair.entity.Region;
import com.drgproject.repair.repository.HomeDepotRepository;
import com.drgproject.repair.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeDepotService {

    private final HomeDepotRepository homeDepotRepository;
    private final RegionRepository regionRepository;

    public HomeDepotService(HomeDepotRepository homeDepotRepository, RegionRepository regionRepository) {
        this.homeDepotRepository = homeDepotRepository;
        this.regionRepository = regionRepository;
    }

    public List<HomeDepotDTO> getAllHomeDepots() {
        return homeDepotRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public HomeDepotDTO getHomeDepotById(Long id) {
        Optional<HomeDepot> homeDepot = homeDepotRepository.findById(id);
        return homeDepot.map(this::convertToDTO).orElse(null);
    }

    public HomeDepotDTO createHomeDepot(HomeDepotDTO homeDepotDTO) {
        Optional<Region> region = regionRepository.findById(homeDepotDTO.getRegionId());
        if (region.isPresent()) {
            HomeDepot homeDepot = new HomeDepot(homeDepotDTO.getDepot(), region.get());
            homeDepot = homeDepotRepository.save(homeDepot);
            return convertToDTO(homeDepot);
        }
        return null;
    }

    public HomeDepotDTO updateHomeDepot(Long id, HomeDepotDTO homeDepotDTO) {
        Optional<HomeDepot> optionalHomeDepot = homeDepotRepository.findById(id);
        Optional<Region> region = regionRepository.findById(homeDepotDTO.getRegionId());
        if (optionalHomeDepot.isPresent() && region.isPresent()) {
            HomeDepot homeDepot = optionalHomeDepot.get();
            homeDepot.setDepot(homeDepotDTO.getDepot());
            homeDepot.setRegion(region.get());
            homeDepot = homeDepotRepository.save(homeDepot);
            return convertToDTO(homeDepot);
        }
        return null;
    }

    public boolean deleteHomeDepot(Long id) {
        if (homeDepotRepository.existsById(id)) {
            homeDepotRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<HomeDepotDTO> getDepotsByRegionId(Long regionId) {
        List<HomeDepot> homeDepots = homeDepotRepository.findByRegionId(regionId);
        return homeDepots.stream().map(this::convertToDTO).toList();
    }


    private HomeDepotDTO convertToDTO(HomeDepot homeDepot) {
        return new HomeDepotDTO(homeDepot.getId(), homeDepot.getDepot(), homeDepot.getRegion().getId());
    }
}
