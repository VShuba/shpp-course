package shuba.practice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class ProductTypeByStoreDTO implements ValidatableDTO{
    @NotNull(message = "storeId id can't be null")
    private UUID storeId;

    @NotNull(message = "productType id can't be null")
    private String productType;

    @NotNull(message = "storeAddress id can't be null")
    private String storeAddress;

    @Positive(message = "Quantity can not be negative.")
    private Short totalQuantity;

    public @NotNull(message = "productType id can't be null") String getProductType() {
        return productType;
    }

    public void setProductType(@NotNull(message = "productType id can't be null") String productType) {
        this.productType = productType;
    }

    public @NotNull(message = "storeId id can't be null") UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(@NotNull(message = "storeId id can't be null") UUID storeId) {
        this.storeId = storeId;
    }

    public @NotNull(message = "storeAddress id can't be null") String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(@NotNull(message = "storeAddress id can't be null") String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public @Positive(message = "Quantity can not be negative.") Short getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(@Positive(message = "Quantity can not be negative.") Short totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
