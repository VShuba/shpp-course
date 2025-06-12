package shuba.practice.db.setters;

import shuba.practice.db.dto.ProductDTO;
import shuba.practice.db.dto.ValidatableDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductSetter implements StatementSetter {
    @Override
    public void setParams(PreparedStatement ps, ValidatableDTO dto) throws SQLException {
        ProductDTO productDTO = (ProductDTO) dto;
        ps.setString(1, productDTO.getName());
        ps.setInt(2, productDTO.getCategoryId());
        ps.setBigDecimal(3, productDTO.getPrice());
    }

    @Override
    public String[] getColumns() {
        return new String[]{"name", "category_id", "price"};
    }
}
