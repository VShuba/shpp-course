package shuba.practice.db.setters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shuba.practice.db.dto.CategoryDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

class CategorySetterTest {

    private PreparedStatement mockPreparedStatement;
    private CategorySetter categorySetter;

    @BeforeEach
    void setUp() {
        mockPreparedStatement = mock(PreparedStatement.class);
        categorySetter = new CategorySetter();
    }

    @Test
    void testSetParams() throws SQLException {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Одяг");

        // Act
        categorySetter.setParams(mockPreparedStatement, categoryDTO);

        // Assert
        verify(mockPreparedStatement, times(1)).setString(1, "Одяг");
    }

    @Test
    void testGetColumns() {
        // Act
        String[] columns = categorySetter.getColumns();

        // Assert
        assertArrayEquals(new String[]{"name"}, columns);
    }
}
