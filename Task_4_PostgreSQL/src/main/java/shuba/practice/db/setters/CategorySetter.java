package shuba.practice.db.setters;

import shuba.practice.db.dto.CategoryDTO;
import shuba.practice.db.dto.ValidatableDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategorySetter implements StatementSetter {
    @Override
    public void setParams(PreparedStatement ps, ValidatableDTO dto) throws SQLException {
        CategoryDTO categoryDTO = (CategoryDTO) dto;
        ps.setString(1, categoryDTO.getName());
    }

    @Override
    public String[] getColumns() {
        return new String[]{"name"};
    }
}
