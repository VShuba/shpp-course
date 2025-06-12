package shuba.practice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class StoreProductDTO implements ValidatableDTO {
    @NotNull(message = "StoreId can't be null")
    private UUID storeId;

    @NotNull(message = "productId can't be null")
    private UUID productId;

    @NotNull(message = "productName can't be null")
    @Size(min = 4, max = 30, message = "Product name size must be between 4-30 symbol's.")
    private String productName;

    @NotNull(message = "productType can't be null")
    private String productType;

    @Positive(message = "Quantity can not be negative.")
    private short quantity;

    public @NotNull(message = "StoreId can't be null") UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(@NotNull(message = "StoreId can't be null") UUID storeId) {
        this.storeId = storeId;
    }

    public @NotNull(message = "productId can't be null") UUID getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "productId can't be null") UUID productId) {
        this.productId = productId;
    }

    public @NotNull(message = "productName can't be null") @Size(min = 4, max = 30, message = "Product name size must be between 4-30 symbol's.") String getProductName() {
        return productName;
    }

    public void setProductName(@NotNull(message = "productName can't be null") @Size(min = 4, max = 30, message = "Product name size must be between 4-30 symbol's.") String productName) {
        this.productName = productName;
    }

    public @NotNull(message = "productType can't be null") String getProductType() {
        return productType;
    }

    public void setProductType(@NotNull(message = "productType can't be null") String productType) {
        this.productType = productType;
    }

    @Positive(message = "Quantity can not be negative.")
    public short getQuantity() {
        return quantity;
    }

    public void setQuantity(@Positive(message = "Quantity can not be negative.") short quantity) {
        this.quantity = quantity;
    }
}
