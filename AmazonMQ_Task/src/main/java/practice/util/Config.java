package practice.util;

import java.util.Properties;

public class Config {
    private static final String DEFAULT_BROKER_URL = "ssl://default-broker-url:61617";
    private static final String DEFAULT_USERNAME = "defaultUser";
    private static final String DEFAULT_PASSWORD = "defaultPassword";
    private static final String DEFAULT_QUEUE_NAME = "defaultQueue";
    private static final String DEFAULT_POISON_PILL = "defaultPoisonPill";
    private static final int STOP_TIME = 60; // time is seconds

    private Properties properties = new Properties();

    public Config(PropertiesLoader properties) {
        this.properties = properties.getProperties();
    }

    // Constructor for future testing
    public Config() {

    }

    public String getBrokerUrl() {
        return properties.getProperty("activemq.broker-url", DEFAULT_BROKER_URL);
    }

    public String getUsername() {
        return properties.getProperty("activemq.username", DEFAULT_USERNAME);
    }

    public String getPassword() {
        return properties.getProperty("activemq.password", DEFAULT_PASSWORD);
    }

    public String getQueueName() {
        return properties.getProperty("queue.name", DEFAULT_QUEUE_NAME);
    }

    public int getStopTime() {
        String stopTimeValue = properties.getProperty("stop.time");
        try {
            return Integer.parseInt(stopTimeValue);
        } catch (NumberFormatException e) {
            return STOP_TIME;
        }
    }
    public String getPoisonPill() {
        return properties.getProperty("poison.pill", DEFAULT_POISON_PILL);
    }

    public void setBrokerUrl(String brokerUrl) {
        properties.setProperty("activemq.broker-url", brokerUrl);
    }

    public void setUsername(String username) {
        properties.setProperty("activemq.username", username);
    }

    public void setPassword(String password) {
        properties.setProperty("activemq.password", password);
    }

    public void setQueueName(String queueName) {
        properties.setProperty("queue.name", queueName);
    }

    public void setStopTime(String stopTime) {
        properties.setProperty("stop.time", String.valueOf(stopTime));
    }
}
