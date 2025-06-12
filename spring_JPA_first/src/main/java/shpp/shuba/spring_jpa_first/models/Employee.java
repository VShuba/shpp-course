package shpp.shuba.spring_jpa_first.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString(of = {"id", "fullName", "hireDate", "email"})
@EqualsAndHashCode(of = {"id", "taxIdNumber"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String taxIdNumber; // <--- ІПН

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EmploymentStatus status;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false, unique = true)
    String email;
}
