package shuba.practice.db.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryDTO implements ValidatableDTO {
    private Integer id; // like 'serial'

    @NotNull(message = "Category name can not be null.")
    @Size(min = 4, max = 30, message = "Category size must be between 4 to 30 letters.")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull(message = "Category name can not be null.") @Size(min = 4, max = 30, message = "Category size must be between 4 to 30 letters.") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Category name can not be null.") @Size(min = 4, max = 30, message = "Category size must be between 4 to 30 letters.") String name) {
        this.name = name;
    }
}
