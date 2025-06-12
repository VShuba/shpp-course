package shuba.practice.setters;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import shuba.practice.config.Config;
import shuba.practice.dto.StoreDTO;
import shuba.practice.dto.ValidatableDTO;
import shuba.practice.generate.FakeGenerate;

public class StoreSetter implements Setter {

    private final Config config;
    private final FakeGenerate fakeGenerate;

    public StoreSetter(Config config) {
        this.config = config;
        fakeGenerate = new FakeGenerate();
    }

    @Override
    public BoundStatement getBoundStatement(PreparedStatement ps, ValidatableDTO dto) {
        StoreDTO storeDTO = (StoreDTO) dto;
        return ps.bind(
                storeDTO.getId(),
                storeDTO.getName(),
                storeDTO.getAddress()
        );
    }

    @Override
    public String[] getColumns() {
        return new String[]{"id", "name", "address"};
    }

    @Override
    public String getTableName() {
        return "epicenter.stores";
    }

    @Override
    public int getCount() {
        return config.getCountStores();
    }

    @Override
    public ValidatableDTO getRandomDTO() {
        return fakeGenerate.getRandomStore();
    }
}
