package shpp.shuba.spring_jpa_first.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shpp.shuba.spring_jpa_first.util.RandomTax;
import shpp.shuba.spring_jpa_first.dto.EmployeeDTO;
import shpp.shuba.spring_jpa_first.models.Employee;
import shpp.shuba.spring_jpa_first.models.EmploymentStatus;
import shpp.shuba.spring_jpa_first.exceptions.*;
import shpp.shuba.spring_jpa_first.repository.EmployeeRepositoryInterface;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepositoryInterface repository;
    private final RandomTax randomTax;

    // todo MAPSTRUCT!

    @SneakyThrows
    @Override
    public List<EmployeeDTO> findAllEmployees(int page, int perPage) { // +

        if (repository.findAll().isEmpty()) {
            log.warn("The DataBase is empty.");
            throw new EmptyDBException("The DataBase is empty."); // todo delete
        }

        Pageable pageRequest = PageRequest.of(page, perPage);

        Page<Employee> all = repository.findAllBy(pageRequest); // getAll
        Stream<Employee> stream = all.get();
        List<Employee> list = stream.toList();

        return toDTOList(list);
    }

    @SneakyThrows
    @Override
    public EmployeeDTO findEmployee(Long id) { // +

        getEmployeeOrThrow(id);

        return toDTO(repository.findById(id));
    }

    @SneakyThrows
    @Override
    public EmployeeDTO findByEmployeeTaxNumber(String taxIdNumber) { // +

        if (repository.findByTaxIdNumber(taxIdNumber).isEmpty()) {
            log.error("Failed to find this tax id: {}", taxIdNumber);
            throw new InvalidEmployeeId("There is no employee with tax ID like this.");
        }

        return toDTO(repository.findByTaxIdNumber(taxIdNumber).get());
    }


    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO dto) { // +

        if (repository.findByEmail(dto.email()).isPresent()) {
            log.error("Failed to save email cause it's already exists: {}", dto.email());
            throw new UniqueEmailException("Failed to save.");
        }
        return toDTO(repository.save(toEmployeeFromDTO(dto)));
    }

    @SneakyThrows
    @Override
    public EmployeeDTO updateEmployeeByStatus(Long id, EmploymentStatus status) { // +
        // it needs DTO @RequestBody to transform !!! not only one field

        Employee employee = getEmployeeOrThrow(id); // 1
        employee.setStatus(checkStatusOrThrow(status)); // 2

        repository.save(employee);

        return toDTO(employee);
    }

    @SneakyThrows
    @Override
    public EmployeeDTO patchEmployee(Long id, EmployeeDTO dto) { // +

        Employee employee = getEmployeeOrThrow(id);

        // todo а якщо потрібно очистити обьект? зробити через Optinonal

        if (dto.fullName() != null) employee.setFullName(dto.fullName());
        if (dto.status() != null) employee.setStatus(dto.status());
        if (dto.salary() != null) employee.setSalary(dto.salary());
        if (dto.hireDate() != null) employee.setHireDate(dto.hireDate());
        if (dto.email() != null) {
            if (repository.findByEmail(dto.email()).isPresent()) {
                throw new UniqueEmailException("Email is already in use.");
            }
            employee.setEmail(dto.email());
        }

        repository.save(employee);
        return toDTO(employee);
    }

    @Override
    public void deleteEmployeeById(Long id) { // +
        repository.delete(getEmployeeOrThrow(id)); // todo no need to fall
    }

    @SneakyThrows
    private Employee getEmployeeOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                            log.error("Failed to find this id: {}", id);
                            return new InvalidEmployeeId("There is no employee with ID like this.");
                        }
                );
    }


    private EmploymentStatus checkStatusOrThrow(EmploymentStatus status) {
        if (status == null || !EnumSet.allOf(EmploymentStatus.class).contains(status)) {
            throw new IllegalEmployeeStatus("Invalid employment status: " + status);
        }

        return status; // checked int a = "string" and unchecked arr[i] arr.length = 3 ; i=99 Exception
    }

    private Employee toEmployeeFromDTO(EmployeeDTO dto) {
        return Employee.builder()
                .fullName(dto.fullName())
                .taxIdNumber(randomTax.generateRandomTaxIdNumber())
                .status(dto.status())
                .salary(dto.salary())
                .hireDate(dto.hireDate())
                .email(dto.email())
                .build();
    }

    @SneakyThrows
    private EmployeeDTO toDTO(Optional<Employee> employeeOptional) {
        if (employeeOptional.isEmpty()) {
            throw new EmptyDTOException("Employee is empty.");
        }
        Employee employee = employeeOptional.get();
        return new EmployeeDTO(
                employee.getFullName(),
                employee.getStatus(),
                employee.getSalary(),
                employee.getHireDate(),
                employee.getEmail()
        );
    }

    private EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getFullName(),
                employee.getStatus(),
                employee.getSalary(),
                employee.getHireDate(),
                employee.getEmail()
        );
    }

    private List<EmployeeDTO> toDTOList(List<Employee> employeeEntities) {
        return employeeEntities.stream().map(this::toDTO).toList();
    }
}
