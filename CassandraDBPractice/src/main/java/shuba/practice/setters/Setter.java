package shuba.practice.setters;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import shuba.practice.dto.ValidatableDTO;

public interface Setter {

    BoundStatement getBoundStatement(PreparedStatement ps, ValidatableDTO dto); // setInt set

    String[] getColumns();

    String getTableName();

    int getCount();

    ValidatableDTO getRandomDTO();
}
