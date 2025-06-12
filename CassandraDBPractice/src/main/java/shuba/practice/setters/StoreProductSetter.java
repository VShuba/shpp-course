package shuba.practice.setters;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import shuba.practice.config.Config;
import shuba.practice.dto.StoreProductDTO;
import shuba.practice.dto.ValidatableDTO;
import shuba.practice.generate.FakeGenerate;

public class StoreProductSetter implements Setter {

    private final Config config;
    private final FakeGenerate fakeGenerate;

    public StoreProductSetter(Config config) {
        this.config = config;
        fakeGenerate = new FakeGenerate();
    }

    @Override
    public BoundStatement getBoundStatement(PreparedStatement ps, ValidatableDTO dto) {
        StoreProductDTO storeProductDTO = (StoreProductDTO) dto;
        return ps.bind(
                storeProductDTO.getProductId(),
                storeProductDTO.getStoreId(),
                storeProductDTO.getProductName(),
                storeProductDTO.getProductType(),
                storeProductDTO.getQuantity()
        );
    }

    @Override
    public String[] getColumns() {
        return new String[]{"product_id", "store_id", "product_name", "product_type", "quantity"};
    }

    @Override
    public String getTableName() {
        return "epicenter.store_products";
    }

    @Override
    public int getCount() {
        return config.getCountStoreProducts();
    }

    @Override
    public ValidatableDTO getRandomDTO() {
        return fakeGenerate.getRandomStoreProduct();
    }
}
