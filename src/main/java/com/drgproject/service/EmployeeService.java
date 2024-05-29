package com.drgproject.service;

import com.drgproject.dto.EmployeeDto;
import com.drgproject.dto.UserDTO;
import com.drgproject.entity.Employee;
import com.drgproject.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    public EmployeeService(EmployeeRepository employeeRepository, UserService userService) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
    }

    //
    @Transactional
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public EmployeeDto getEmployeeById(long id) {
        return employeeRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public EmployeeDto createEmployee(String numberTable) {
        UserDTO userDTO = userService.getUserByNumberTable(numberTable);
        if (userDTO == null) {
            throw new IllegalArgumentException("User with numberTable " + numberTable + " not found");
        }

        Employee employee = new Employee(
                userDTO.getFio(),
                userDTO.getPost(),
                userDTO.getUnit(),
                userDTO.getRegion(),
                userDTO.getNumberTable()
        );

        employee = employeeRepository.save(employee);
        return convertToDTO(employee);
    }

    @Transactional
    public EmployeeDto updateEmployee(long id, String numberTable) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            UserDTO userDTO = userService.getUserByNumberTable(numberTable);
            if (userDTO == null) {
                throw new IllegalArgumentException("User with numberTable " + numberTable + " not found");
            }

            employee.setFio(userDTO.getFio());
            employee.setPost(userDTO.getPost());
            employee.setUnit(userDTO.getUnit());
            employee.setRegion(userDTO.getRegion());
            employee.setNumberTable(userDTO.getNumberTable());

            employee = employeeRepository.save(employee);
            return convertToDTO(employee);
        }
        return null;
    }

    @Transactional
    public void deleteEmployee(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employeeRepository.delete(employee);
    }

    private EmployeeDto convertToDTO(Employee employee) {
        return new EmployeeDto(
                employee.getFio(),
                employee.getPost(),
                employee.getUnit(),
                employee.getRegion(),
                employee.getNumberTable()
        );
    }

    private Employee convertToEntity(EmployeeDto employeeDto) {
        return new Employee(
                employeeDto.getFio(),
                employeeDto.getPost(),
                employeeDto.getUnit(),
                employeeDto.getRegion(),
                employeeDto.getNumberTable()
        );
    }
}
