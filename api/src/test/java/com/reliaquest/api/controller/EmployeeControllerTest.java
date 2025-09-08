package com.reliaquest.api.controller;

import com.reliaquest.api.dto.EmployeeDto;
import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.exception.ResourceNotFoundException;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    private EmployeeService employeeService;
    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        employeeService = mock(EmployeeService.class);
        controller = new EmployeeController(employeeService);
    }

    private EmployeeDto makeEmployee(String id, String name, Integer salary) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(id);
        dto.setEmployeeName(name);
        dto.setEmployeeSalary(salary);
        return dto;
    }

    @Test
    void testGetAllEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(List.of(makeEmployee("1", "John", 1000)));

        ResponseEntity<List<EmployeeDto>> response = controller.getAllEmployees();

        assertEquals(200,response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void testGetEmployeesByNameSearch() {
        when(employeeService.getEmployeesByNameSearch("John")).thenReturn(List.of(makeEmployee("1", "John", 1000)));

        ResponseEntity<List<EmployeeDto>> response = controller.getEmployeesByNameSearch("John");

        assertEquals(200,response.getStatusCode().value());
        assertEquals("John", response.getBody().get(0).getEmployeeName());
        verify(employeeService).getEmployeesByNameSearch("John");
    }

    @Test
    void testGetEmployeeById_found() {
        when(employeeService.getEmployeeById("1")).thenReturn(makeEmployee("1", "John", 1000));

        ResponseEntity<EmployeeDto> response = controller.getEmployeeById("1");

        assertEquals(200,response.getStatusCode().value());
        assertEquals("John", response.getBody().getEmployeeName());
        verify(employeeService).getEmployeeById("1");
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(5000);

        ResponseEntity<Integer> response = controller.getHighestSalaryOfEmployees();

        assertEquals(200,response.getStatusCode().value());
        assertEquals(5000, response.getBody());
        verify(employeeService).getHighestSalaryOfEmployees();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        EmployeeDto e1 = mock(EmployeeDto.class);
        List<EmployeeDto> expected = Collections.singletonList(e1);
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(expected);

        ResponseEntity<List<EmployeeDto>> response = controller.getTopTenHighestEarningEmployeeNames();

        assertEquals(200,response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(employeeService).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void testCreateEmployee() {
        EmployeeInput input = new EmployeeInput("Jane", 2000, 30, "Engineer");
        EmployeeDto created = makeEmployee("5", "Jane", 2000);
        when(employeeService.createEmployee(input)).thenReturn(created);

        ResponseEntity<EmployeeDto> response = controller.createEmployee(input);

        assertEquals(200,response.getStatusCode().value());
        assertEquals("Jane", response.getBody().getEmployeeName());
        verify(employeeService).createEmployee(input);
    }

    @Test
    void testDeleteEmployeeById_success() {
        when(employeeService.deleteEmployeeById("John")).thenReturn(true);

        ResponseEntity<String> response = controller.deleteEmployeeByName("John");

        assertEquals(200,response.getStatusCode().value());
        assertEquals("John", response.getBody());
        verify(employeeService).deleteEmployeeById("John");
    }

    @Test
    void testDeleteEmployeeById_failure() {
        when(employeeService.deleteEmployeeById("John")).thenReturn(false);

        ResponseEntity<String> response = controller.deleteEmployeeByName("John");

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Failed to delete employee", response.getBody());
    }
}
