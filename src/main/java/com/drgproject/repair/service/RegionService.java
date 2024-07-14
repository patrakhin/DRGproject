package com.drgproject.repair.service;

import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.entity.HomeDepot;
import com.drgproject.repair.entity.Region;
import com.drgproject.repair.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Transactional
    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public RegionDTO getRegionById(Long id) {
        Optional<Region> region = regionRepository.findById(id);
        return region.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public RegionDTO getRegionByName(String regionName) {
        Optional<Region> region = regionRepository.findRegionByName(regionName);
        return region.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public RegionDTO createRegion(RegionDTO regionDTO) {
        Region region = new Region(regionDTO.getName()/*, null*/);
        region = regionRepository.save(region);
        return convertToDTO(region);
    }

    @Transactional
    public RegionDTO updateRegion(Long id, RegionDTO regionDTO) {
        Optional<Region> optionalRegion = regionRepository.findById(id);
        if (optionalRegion.isPresent()) {
            Region region = optionalRegion.get();
            region.setName(regionDTO.getName());
            region = regionRepository.save(region);
            return convertToDTO(region);
        }
        return null;
    }

    @Transactional
    public void deleteRegion(Long id) {
        if (regionRepository.existsById(id)) {
            regionRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteRegionByName(String regionName){
        regionRepository.deleteRegionByName(regionName);
    }

    private RegionDTO convertToDTO(Region region) {
        List<Long> depotIds = region.getDepots().stream().map(HomeDepot::getId).toList();
        return new RegionDTO(region.getId(), region.getName(), depotIds);
    }
}
