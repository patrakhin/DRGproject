package com.drgproject.controller;

import com.drgproject.dto.EmployeeDto;
import com.drgproject.dto.EmployeeRequestDto;
import com.drgproject.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Получение списка всех сотрудников.
     *
     * @return список сотрудников
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        List<EmployeeDto> employeeDto = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeDto);
    }

    /**
     * Получение сотрудника по его ID.
     *
     * @param id идентификатор сотрудника
     * @return сотрудник, если найден, иначе 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable long id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        if(employeeDto != null){
            return ResponseEntity.ok(employeeDto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Создание нового сотрудника на основе табельного номера пользователя.
     *
     * @param employeeRequestDto DTO табельного номера пользователя
     * @return созданный сотрудник
     */
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeRequestDto employeeRequestDto){
        try {
            EmployeeDto newEmployee = employeeService.createEmployee(employeeRequestDto.getNumberTable());
            return ResponseEntity.ok(newEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Обновление информации о сотруднике по его ID на основе табельного номера пользователя.
     *
     * @param id          идентификатор сотрудника
     * @param numberTable табельный номер пользователя
     * @return обновленный сотрудник, если найден, иначе 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable long id, @RequestParam String numberTable) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, numberTable);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаление сотрудника по его ID.
     *
     * @param id идентификатор сотрудника
     * @return статус 204, если сотрудник удален, иначе 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
