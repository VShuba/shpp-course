package practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.mq.Consumer;
import practice.mq.Producer;
import practice.util.ArgsValidator;
import practice.util.Config;
import practice.util.PropertiesLoader;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
    // try this
        Config config = new Config(new PropertiesLoader());

        Producer producer = new Producer(config);
        Consumer consumer = new Consumer(config);

        long programStartTime = System.currentTimeMillis();
        int numberOfMessages = ArgsValidator.validateArgs(args);

        logger.info("Program Execution Started");
        logger.info("Number of messages to process: {}", numberOfMessages);
        logger.info("======================================");

        long producerExecutionTime = 1;
        long consumerExecutionTime = 1;

        try {
            // Producer Phase
            logger.info("Producer phase started");
            long producerStartTime = System.currentTimeMillis();
            producer.start();
            producer.generateAndSendMessages(numberOfMessages, config.getStopTime());
            producer.stop();
            producerExecutionTime = System.currentTimeMillis() - producerStartTime;
            logger.info("Producer phase finished");

            // Consumer Phase
            logger.info("Consumer phase started");
            long consumerStartTime = System.currentTimeMillis();
            consumer.start();
            consumer.consumeMessages();
            consumer.stop();
            consumerExecutionTime = System.currentTimeMillis() - consumerStartTime;
            logger.info("Consumer phase finished");

        } catch (Exception e) {
            logger.error("An error occurred during program execution: {}", e.getMessage(), e);
        } finally {
            // Final Logging
            long programExecutionTime = System.currentTimeMillis() - programStartTime;

            double producerMps = numberOfMessages / (producerExecutionTime / 1000.0);
            double consumerMps = numberOfMessages / (consumerExecutionTime / 1000.0);
            double totalMps = numberOfMessages / (programExecutionTime / 1000.0);

            logger.info("========================================");
            logger.info("Program Execution Summary:");
            logger.info("Producer - Sent {} messages in {} seconds. MPS: {}",
                    numberOfMessages,
                    producerExecutionTime / 1000.0,
                    producerMps);
            logger.info("Consumer - Processed {} messages in {} seconds. MPS: {}",
                    numberOfMessages,
                    consumerExecutionTime / 1000.0,
                    consumerMps);
            logger.info("Total Execution - Messages: {} | Total Time: {} seconds | Overall MPS: {}",
                    numberOfMessages,
                    programExecutionTime / 1000.0,
                    totalMps);
            logger.info("========================================");
        }
    }
}
