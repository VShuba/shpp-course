package shuba.practice.setters;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shuba.practice.config.Config;
import shuba.practice.dto.ProductTypeByStoreDTO;
import shuba.practice.generate.FakeGenerate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductTypeByStoreSetterTest {

    Config mockConfig;
    FakeGenerate mockFakeGenerate;
    PreparedStatement mockPreparedStatement;
    BoundStatement mockBoundStatement;

    ProductTypeByStoreSetter productTypeByStoreSetter;

    UUID storeId;
    ProductTypeByStoreDTO productTypeByStoreDTO;
    final int expectedCount = 10;

    @BeforeEach
    void setUp() {
        mockConfig = mock(Config.class);
        mockFakeGenerate = mock(FakeGenerate.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockBoundStatement = mock(BoundStatement.class);

        productTypeByStoreSetter = new ProductTypeByStoreSetter(mockConfig);

        productTypeByStoreDTO = new ProductTypeByStoreDTO();
        productTypeByStoreDTO.setProductType("Electronics");
        productTypeByStoreDTO.setStoreId(UUID.randomUUID());
        productTypeByStoreDTO.setStoreAddress("123 Main St");
        productTypeByStoreDTO.setTotalQuantity((short)500);

        storeId = productTypeByStoreDTO.getStoreId();

        when(mockConfig.getProductTypeByStore()).thenReturn(expectedCount);
        when(mockFakeGenerate.getRandomProductTypeByStore()).thenReturn(productTypeByStoreDTO);
    }

    @Test
    void testBoundedStatements() {
        when(mockPreparedStatement.bind(
                productTypeByStoreDTO.getProductType(),
                productTypeByStoreDTO.getStoreId(),
                productTypeByStoreDTO.getStoreAddress(),
                productTypeByStoreDTO.getTotalQuantity()
        )).thenReturn(mockBoundStatement);


        BoundStatement result = productTypeByStoreSetter.getBoundStatement(mockPreparedStatement, productTypeByStoreDTO);

        verify(mockPreparedStatement, times(1)).bind(
                productTypeByStoreDTO.getProductType(),
                productTypeByStoreDTO.getStoreId(),
                productTypeByStoreDTO.getStoreAddress(),
                productTypeByStoreDTO.getTotalQuantity()
        );

        assertEquals(mockBoundStatement, result);
    }

    @Test
    void testGetColumns() {
        String[] columns = productTypeByStoreSetter.getColumns();

        assertArrayEquals(
                new String[]{"product_type", "store_id", "store_address", "total_quantity"},
                columns
        );
    }

    @Test
    void testTableName() {
        String tableName = productTypeByStoreSetter.getTableName();

        assertEquals("epicenter.product_types_by_store", tableName);
    }

    @Test
    void testGetCount() {
        int count = productTypeByStoreSetter.getCount();

        assertEquals(expectedCount, count);
    }

}
