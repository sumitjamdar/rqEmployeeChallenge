package com.reliaquest.api.service;

import com.reliaquest.api.dao.EmployeeDao;
import com.reliaquest.api.dto.EmployeeDto;
import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }

    public List<EmployeeDto> getEmployeesByNameSearch(String searchString) {
        logger.info("Searching employees by name: {}", searchString);
        List<EmployeeDto> employeeDtos = employeeDao.getAllEmployees();
        return employeeDtos.stream()
                .filter(emp -> emp.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                .toList();
    }

    public EmployeeDto getEmployeeById(String id) {
        logger.info("Searching Employee by Id: {}", id);
        List<EmployeeDto> employeeDtos = employeeDao.getAllEmployees();
        return employeeDtos.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Employee with provided id not found"));
    }

    public Integer getHighestSalaryOfEmployees() {
        logger.info("Searching highest paid employee");
        List<EmployeeDto> employeeDtos = employeeDao.getAllEmployees();
        return employeeDtos.stream()
                .map(EmployeeDto::getEmployeeSalary)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public List<EmployeeDto> getTopTenHighestEarningEmployeeNames() {
        logger.info("Searching top ten highest paid employees");
        List<EmployeeDto> employeeDtos = employeeDao.getAllEmployees();
        return employeeDtos.stream()
                .filter(emp -> emp.getEmployeeSalary() != null)
                .sorted(Comparator.comparing(EmployeeDto::getEmployeeSalary).reversed())
                .limit(10)
                .toList();
    }

    public EmployeeDto createEmployee(EmployeeInput employeeInput) {
        logger.info("Creating employee with name:{}", employeeInput.getName());
        return employeeDao.createEmployee(employeeInput);
    }

    public boolean deleteEmployeeById(String name) {
        logger.info("Deleting employee with name: {}", name);
       return employeeDao.deleteEmployeeByName(name);
    }
}


