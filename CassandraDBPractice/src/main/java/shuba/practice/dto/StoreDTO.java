package shuba.practice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class StoreDTO implements ValidatableDTO {
    @NotNull(message = "Store id can't be null")
    private UUID id;

    @NotNull(message = "Store name can't be null")
    private String name;

    @NotNull(message = "Store address can't be null")
    private String address;

    public @NotNull(message = "Store id can't be null") UUID getId() {
        return id;
    }

    public void setId(@NotNull(message = "Store id can't be null") UUID id) {
        this.id = id;
    }

    public @NotNull(message = "Store name can't be null") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Store name can't be null") String name) {
        this.name = name;
    }

    public @NotNull(message = "Store address can't be null") String getAddress() {
        return address;
    }

    public void setAddress(@NotNull(message = "Store address can't be null") String address) {
        this.address = address;
    }
}
