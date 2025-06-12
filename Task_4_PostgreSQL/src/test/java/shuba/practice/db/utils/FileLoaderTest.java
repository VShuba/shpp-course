package shuba.practice.db.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileLoaderTest {

    @Test
    void testLoadProps_missingFile() {
        FileLoader fileLoader = new FileLoader();

        assertEquals(0, fileLoader.getProperties().size());
    }

    @Test
    void testLoadScript_success() throws IOException {
        String script = "SELECT * FROM table;";
        Path tempFile = Files.createTempFile("test-script", ".sql");
        Files.writeString(tempFile, script);

        String result = FileLoader.loadScript(tempFile.toString());

        assertEquals(script, result);

        // Cleanup
        Files.delete(tempFile);
    }

    @Test
    void testLoadScript_fileNotFound() {
        String result = FileLoader.loadScript("nonexistent.sql");

        assertEquals("", result);
    }
}
