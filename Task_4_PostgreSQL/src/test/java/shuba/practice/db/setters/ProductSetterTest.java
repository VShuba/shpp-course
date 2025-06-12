package shuba.practice.db.setters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shuba.practice.db.dto.ProductDTO;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

class ProductSetterTest {

    private PreparedStatement mockPreparedStatement;
    private ProductSetter productSetter;

    @BeforeEach
    void setUp() {
        mockPreparedStatement = mock(PreparedStatement.class);
        productSetter = new ProductSetter();
    }

    @Test
    void testSetParams() throws SQLException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Телефон");
        productDTO.setCategoryId(1);
        productDTO.setPrice(BigDecimal.valueOf(9.99));

        // Act
        productSetter.setParams(mockPreparedStatement, productDTO);

        // Assert
        verify(mockPreparedStatement, times(1)).setString(1, "Телефон");
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).setBigDecimal(3, BigDecimal.valueOf(9.99));
    }

    @Test
    void testGetColumns() {
        // Act
        String[] columns = productSetter.getColumns();

        // Assert
        assertArrayEquals(new String[]{"name", "category_id", "price"}, columns);
    }
}
