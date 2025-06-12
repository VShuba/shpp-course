package practice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class PropertiesLoaderTest {

    @Test
    void testResourceStreamIsNull() {
        // Подменяем поток с помощью Mockito
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.getResourceAsStream("config.properties")).thenReturn(null);

        Thread.currentThread().setContextClassLoader(classLoader);

        PropertiesLoader loader = new PropertiesLoader();

        // Проверяем, что свойства остались пустыми
        assertTrue(loader.getProperties().isEmpty());
    }

    @Test
    void testIOExceptionHandling() throws IOException {
        // Создаем мок InputStream, который выбрасывает IOException
        InputStream mockInputStream = mock(InputStream.class);
        doThrow(new IOException("Simulated IO exception")).when(mockInputStream).read(any(byte[].class), anyInt(), anyInt());

        // Подменяем поток, чтобы он возвращал наш мок
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.getResourceAsStream("config.properties")).thenReturn(mockInputStream);

        Thread.currentThread().setContextClassLoader(classLoader);

        // Тестируем PropertiesLoader
        PropertiesLoader loader = new PropertiesLoader();

        // Проверяем, что свойства остались пустыми
        assertTrue(loader.getProperties().isEmpty());
    }
}