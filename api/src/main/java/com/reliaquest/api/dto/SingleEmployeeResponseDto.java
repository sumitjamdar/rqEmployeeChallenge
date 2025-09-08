package com.reliaquest.api.dto;

import lombok.Data;

@Data
public class SingleEmployeeResponseDto {
    private EmployeeDto data;
    private String status;
}
