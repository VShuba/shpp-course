package shuba.practice.db.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StoreDTO implements ValidatableDTO {
    private Integer id;

    @NotNull(message = "Store name can not be null.")
    @Size(min = 6, max = 20, message = "Store name must be between 6 to 20 letters.")
    private String name;

    @Size(max = 30, message = "Store location must be between 0 to 30 letters.")
    private String location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull(message = "Store name can not be null.") @Size(min = 6, max = 20, message = "Store name must be between 6 to 20 letters.") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Store name can not be null.") @Size(min = 6, max = 20, message = "Store name must be between 6 to 20 letters.") String name) {
        this.name = name;
    }

    public @Size(max = 30, message = "Store location must be between 0 to 30 letters.") String getLocation() {
        return location;
    }

    public void setLocation(@Size(max = 30, message = "Store location must be between 0 to 30 letters.") String location) {
        this.location = location;
    }
}
