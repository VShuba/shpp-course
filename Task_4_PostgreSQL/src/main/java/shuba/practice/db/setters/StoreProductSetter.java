package shuba.practice.db.setters;

import shuba.practice.db.dto.StoreProductDTO;
import shuba.practice.db.dto.ValidatableDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoreProductSetter implements StatementSetter {
    @Override
    public void setParams(PreparedStatement ps, ValidatableDTO dto) throws SQLException {
        StoreProductDTO storeProductDTO = (StoreProductDTO) dto;
        ps.setInt(1, storeProductDTO.getStoreId());
        ps.setInt(2, storeProductDTO.getProductId());
        ps.setInt(3, storeProductDTO.getQuantity());
    }

    @Override
    public String[] getColumns() {
        return new String[]{"store_id", "product_id", "quantity"};
    }
}
