package shuba.practice.connect;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.config.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

public class ConnectDatabase {

    private static final Logger logger = LoggerFactory.getLogger(ConnectDatabase.class);

    private final Config config;

    private CqlSession session;

    public ConnectDatabase(Config config) {
        this.config = config;
    }

    public CqlSession getSession() {
        checkSessionForNull();
        return session;
    }

    private void checkSessionForNull() {
        if (session == null) {
            logger.error("Session is null.");
            throw new IllegalStateException();
        }
    }

    public void createSession() {
        Credentials credentials = getCredentials();
        logger.info("Client ID: {}", credentials.getClientId());
        logger.info("KeySpace: {}", config.getKeySpace());

        try {
            session = CqlSession.builder().
                    withCloudSecureConnectBundle(Paths.get(config.getPassSecureConnectBundle())).
                    withAuthCredentials(credentials.getClientId(), credentials.getSecret()).
                    withKeyspace(config.getKeySpace())
                    .build();
            logger.info("CqlSession successfully created.");
        } catch (Exception e) {
            logger.error("Error creating CqlSession: ", e);
        }
    }

    public void createSessionWithCustomTimeout() {
        Credentials credentials = getCredentials();

        // todo app conf
        // Настройка конфигурации драйвера
        DriverConfigLoader configLoader = DriverConfigLoader.programmaticBuilder()
                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT,
                        Duration.ofSeconds(config.getRequestTimeOut())) // Таймаут запросов
                .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT,
                        Duration.ofSeconds(config.getQueryTimeOut())) // Таймаут инициализации подключения
                .build();

        try {
            session = CqlSession.builder()
                    .withCloudSecureConnectBundle(Paths.get(config.getPassSecureConnectBundle()))
                    .withAuthCredentials(credentials.getClientId(), credentials.getSecret()) // Учетные данные
                    .withKeyspace(config.getKeySpace()) // Выбор keyspace
                    .withConfigLoader(configLoader) // Применение настроек
                    .build();

            logger.info("CqlSession successfully created with custom timeouts.");
        } catch (Exception e) {
            logger.error("Error creating CqlSession: ", e);
        }
    }

    public void createSessionForFastInserts() {
        Credentials credentials = getCredentials();

        // Оптимизация конфигурации драйвера для высокопроизводительных вставок
        DriverConfigLoader configLoader = DriverConfigLoader.programmaticBuilder()
                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30)) // Таймаут запросов
                .withInt(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, 10) // Размер пула локальных соединений
                .withInt(DefaultDriverOption.REQUEST_THROTTLER_MAX_CONCURRENT_REQUESTS, 5000) // Максимальное число параллельных запросов
                .withString(DefaultDriverOption.LOAD_BALANCING_LOCAL_DATACENTER, "datacenter1") // Указание локального датацентра
                .build();

        try {
            // Создание сессии Cassandra
            session = CqlSession.builder()
                    .withCloudSecureConnectBundle(Paths.get(config.getPassSecureConnectBundle())) // Secure connect для AstraDB
                    .withAuthCredentials(credentials.getClientId(), credentials.getSecret()) // Аутентификация
                    .withKeyspace(config.getKeySpace()) // Установка keyspace
                    .withConfigLoader(configLoader) // Применение конфигурации
                    .build();

            logger.info("CqlSession successfully created with fast insert optimizations.");
        } catch (Exception e) {
            logger.error("Error creating CqlSession for fast inserts: ", e);
            throw new RuntimeException("Failed to create CqlSession.", e);
        }
    }


    private Credentials getCredentials() {
        Credentials credentials;
        try {
            credentials = new ObjectMapper().readValue(new File(config.getPassTokenJson()), Credentials.class);
        } catch (IOException e) {
            logger.error("Failed to load credentials from PATH: {}", config.getPassTokenJson(), e);
            throw new RuntimeException(e);
        }
        return credentials;
    }

    public int getNumberOfNodes() {
        checkSessionForNull();
        return session.getMetadata().getNodes().size();
    }

    public void closeSession() {
        logger.info("Trying to close session");
        if (session != null) {
            session.close();
            logger.info("Session closed successfully.");
        }
        else {
            logger.error("Session is null. Can't close it.");
        }
    }
}
