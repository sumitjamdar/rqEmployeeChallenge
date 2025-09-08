package com.reliaquest.api.dto;

import lombok.Data;

@Data
public class DeleteEmployeeInput {
    private String name;

    public DeleteEmployeeInput(String name) {
        this.name = name;
    }
}
