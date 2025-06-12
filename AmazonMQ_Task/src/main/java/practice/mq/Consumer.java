package practice.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.util.Config;
import practice.util.FileModifier;

import javax.jms.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer {
    private final Config config;

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    private Thread readerThread;
    private Thread processorThread;

    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    // Потокобезопасные счётчики
    private final AtomicInteger readCount = new AtomicInteger(0);
    private final AtomicInteger processedCount = new AtomicInteger(0);

    public Consumer(Config config) {
        this.config = config;
    }

    public void start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    config.getUsername(),
                    config.getPassword(),
                    config.getBrokerUrl()
            );
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(config.getQueueName());
            messageConsumer = session.createConsumer(destination);

            logger.info("Trying to start with threads.");
            startThreads();
            logger.info("Consumer started with threads.");
        } catch (JMSException e) {
            logger.error("Error starting Consumer: {}", e.getMessage());
        }
    }

    private void startThreads() {

        // Поток для чтения сообщений из ActiveMQ
        readerThread = new Thread(() -> {
            try {
                while (true) {
                    Message message = messageConsumer.receive(5000);
                    if (message instanceof TextMessage textMessage) {
                        String currentMessage = textMessage.getText();
                        messageQueue.put(currentMessage);
                        int count = readCount.incrementAndGet();

                        if (count % 10000 == 0) {
                            logger.info("ReaderThread: Read {} messages.", count);
                        }

                        if (config.getPoisonPill().equals(currentMessage)) {
                            logger.info("ReaderThread: received poison pill.");
                            break;
                        }
                    } else if (message == null) {
                        logger.warn("ReaderThread: no message received (queue might be empty).");
                        break;
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("ReaderThread interrupted.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("ReaderThread error: {}", e.getMessage());
            }
        });

        // Поток для обработки сообщений и записи в файл
        processorThread = new Thread(() -> {
            try (FileModifier fileModifier = new FileModifier()) {
                while (true) {
                    String message = messageQueue.take();
                    int count = processedCount.incrementAndGet();

                    if (count % 10000 == 0) {
                        logger.info("ProcessorThread: Processed {} messages.", count);
                    }

                    if (config.getPoisonPill().equals(message)) {
                        logger.info("ProcessorThread: received poison pill.");
                        break;
                    }

                    fileModifier.processMessage(message); // Обрабатываем сообщение
                }
            } catch (InterruptedException e) {
                logger.error("ProcessorThread interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        readerThread.start();
        processorThread.start();
    }

    public void consumeMessages() {
        try {
            readerThread.join(); // Ждём завершения потока чтения
            processorThread.join(); // Ждём завершения потока обработки
        } catch (InterruptedException e) {
            logger.error("Error waiting for threads to finish: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
            logger.info("Consumer stopped.");
        } catch (JMSException e) {
            logger.error("Error stopping Consumer: {}", e.getMessage());
        }
    }
}
