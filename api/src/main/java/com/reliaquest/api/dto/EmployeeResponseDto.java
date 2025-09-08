package com.reliaquest.api.dto;


import lombok.Data;

import java.util.List;

@Data
public class EmployeeResponseDto {

    private List<EmployeeDto> data;
}

