package shuba.practice.db.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import shuba.practice.db.dto.StoreProductDTO;
import shuba.practice.db.dto.ValidatableDTO;
import shuba.practice.db.setters.StatementSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DTORepositoryTest {

    private DTORepository repository;
    private BlockingQueue<ValidatableDTO> validDTOs;

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private StatementSetter mockSetter;

    @BeforeEach
    void setUp() throws SQLException {
        //  an obj to test
        repository = new DTORepository();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockSetter = mock(StatementSetter.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        validDTOs = new LinkedBlockingQueue<>();

        fillingQueue result = getFillingQueue();

        validDTOs.add(result.storeProductDTO1());
        validDTOs.add(result.storeProductDTO2());

        when(mockSetter.getColumns()).thenReturn(new String[]{"store_id", "product_id", "quantity"});
    }

    private record fillingQueue(StoreProductDTO storeProductDTO1, StoreProductDTO storeProductDTO2) {

    }
    @Test
    void testSaveAll() throws SQLException {
        repository.saveAll(validDTOs, mockConnection, "test_table", mockSetter);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockConnection).prepareStatement(sqlCaptor.capture());
        assertEquals("INSERT INTO test_table ( store_id, product_id, quantity ) VALUES ( ?, ?, ? )", sqlCaptor.getValue());

        verify(mockSetter, times(2)).setParams(eq(mockPreparedStatement), any(ValidatableDTO.class));

        verify(mockPreparedStatement, times(2)).addBatch();
        verify(mockPreparedStatement, times(1)).executeBatch();
    }

    @Test
    void testSaveAllEmptyQueue() throws SQLException {
        validDTOs.clear();

        repository.saveAll(validDTOs, mockConnection, "test_table", mockSetter);

        verify(mockPreparedStatement, never()).addBatch();
        verify(mockPreparedStatement, never()).executeBatch();
    }

    @Test
    void testSQLExceptionHandling() throws SQLException {
        doThrow(new SQLException("Test SQL exception")).when(mockPreparedStatement).addBatch();

        assertDoesNotThrow(() -> repository.saveAll(validDTOs, mockConnection, "test_table", mockSetter));

        verify(mockPreparedStatement, times(1)).addBatch();
        verify(mockPreparedStatement, never()).executeBatch();
    }

    private static fillingQueue getFillingQueue() {
        StoreProductDTO storeProductDTO1 = new StoreProductDTO();
        storeProductDTO1.setStoreId(1);
        storeProductDTO1.setProductId(2);
        storeProductDTO1.setQuantity(3);

        StoreProductDTO storeProductDTO2 = new StoreProductDTO();
        storeProductDTO2.setStoreId(4);
        storeProductDTO2.setProductId(5);
        storeProductDTO2.setQuantity(6);
        return new fillingQueue(storeProductDTO1, storeProductDTO2);
    }
}
