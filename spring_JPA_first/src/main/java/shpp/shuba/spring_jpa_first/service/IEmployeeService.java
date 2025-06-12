package shpp.shuba.spring_jpa_first.service;

import jakarta.transaction.Transactional;
import shpp.shuba.spring_jpa_first.dto.EmployeeDTO;
import shpp.shuba.spring_jpa_first.models.EmploymentStatus;

import java.util.List;


public interface IEmployeeService {

    List<EmployeeDTO> findAllEmployees(int page, int size);

    EmployeeDTO findEmployee(Long id);

    EmployeeDTO findByEmployeeTaxNumber(String taxIdNumber);

    @Transactional
    EmployeeDTO saveEmployee(EmployeeDTO employee);

    @Transactional
    EmployeeDTO updateEmployeeByStatus(Long id, EmploymentStatus status);

    @Transactional
    EmployeeDTO patchEmployee(Long id, EmployeeDTO dto);

    @Transactional
    void deleteEmployeeById(Long id);
}
