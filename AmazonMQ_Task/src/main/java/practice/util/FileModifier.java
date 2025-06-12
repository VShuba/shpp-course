package practice.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.pojo.Pojo;
import practice.pojo.PojoModifier;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.Map;

public class FileModifier implements AutoCloseable {
    private BufferedWriter validWriter;
    private BufferedWriter invalidWriter;


    private static final Logger logger = LoggerFactory.getLogger(FileModifier.class);

    private PojoModifier pojoModifier;
    private ObjectMapper objectMapper;

    public FileModifier() {
        createFiles();
    }

    private void initializeDependencies() {
        if (pojoModifier == null) {
            pojoModifier = new PojoModifier();
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    public void processMessage(String message) {
        BufferedWriter targetWriter;
        try {
            initializeDependencies();

            Pojo pojo = pojoModifier.deserializePojo(message);
            Map<String, String> validationErrors = pojoModifier.validatePojo(pojo);

            targetWriter = validationErrors.isEmpty() ? validWriter : invalidWriter;
            String output = validationErrors.isEmpty()
                    ? message + "\n"
                    : message + "," + objectMapper.writeValueAsString(validationErrors) + "\n";

            synchronized (targetWriter) {
                targetWriter.write(output);
            }
        } catch (IOException e) {
            logger.error("Помилка запису у файл для повідомлення: {}", message, e);
            throw new IllegalStateException("Помилка запису у файл", e);
        }
    }


    @Override
    public void close() {
        try {
            if (validWriter != null) {
                validWriter.close();
            }
            if (invalidWriter != null) {
                invalidWriter.close();
            }
            logger.info("Файли успішно закриті.");
        } catch (IOException e) {
            logger.error("Помилка при закритті файлів: {}", e.getMessage());
        }
    }

    public void createFiles() {
        try {
            this.validWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("valid.csv", false), StandardCharsets.UTF_8));
            this.invalidWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("invalid.csv", false), StandardCharsets.UTF_8));
            logger.info("Файли valid.csv та invalid.csv ініціалізовані.");
        } catch (IOException e) {
            logger.error("Помилка при створенні файлів: {}", e.getMessage());
        }
    }
}
