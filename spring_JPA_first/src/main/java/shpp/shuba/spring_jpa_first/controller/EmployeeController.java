package shpp.shuba.spring_jpa_first.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import shpp.shuba.spring_jpa_first.dto.EmployeeDTO;
import shpp.shuba.spring_jpa_first.models.EmploymentStatus;
import shpp.shuba.spring_jpa_first.service.IEmployeeService;

import java.util.List;

@Tag(name = "Employees", description = "Operations with employees")
@RestController
@RequestMapping("/api/v1/shpp")
@AllArgsConstructor
public class EmployeeController {

    private final IEmployeeService service;

    @Operation(summary = "Find all employees", description = "Returns all employees in DB if it exists.")
    @ApiResponse(responseCode = "200", description = "Successfully find all employees in DB")
    @ApiResponse(responseCode = "404", description = "Failed to find employees cause DB is empty")
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(
            @Parameter(description = "Number of page starts from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of elements by page", example = "10")
            @RequestParam(defaultValue = "10") int size

    ) {
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(service.findAllEmployees(page, size));
    }

    @Operation(summary = "Find employee by ID", description = "Returns an employee by his ID if it exists.")
    @ApiResponse(responseCode = "200", description = "Successfully find employee by his ID")
    @ApiResponse(responseCode = "404", description = "Failed to find cause there is no employee with ID like this.")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findEmployee(
            @Parameter(description = "ID of employee", example = "1")
            @PathVariable Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findEmployee(id));
    }

    @Operation(summary = "Find employee by Tax number", description = "Returns an employee by his Tax number if it exists.")
    @ApiResponse(responseCode = "200", description = "Successfully find employee by his Tax ID")
    @ApiResponse(responseCode = "404", description = "Failed to find cause there is no employee with Tax ID like this.")
    @GetMapping("/tax/{id}")
    public ResponseEntity<EmployeeDTO> findEmployeeByTaxId(
            @Parameter(description = "Tax ID of employee", example = "6372147990")
            @PathVariable String id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findByEmployeeTaxNumber(id));
    }

    @Operation(summary = "Saving employee", description = "Returns an employee if it unique by email.")
    @ApiResponse(responseCode = "200", description = "Successfully saved to DB")
    @ApiResponse(responseCode = "400", description = "Failed to save cause you need an Unique Email or" +
            " you input not valid fields")
    @PostMapping("/save")
    public ResponseEntity<EmployeeDTO> saveEmployee(@Valid @RequestBody EmployeeDTO dto) {
        //bindingResult.

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.saveEmployee(dto));
    }

    @Operation(summary = "Updating status of employee by his ID", description = "Returns an updated employee if it exists.")
    @ApiResponse(responseCode = "200", description = "Successfully updated employee.")
    @ApiResponse(responseCode = "400", description = "Invalid status")
    @ApiResponse(responseCode = "404", description = "Failed to find employee with id like this")
    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployeeByStatus(
            @Parameter(description = "ID of employee", example = "1")
            @PathVariable Long id,
            @RequestParam EmploymentStatus status) {

        // it needs DTO @RequestBody to transform !!! not only one field
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateEmployeeByStatus(id, status));
    }

    @Operation(summary = "Updating employee by all fields except ID", description = "Returns an updated employee if it exists and it was an unique email.")
    @ApiResponse(responseCode = "200", description = "Successfully updated employee")
    @ApiResponse(responseCode = "400", description = "Email like this is already exists.")
    @ApiResponse(responseCode = "404", description = "There is no ID like this")
    @PatchMapping("/patch/{id}")
    public ResponseEntity<EmployeeDTO> patchEmployee(
            @Parameter(description = "ID of employee", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.patchEmployee(id, dto));
    }



    @Operation(summary = "Deleting employee by ID", description = "Deleting employee by his ID if it exists.")
    @ApiResponse(responseCode = "200", description = "Deleting employee was successful.")
    @ApiResponse(responseCode = "404", description = "Deleting employee was failed by not existing ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployeeByID(
            @Parameter(description = "ID of employee", example = "1")
            @PathVariable Long id) {

        service.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

}
