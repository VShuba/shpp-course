package shuba.practice.db.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StoreProductDTO implements ValidatableDTO {

    private Integer id;

    @NotNull(message = "StoreID can not be null.")
    private Integer storeId;

    @NotNull(message = "ProductID can not be null.")
    private Integer productId;

    @NotNull(message = "Quantity can not be null")
    @Min(value = 0, message = "Quantity must be bigger or equal 0.")
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull(message = "StoreID can not be null.") Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(@NotNull(message = "StoreID can not be null.") Integer storeId) {
        this.storeId = storeId;
    }

    public @NotNull(message = "ProductID can not be null.") Integer getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "ProductID can not be null.") Integer productId) {
        this.productId = productId;
    }

    public @NotNull(message = "Quantity can not be null") @Min(value = 0, message = "Quantity must be bigger or equal 0.") Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "Quantity can not be null") @Min(value = 0, message = "Quantity must be bigger or equal 0.") Integer quantity) {
        this.quantity = quantity;
    }
}
