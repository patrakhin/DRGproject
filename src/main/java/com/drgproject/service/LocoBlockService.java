package com.drgproject.service;

import com.drgproject.dto.LocoBlockDto;
import com.drgproject.dto.StorageDto;
import com.drgproject.dto.UserDTO;
import com.drgproject.entity.BlockShipment;
import com.drgproject.entity.LocoBlock;
import com.drgproject.entity.Storage;
import com.drgproject.entity.User;
import com.drgproject.repository.LocoBlockRepository;
import com.drgproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LocoBlockService {

    private final LocoBlockRepository locoBlockRepository;

    public LocoBlockService(LocoBlockRepository locoBlockRepository){
        this.locoBlockRepository = locoBlockRepository;
    }

    @Transactional
    public List<LocoBlockDto> getAllLocoBlock(){
        return locoBlockRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public LocoBlockDto getLocoBlockById(long id){
        return locoBlockRepository.findById(id).map(this::convertToDTO).orElse(null);
    }


    @Transactional
    public UserDTO getUserByNumberTable(String numberTable) {
        return userRepository.findByNumberTable(numberTable).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO updateUser(long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFio(userDTO.getFio());
            user.setPost(userDTO.getPost());
            user.setUnit(userDTO.getUnit());
            user.setRegion(userDTO.getRegion());
            user.setNumberTable(userDTO.getNumberTable());
            user.setLogin(userDTO.getLogin());
            user.setPassword(userDTO.getPassword());
            user = userRepository.save(user);
            return convertToDTO(user);
        }
        return null;
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserByNumberTable(String numberTable) {
        userRepository.deleteById(getUserByNumberTable(numberTable).getId());
    }

    private LocoBlockDto convertToDTO(LocoBlock locoBlock){
        StorageDto storageDto = new StorageDto(locoBlock.getStorage().getId(), locoBlock.getStorage().getName());

        LocoBlockDto locoBlockDto = new LocoBlockDto();
        locoBlockDto.setId(locoBlock.getId());
        locoBlockDto.setStorage(storageDto);
        locoBlockDto.setBlockSupply(locoBlock.getBlockSupply());
        locoBlockDto.setBlockShipment(locoBlock.getBlockShipment());
        locoBlockDto.setSystemType(locoBlock.getSystemType());
        locoBlockDto.setBlockName(locoBlock.getBlockName());
        locoBlockDto.setBlockNumber(locoBlock.getBlockNumber());
        locoBlockDto.setStatus(locoBlock.getStatus());
        locoBlockDto.setDateCreate(locoBlock.getDateCreate());
        return locoBlockDto;
    }

    private LocoBlock convertToEntity(LocoBlockDto locoBlockDto){
        Storage storage = new Storage(locoBlockDto.getStorage().getName(), locoBlockDto.getStorage().getDateCreate());
        LocoBlock locoBlock = new LocoBlock();
        locoBlock.setStorage(storage);
        locoBlock.setBlockSupply(locoBlockDto.getBlockSupply());
        locoBlock.setBlockShipment(locoBlockDto.getBlockShipment());
        locoBlock.setSystemType(locoBlockDto.getSystemType());
        locoBlock.setBlockName(locoBlockDto.getBlockName());
        locoBlock.setBlockNumber(locoBlockDto.getBlockNumber());
        locoBlock.setStatus(locoBlockDto.getStatus());
        return locoBlock;
    }
}
