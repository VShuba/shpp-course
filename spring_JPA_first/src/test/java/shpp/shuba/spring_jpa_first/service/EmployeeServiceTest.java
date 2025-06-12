package shpp.shuba.spring_jpa_first.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shpp.shuba.spring_jpa_first.dto.EmployeeDTO;
import shpp.shuba.spring_jpa_first.models.Employee;
import shpp.shuba.spring_jpa_first.exceptions.InvalidEmployeeId;
import shpp.shuba.spring_jpa_first.repository.EmployeeRepositoryInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepositoryInterface repository;

    @InjectMocks
    private EmployeeService service;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .fullName("John")
                .status(null)
                .salary(new BigDecimal("5000"))
                .hireDate(LocalDate.of(2020, 1, 1))
                .email("john@example.com")
                .build();
    }

    @Test
    void findEmployee_success() {
        Long employeeId = 1L;
        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));

        EmployeeDTO result = service.findEmployee(employeeId);

        assertNotNull(result);
        assertEquals("John", result.fullName());
        assertEquals(new BigDecimal("5000"), result.salary());
        assertEquals(LocalDate.of(2020, 1, 1), result.hireDate());

        verify(repository, times(2)).findById(employeeId);
    }

    @Test
    void findEmployee_notFound() {
        Long employeeId = 2L;
        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(InvalidEmployeeId.class, () -> service.findEmployee(employeeId));

        verify(repository, times(1)).findById(employeeId);
    }
}
