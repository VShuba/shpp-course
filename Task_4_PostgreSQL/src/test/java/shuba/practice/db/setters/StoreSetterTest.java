package shuba.practice.db.setters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shuba.practice.db.dto.StoreDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

class StoreSetterTest {

    private PreparedStatement mockPreparedStatement;
    private StoreSetter storeSetter;

    @BeforeEach
    void setUp() {
        mockPreparedStatement = mock(PreparedStatement.class);
        storeSetter = new StoreSetter();
    }

    @Test
    void testSetParams() throws SQLException {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setName("Чудо");
        storeDTO.setLocation("м.Харків вул.Сумська");

        // Act
        storeSetter.setParams(mockPreparedStatement, storeDTO);

        // Assert
        verify(mockPreparedStatement, times(1)).setString(1, "Чудо");
        verify(mockPreparedStatement, times(1)).setString(2, "м.Харків вул.Сумська");
    }

    @Test
    void testGetColumns() {
        // Act
        String[] columns = storeSetter.getColumns();

        // Assert
        assertArrayEquals(new String[]{"name", "location"}, columns);
    }
}
