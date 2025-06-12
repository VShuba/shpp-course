package shuba.practice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CategoryDTO implements ValidatableDTO {
    @NotNull(message = "Category id can't be null")
    private UUID id;

    @NotNull(message = "Category name id can't be null")
    private String name;

    public @NotNull(message = "Category id can't be null") UUID getId() {
        return id;
    }

    public void setId(@NotNull(message = "Category id can't be null") UUID id) {
        this.id = id;
    }

    public @NotNull(message = "Category name id can't be null") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Category name id can't be null") String name) {
        this.name = name;
    }
}
