package com.reliaquest.api.service;

import com.reliaquest.api.dao.EmployeeDao;
import com.reliaquest.api.dto.EmployeeDto;
import com.reliaquest.api.dto.EmployeeInput;
import com.reliaquest.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeDao dao;
    private EmployeeService service;

    @BeforeEach
    void setUp() {
        dao = mock(EmployeeDao.class);
        service = new EmployeeService(dao);
    }

    private EmployeeDto makeEmployee(String id, String name, Integer salary) {
        EmployeeDto e = new EmployeeDto();
        e.setId(id);
        e.setEmployeeName(name);
        e.setEmployeeSalary(salary);
        return e;
    }

    @Test
    void testGetAllEmployees() {
        when(dao.getAllEmployees()).thenReturn(List.of(makeEmployee("1", "John", 1000)));
        assertEquals(1, service.getAllEmployees().size());
    }

    @Test
    void testGetEmployeesByNameSearch_caseInsensitive() {
        when(dao.getAllEmployees()).thenReturn(List.of(
                makeEmployee("1", "Alice Smith", 1000),
                makeEmployee("2", "Bob", 2000)
        ));

        List<EmployeeDto> result = service.getEmployeesByNameSearch("alice");
        assertEquals(1, result.size());
        assertEquals("Alice Smith", result.get(0).getEmployeeName());
    }

    @Test
    void testGetEmployeeById_found() {
        when(dao.getAllEmployees()).thenReturn(List.of(makeEmployee("1", "John", 1000)));
        EmployeeDto result = service.getEmployeeById("1");
        assertEquals("John", result.getEmployeeName());
    }

    @Test
    void testGetEmployeeById_notFound() {
        when(dao.getAllEmployees()).thenReturn(List.of(makeEmployee("1", "John", 1000)));
        assertThrows(ResourceNotFoundException.class, () -> service.getEmployeeById("2"));
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        when(dao.getAllEmployees()).thenReturn(List.of(
                makeEmployee("1", "John", 1000),
                makeEmployee("2", "Jane", 5000),
                makeEmployee("3", "Mark", null)
        ));

        int highest = service.getHighestSalaryOfEmployees();
        assertEquals(5000, highest);
    }

    @Test
    void testGetHighestSalaryOfEmployees_emptyList() {
        when(dao.getAllEmployees()).thenReturn(List.of());
        int highest = service.getHighestSalaryOfEmployees();
        assertEquals(0, highest);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<EmployeeDto> employeeDtos = List.of(
                makeEmployee("1", "A", 100),
                makeEmployee("2", "B", 500),
                makeEmployee("3", "C", 300),
                makeEmployee("4", "D", 1000)
        );
        when(dao.getAllEmployees()).thenReturn(employeeDtos);

        List<EmployeeDto> top = service.getTopTenHighestEarningEmployeeNames();

        List<EmployeeDto> expected = employeeDtos.stream().sorted(Comparator.comparing(EmployeeDto::getEmployeeSalary).reversed()).toList();
        assertEquals(expected, top);
    }

    @Test
    void testCreateEmployee() {
        EmployeeInput input = new EmployeeInput("Jane", 2000, 30, "Engineer");
        EmployeeDto created = makeEmployee("5", "Jane", 2000);
        when(dao.createEmployee(input)).thenReturn(created);

        EmployeeDto result = service.createEmployee(input);
        assertEquals("Jane", result.getEmployeeName());
    }

    @Test
    void testDeleteEmployeeById() {
        when(dao.deleteEmployeeByName("John")).thenReturn(true);
        assertTrue(service.deleteEmployeeById("John"));
    }
}
