package com.reliaquest.api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IEmployeeController<Entity, Input> {

    @GetMapping()
    ResponseEntity<List<Entity>> getAllEmployees();

    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Entity>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/{id}")
    ResponseEntity<Entity> getEmployeeById(@PathVariable String id);

    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    @PostMapping()
    ResponseEntity<Entity> createEmployee(@RequestBody Input employeeInput);

    @DeleteMapping("/{name}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String name);
}
