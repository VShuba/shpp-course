package shuba.practice.setters;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shuba.practice.config.Config;
import shuba.practice.dto.CategoryDTO;
import shuba.practice.generate.FakeGenerate;

import com.datastax.oss.driver.api.core.cql.PreparedStatement;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategorySetterTest {

    Config mockConfig;
    FakeGenerate mockFakeGenerate;
    PreparedStatement mockPreparedStatement;
    BoundStatement mockBoundStatement;


    CategorySetter categorySetter;

    UUID uuid;
    CategoryDTO categoryDTO;
    final int expectedCount = 5;

    @BeforeEach
    void setUp() {
        mockConfig = mock(Config.class);
        mockFakeGenerate = mock(FakeGenerate.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockBoundStatement = mock(BoundStatement.class);

        categorySetter = new CategorySetter(mockConfig);

        categoryDTO = new CategoryDTO();
        categoryDTO.setName("Wood");

        uuid = UUID.randomUUID();
        categoryDTO.setId(uuid);

        Mockito.when(mockConfig.getCountCategories()).thenReturn(expectedCount);
        Mockito.when(mockFakeGenerate.getRandomCategory()).thenReturn(categoryDTO);
    }

    @Test
    void testBoundedStatements() {

        when(mockPreparedStatement.bind(
                categoryDTO.getId(),
                categoryDTO.getName()))
                .thenReturn(mockBoundStatement);

        BoundStatement res = categorySetter.getBoundStatement(mockPreparedStatement, categoryDTO);

        verify(mockPreparedStatement, times(1)).bind(uuid, "Wood");
        assertEquals(res, mockBoundStatement);
    }

    @Test
    void testGetColumns() {
        String[] columns = categorySetter.getColumns();

        assertArrayEquals(new String[]{"id", "name"}, columns);
    }

    @Test
    void testTableName() {
        String tableName = categorySetter.getTableName();

        assertEquals("epicenter.categories", tableName);
    }


    @Test
    void testGetCount() {
        int count = categorySetter.getCount();

        assertEquals(expectedCount, count);
    }
}