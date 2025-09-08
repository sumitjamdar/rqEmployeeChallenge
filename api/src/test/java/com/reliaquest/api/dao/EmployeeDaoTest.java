package com.reliaquest.api.dao;

import com.reliaquest.api.dto.*;
import com.reliaquest.api.service.RestServiceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmployeeDaoTest {

    public static final String URL = "http://localhost:8112/api/v1/employee";
    private RestServiceHelper helper;
    private EmployeeDao dao;

    @BeforeEach
    void setUp() {
        helper = Mockito.mock(RestServiceHelper.class);
        dao = new EmployeeDao(URL, helper);
    }

    @Test
    void testGetAllEmployees() {
        EmployeeDto emp = new EmployeeDto();
        emp.setId("1");
        emp.setEmployeeName("John");

        EmployeeResponseDto response = new EmployeeResponseDto();
        response.setData(List.of(emp));

        when(helper.execute(eq(URL), eq(HttpMethod.GET), isNull(), eq(EmployeeResponseDto.class)))
                .thenReturn(response);

        List<EmployeeDto> employeeDtos = dao.getAllEmployees();
        assertEquals(1, employeeDtos.size());
        assertEquals("John", employeeDtos.get(0).getEmployeeName());
    }

    @Test
    void testCreateEmployee() {
        EmployeeInput input = new EmployeeInput("Jane", 50000, 30, "Engineer");

        EmployeeDto created = new EmployeeDto();
        created.setId("2");
        created.setEmployeeName("Jane");

        SingleEmployeeResponseDto response = new SingleEmployeeResponseDto();
        response.setData(created);

        when(helper.execute(eq(URL), eq(HttpMethod.POST), eq(input), eq(SingleEmployeeResponseDto.class)))
                .thenReturn(response);

        EmployeeDto result = dao.createEmployee(input);
        assertEquals("Jane", result.getEmployeeName());
    }

    @Test
    void testDeleteEmployeeByName() {
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();
        response.setData(true);

        when(helper.execute(eq(URL), eq(HttpMethod.DELETE), any(DeleteEmployeeInput.class), eq(DeleteEmployeeResponse.class)))
                .thenReturn(response);

        boolean result = dao.deleteEmployeeByName("Mark");
        assertTrue(result);
    }
}
