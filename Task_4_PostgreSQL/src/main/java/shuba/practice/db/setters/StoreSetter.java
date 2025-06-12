package shuba.practice.db.setters;

import shuba.practice.db.dto.StoreDTO;
import shuba.practice.db.dto.ValidatableDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoreSetter implements StatementSetter {
    @Override
    public void setParams(PreparedStatement ps, ValidatableDTO dto) throws SQLException {
        StoreDTO storeDTO = (StoreDTO) dto;
        ps.setString(1, storeDTO.getName());
        ps.setString(2, storeDTO.getLocation());
    }

    @Override
    public String[] getColumns() {
        return new String[]{"name", "location"};
    }
}
