package practice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void testGetBrokerUrl_DefaultValue() {
        Config config = new Config();
        assertEquals("ssl://default-broker-url:61617", config.getBrokerUrl());
    }

    @Test
    void testGetBrokerUrl_CustomValue() {
        Config config = new Config(new PropertiesLoader());
        config.setBrokerUrl("ssl://custom-broker-url:61617");
        assertEquals("ssl://custom-broker-url:61617", config.getBrokerUrl());
    }

    @Test
    void testGetUsername_DefaultValue() {
        Config config = new Config();
        assertEquals("defaultUser", config.getUsername());
    }

    @Test
    void testGetUsername_CustomValue() {
        Config config = new Config(new PropertiesLoader());
        config.setUsername("customUser");
        assertEquals("customUser", config.getUsername());
    }

    @Test
    void testGetPassword_DefaultValue() {
        Config config = new Config();
        assertEquals("defaultPassword", config.getPassword());
    }

    @Test
    void testGetPassword_CustomValue() {
        Config config = new Config(new PropertiesLoader());
        config.setPassword("customPassword");
        assertEquals("customPassword", config.getPassword());
    }

    @Test
    void testGetQueueName_DefaultValue() {
        Config config = new Config();
        assertEquals("defaultQueue", config.getQueueName());
    }

    @Test
    void testGetQueueName_CustomValue() {
        Config config = new Config(new PropertiesLoader());
        config.setQueueName("customQueue");
        assertEquals("customQueue", config.getQueueName());
    }

    @Test
    void testGetStopTime_DefaultValue() {
        Config config = new Config();
        assertEquals(60, config.getStopTime());
    }

    @Test
    void testGetStopTime_CustomValue() {
        Config config = new Config(new PropertiesLoader());
        config.setStopTime("120");
        assertEquals(120, config.getStopTime());
    }

    @Test
    void testGetStopTime_InvalidValue() {
        Config config = new Config();
        config.setStopTime("invalid");
        assertEquals(60, config.getStopTime());
    }

}
