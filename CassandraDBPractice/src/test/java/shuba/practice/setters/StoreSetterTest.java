package shuba.practice.setters;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shuba.practice.config.Config;
import shuba.practice.dto.StoreDTO;
import shuba.practice.generate.FakeGenerate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StoreSetterTest {

    Config mockConfig;
    FakeGenerate mockFakeGenerate;
    PreparedStatement mockPreparedStatement;
    BoundStatement mockBoundStatement;

    StoreSetter storeSetter;

    UUID storeId;
    StoreDTO storeDTO;
    final int expectedCount = 30;

    @BeforeEach
    void setUp() {
        mockConfig = mock(Config.class);
        mockFakeGenerate = mock(FakeGenerate.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockBoundStatement = mock(BoundStatement.class);

        storeSetter = new StoreSetter(mockConfig);

        storeDTO = new StoreDTO();
        storeDTO.setId(UUID.randomUUID());
        storeDTO.setName("Store A");
        storeDTO.setAddress("456 Elm St");

        storeId = storeDTO.getId();

        when(mockConfig.getCountStores()).thenReturn(expectedCount);
        when(mockFakeGenerate.getRandomStore()).thenReturn(storeDTO);
    }

    @Test
    void testBoundedStatements() {
        when(mockPreparedStatement.bind(
                storeDTO.getId(),
                storeDTO.getName(),
                storeDTO.getAddress()
        )).thenReturn(mockBoundStatement);

        BoundStatement result = storeSetter.getBoundStatement(mockPreparedStatement, storeDTO);

        verify(mockPreparedStatement, times(1)).bind(
                storeDTO.getId(),
                storeDTO.getName(),
                storeDTO.getAddress()
        );

        assertEquals(mockBoundStatement, result);
    }

    @Test
    void testGetColumns() {
        String[] columns = storeSetter.getColumns();

        assertArrayEquals(
                new String[]{"id", "name", "address"},
                columns
        );
    }

    @Test
    void testTableName() {
        String tableName = storeSetter.getTableName();

        assertEquals("epicenter.stores", tableName);
    }

    @Test
    void testGetCount() {
        int count = storeSetter.getCount();

        assertEquals(expectedCount, count);
    }
}
