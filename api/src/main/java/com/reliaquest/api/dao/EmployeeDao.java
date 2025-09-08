package com.reliaquest.api.dao;

import com.reliaquest.api.dto.*;
import com.reliaquest.api.service.RestServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDao {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDao.class);

    private final String employeeApiBaseUrl;
    private final RestServiceHelper restServiceHelper;


    public EmployeeDao(@Value("${employee.api.baseUrl:http://localhost:8112/api/v1/employee}") String employeeApiBaseUrl, RestServiceHelper restServiceHelper) {
        this.employeeApiBaseUrl = employeeApiBaseUrl;
        this.restServiceHelper = restServiceHelper;
    }

    public List<EmployeeDto> getAllEmployees() {
        logger.info("Fetching all employees");
        EmployeeResponseDto response =
                restServiceHelper.execute(employeeApiBaseUrl, HttpMethod.GET, null, EmployeeResponseDto.class);
        List<EmployeeDto> employeeDtos = response.getData();
        logger.info("Fetched: {} employees", employeeDtos);
        return employeeDtos;
    }

    public EmployeeDto createEmployee(EmployeeInput input) {

        SingleEmployeeResponseDto response =
                restServiceHelper.execute(employeeApiBaseUrl, HttpMethod.POST, input, SingleEmployeeResponseDto.class);
        return response.getData();

    }

    public boolean deleteEmployeeByName(String name) {
        DeleteEmployeeInput requestBody = new DeleteEmployeeInput(name);
        DeleteEmployeeResponse response = restServiceHelper.execute(employeeApiBaseUrl, HttpMethod.DELETE, requestBody, DeleteEmployeeResponse.class);
        return Boolean.TRUE.equals(response.getData());
    }
}
