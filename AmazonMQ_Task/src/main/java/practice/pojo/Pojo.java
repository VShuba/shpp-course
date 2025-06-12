package practice.pojo;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Objects;

public class Pojo {
    @NotNull(message = "First name is compulsory")
    @NotBlank(message = "First name is compulsory")
    @Pattern(regexp = ".*a.*", message = "First name must contain at least one 'a'")
    @Length(min = 7, message = "First name must be at least 7 characters")
    private String name;

    @Pattern(regexp = "\\d{8}(?:\\d{5})?", message = "Eddr has invalid format")
    private String eddr;

    @Min(value = 10, message = "Count must be bigger than or equal to 10")
    private int count;

    private LocalDateTime createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEddr() {
        return eddr;
    }

    public void setEddr(String eddr) {
        this.eddr = eddr;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pojo pojo = (Pojo) o;
        return count == pojo.count &&
                Objects.equals(name, pojo.name) &&
                Objects.equals(eddr, pojo.eddr) &&
                Objects.equals(createdAt, pojo.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eddr, count, createdAt);
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "name='" + name + '\'' +
                ", eddr='" + eddr + '\'' +
                ", count=" + count +
                ", createdAt=" + createdAt +
                '}';
    }
}
