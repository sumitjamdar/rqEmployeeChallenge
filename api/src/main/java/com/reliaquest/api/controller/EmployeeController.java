package com.reliaquest.api.controller;

import com.reliaquest.api.dto.EmployeeDto;
import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController<EmployeeDto, EmployeeInput> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok( employeeService.getEmployeeById(id));
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<EmployeeDto>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
    }

    @Override
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeInput employeeInput) {
        EmployeeDto created = employeeService.createEmployee(employeeInput);
        return ResponseEntity.ok(created);
    }

    @Override
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteEmployeeByName(@PathVariable String name) {
        boolean deleted = employeeService.deleteEmployeeById(name);
        if (deleted) {
            return ResponseEntity.ok(name);
        } else {
            return ResponseEntity.status(500).body("Failed to delete employee");
        }
    }
}
