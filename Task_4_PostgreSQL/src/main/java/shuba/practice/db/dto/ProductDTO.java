package shuba.practice.db.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductDTO implements ValidatableDTO {
    private Integer id;

    @NotNull(message = "CategoryID can not be null.")
    private Integer categoryId;

    @NotNull(message = "Product name can not be null.")
    @Size(min = 5, max = 35, message = "Product name must be between 5 to 35 letters.")
    private String name;

    @NotNull(message = "Product price can not be null.")
    @Min(value = 0, message = "Price of product must be equal 0 or bigger.")
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull(message = "CategoryID can not be null.") Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull(message = "CategoryID can not be null.") Integer categoryId) {
        this.categoryId = categoryId;
    }

    public @NotNull(message = "Product name can not be null.") @Size(min = 5, max = 35, message = "Product name must be between 5 to 35 letters.") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Product name can not be null.") @Size(min = 5, max = 35, message = "Product name must be between 5 to 35 letters.") String name) {
        this.name = name;
    }

    public @NotNull(message = "Product price can not be null.") @Min(value = 0, message = "Price of product must be equal 0 or bigger.") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "Product price can not be null.") @Min(value = 0, message = "Price of product must be equal 0 or bigger.") BigDecimal price) {
        this.price = price;
    }
}
