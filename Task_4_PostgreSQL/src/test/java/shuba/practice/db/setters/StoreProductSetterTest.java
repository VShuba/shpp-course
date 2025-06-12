package shuba.practice.db.setters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shuba.practice.db.dto.StoreProductDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

class StoreProductSetterTest {

    private PreparedStatement mockPreparedStatement;
    private StoreProductSetter storeProductSetter;

    @BeforeEach
    void setUp() {
        mockPreparedStatement = mock(PreparedStatement.class);
        storeProductSetter = new StoreProductSetter();
    }

    @Test
    void testSetParams() throws SQLException {
        StoreProductDTO storeProductDTO = new StoreProductDTO();
        storeProductDTO.setStoreId(1); // 1 pos
        storeProductDTO.setProductId(2); // 2 pos
        storeProductDTO.setQuantity(3); // 3 pos

        // Act
        storeProductSetter.setParams(mockPreparedStatement, storeProductDTO);

        // Assert
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).setInt(2, 2);
        verify(mockPreparedStatement, times(1)).setInt(3, 3);
    }

    @Test
    void testGetColumns() {
        // Act
        String[] columns = storeProductSetter.getColumns();

        // Assert
        assertArrayEquals(new String[]{"store_id", "product_id", "quantity"}, columns);
    }
}
