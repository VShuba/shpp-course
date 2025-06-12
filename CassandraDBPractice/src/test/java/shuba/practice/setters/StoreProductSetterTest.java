package shuba.practice.setters;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shuba.practice.config.Config;
import shuba.practice.dto.StoreProductDTO;
import shuba.practice.generate.FakeGenerate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StoreProductSetterTest {

    Config mockConfig;
    FakeGenerate mockFakeGenerate;
    PreparedStatement mockPreparedStatement;
    BoundStatement mockBoundStatement;

    StoreProductSetter storeProductSetter;

    UUID productId;
    UUID storeId;
    StoreProductDTO storeProductDTO;
    final int expectedCount = 20;

    @BeforeEach
    void setUp() {
        mockConfig = mock(Config.class);
        mockFakeGenerate = mock(FakeGenerate.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockBoundStatement = mock(BoundStatement.class);

        storeProductSetter = new StoreProductSetter(mockConfig);

        storeProductDTO = new StoreProductDTO();
        storeProductDTO.setProductId(UUID.randomUUID());
        storeProductDTO.setStoreId(UUID.randomUUID());
        storeProductDTO.setProductName("Smartphone");
        storeProductDTO.setProductType("Electronics");
        storeProductDTO.setQuantity((short) 300);

        productId = storeProductDTO.getProductId();
        storeId = storeProductDTO.getStoreId();

        when(mockConfig.getCountStoreProducts()).thenReturn(expectedCount);
        when(mockFakeGenerate.getRandomStoreProduct()).thenReturn(storeProductDTO);
    }

    @Test
    void testBoundedStatements() {
        when(mockPreparedStatement.bind(
                storeProductDTO.getProductId(),
                storeProductDTO.getStoreId(),
                storeProductDTO.getProductName(),
                storeProductDTO.getProductType(),
                storeProductDTO.getQuantity()
        )).thenReturn(mockBoundStatement);

        BoundStatement result = storeProductSetter.getBoundStatement(mockPreparedStatement, storeProductDTO);

        verify(mockPreparedStatement, times(1)).bind(
                storeProductDTO.getProductId(),
                storeProductDTO.getStoreId(),
                storeProductDTO.getProductName(),
                storeProductDTO.getProductType(),
                storeProductDTO.getQuantity()
        );

        assertEquals(mockBoundStatement, result);
    }

    @Test
    void testGetColumns() {
        String[] columns = storeProductSetter.getColumns();

        assertArrayEquals(
                new String[]{"product_id", "store_id", "product_name", "product_type", "quantity"},
                columns
        );
    }

    @Test
    void testTableName() {
        String tableName = storeProductSetter.getTableName();

        assertEquals("epicenter.store_products", tableName);
    }

    @Test
    void testGetCount() {
        int count = storeProductSetter.getCount();

        assertEquals(expectedCount, count);
    }
}