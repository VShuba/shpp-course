package shuba.practice.db.setters;

import shuba.practice.db.dto.ValidatableDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementSetter {
    void setParams(PreparedStatement ps, ValidatableDTO dto) throws SQLException;

    String[] getColumns();
}
