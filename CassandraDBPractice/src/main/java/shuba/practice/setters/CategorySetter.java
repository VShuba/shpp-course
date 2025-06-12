package shuba.practice.setters;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import shuba.practice.config.Config;
import shuba.practice.dto.CategoryDTO;
import shuba.practice.dto.ValidatableDTO;
import shuba.practice.generate.FakeGenerate;

public class CategorySetter implements Setter {

    private final Config config;
    private final FakeGenerate fakeGenerate;

    public CategorySetter(Config config) {
        this.config = config;
        fakeGenerate = new FakeGenerate();
    }

    @Override
    public BoundStatement getBoundStatement(PreparedStatement ps, ValidatableDTO dto) {
        CategoryDTO categoryDTO = (CategoryDTO) dto;
        return ps.bind(
                categoryDTO.getId(),
                categoryDTO.getName()
        );
    }

    @Override
    public String[] getColumns() {
        return new String[]{"id", "name"};
    }

    @Override
    public String getTableName() {
        return "epicenter.categories";
    }

    @Override
    public int getCount() {
        return config.getCountCategories();
    }

    @Override
    public ValidatableDTO getRandomDTO() {
        return fakeGenerate.getRandomCategory();
    }

}
