package shuba.practice.db.utils.db.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shuba.practice.db.config.Config;
import shuba.practice.db.utils.FileLoader;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;


@Disabled
class DbDDLFunctionsTest {

    private DbDDLFunctions dbDDLFunctions;
    private Config mockConfig;
    private Connection mockConnection;
    private Statement mockStatement;

    @BeforeEach
    void setUp() throws SQLException {
        // Mock Config
        mockConfig = mock(Config.class);
        when(mockConfig.getPassToCreateScript()).thenReturn("create.sql");
        when(mockConfig.getURL()).thenReturn("jdbc:test-db");
        when(mockConfig.getUser()).thenReturn("user");
        when(mockConfig.getPassword()).thenReturn("password");

        // Mock Connection and Statement
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        // Partial Mock for DbDDLFunctions
        dbDDLFunctions = Mockito.spy(new DbDDLFunctions(mockConfig));
        doReturn(mockConnection).when(dbDDLFunctions).getConnection();
        doNothing().when(dbDDLFunctions).ensureConnection();
    }

    @Test
    void testRunDDLScript() throws SQLException {
        // Mock FileLoader response
        mockStatic(FileLoader.class);
        when(FileLoader.loadScript("create.sql")).thenReturn("CREATE TABLE test (id INT);");

        // Test execution
        assertDoesNotThrow(() -> dbDDLFunctions.runDDLScript());

        // Verify behaviors
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).execute("CREATE TABLE test (id INT);");
        verify(mockStatement, times(1)).close();
    }

    @Test
    void testExecuteSQLScriptWithEmptyScript() throws SQLException {
        // Mock FileLoader response with empty script
        mockStatic(FileLoader.class);
        when(FileLoader.loadScript("create.sql")).thenReturn("");

        // Test execution
        dbDDLFunctions.runDDLScript();

        // Verify that execute is not called
        verify(mockConnection, never()).createStatement();
    }

    @Test
    void testExecuteSQLScriptSQLExceptionHandling() throws SQLException {
        // Mock FileLoader response
        mockStatic(FileLoader.class);
        when(FileLoader.loadScript("create.sql")).thenReturn("CREATE TABLE test (id INT);");

        // Throw exception during statement execution
        doThrow(new SQLException("Test exception")).when(mockStatement).execute(anyString());

        // Test execution
        dbDDLFunctions.runDDLScript();

        // Verify behaviors
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).execute("CREATE TABLE test (id INT);");
    }
}
