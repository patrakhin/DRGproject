package com.drgproject.repair.service;

import com.drgproject.repair.dto.TypeLocoDTO;
import com.drgproject.repair.entity.TypeLoco;
import com.drgproject.repair.repository.TypeLocoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TypeLocoService {


    private final TypeLocoRepository typeLocoRepository;

    public TypeLocoService(TypeLocoRepository typeLocoRepository) {
        this.typeLocoRepository = typeLocoRepository;
    }

    public List<TypeLocoDTO> getAllTypeLocos() {
        return typeLocoRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TypeLocoDTO getTypeLocoById(Long id) {
        Optional<TypeLoco> typeLoco = typeLocoRepository.findById(id);
        return typeLoco.map(this::convertToDTO).orElse(null);
    }

    public TypeLocoDTO createTypeLoco(TypeLocoDTO typeLocoDTO) {
        TypeLoco typeLoco = new TypeLoco(typeLocoDTO.getLocoType());
        typeLoco = typeLocoRepository.save(typeLoco);
        return convertToDTO(typeLoco);
    }

    public TypeLocoDTO updateTypeLoco(Long id, TypeLocoDTO typeLocoDTO) {
        Optional<TypeLoco> optionalTypeLoco = typeLocoRepository.findById(id);
        if (optionalTypeLoco.isPresent()) {
            TypeLoco typeLoco = optionalTypeLoco.get();
            typeLoco.setTypeLoco(typeLocoDTO.getLocoType());
            typeLoco = typeLocoRepository.save(typeLoco);
            return convertToDTO(typeLoco);
        }
        return null;
    }

    public boolean deleteTypeLoco(Long id) {
        if (typeLocoRepository.existsById(id)) {
            typeLocoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TypeLocoDTO convertToDTO(TypeLoco typeLoco) {
        return new TypeLocoDTO(typeLoco.getId(), typeLoco.getTypeLoco());
    }
}
