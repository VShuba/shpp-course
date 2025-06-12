package shuba.practice.setters;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import shuba.practice.config.Config;
import shuba.practice.dto.ProductTypeByStoreDTO;
import shuba.practice.dto.ValidatableDTO;
import shuba.practice.generate.FakeGenerate;

public class ProductTypeByStoreSetter implements Setter {

    private final Config config;
    private final FakeGenerate fakeGenerate;

    public ProductTypeByStoreSetter(Config config) {
        this.config = config;
        fakeGenerate = new FakeGenerate();
    }

    @Override
    public BoundStatement getBoundStatement(PreparedStatement ps, ValidatableDTO dto) {
        ProductTypeByStoreDTO type = (ProductTypeByStoreDTO) dto;
        return ps.bind(
                type.getProductType(),
                type.getStoreId(),
                type.getStoreAddress(),
                type.getTotalQuantity()
        );
    }

    @Override
    public String[] getColumns() {
        return new String[]{"product_type", "store_id", "store_address", "total_quantity"};
    }

    @Override
    public String getTableName() {
        return "epicenter.product_types_by_store";
    }

    @Override
    public int getCount() {
        return config.getProductTypeByStore();
    }

    @Override
    public ValidatableDTO getRandomDTO() {
        return fakeGenerate.getRandomProductTypeByStore();
    }
}
