package practice.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.pojo.Pojo;
import practice.pojo.PojoModifier;
import practice.util.Config;

import javax.jms.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer {
    private final Config config;

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    private Thread senderThread;

    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private static final int N_THREADS = 4;

    private final AtomicInteger sentCount = new AtomicInteger(0);
    private final AtomicInteger queuedCount = new AtomicInteger(0);

    public Producer(Config config) {
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
            messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            logger.info("Trying to start with sender thread.");
            startSenderThread();
            logger.info("Producer started with sender thread.");
        } catch (JMSException e) {
            logger.error("Error starting Producer: {}", e.getMessage());
        }
    }

    private void startSenderThread() {
        senderThread = new Thread(() -> {
            try {
                while (true) {
                    String messageContent = messageQueue.take();
                    if (config.getPoisonPill().equals(messageContent)) {
                        logger.info("SenderThread: received poison pill.");
                        sendMessage(messageContent);
                        break;
                    }

                    sendMessage(messageContent);
                    int count = sentCount.incrementAndGet();

                    if (count % 10000 == 0) {
                        logger.info("SenderThread: Sent {} messages.", count);
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("SenderThread interrupted.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("SenderThread error: {}", e.getMessage());
            }
        });

        senderThread.start();
    }

    public void generateAndSendMessages(int messageCount, int stopTimeInSeconds) {
        try (ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS)) {
            long startTime = System.currentTimeMillis();
            long stopTime = startTime + stopTimeInSeconds * 1000L;

            PojoModifier pojoModifier = new PojoModifier();
            logger.info("Starting message generation and queueing of {} messages", messageCount);

            for (int i = 0; i < messageCount; i++) {
                if (System.currentTimeMillis() >= stopTime) {
                    logger.info("Execution time limit reached.");
                    break;
                }

                executor.submit(() -> {
                    try {
                        Pojo message = pojoModifier.generateRandomPojo();
                        String serializedMessage = pojoModifier.serializePojo(message);
                        messageQueue.put(serializedMessage); // put in queue
                        int count = queuedCount.incrementAndGet(); // Инкрементируем потокобезопасно

                        if (count % 10000 == 0) {
                            logger.info("Queued message #{}", count);
                        }
                    } catch (Exception e) {
                        logger.error("Error generating message: {}", e.getMessage(), e); // todo stacktrace
                    }
                });
            }

            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                logger.warn("Not all tasks completed on time.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error waiting for executor termination: {}", e.getMessage());
        }

        // Отправка poison pill
        try {
            logger.info("Queueing poison pill.");
            messageQueue.put(config.getPoisonPill());
            logger.info("Poison pill successfully queued.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error queueing poison pill: {}", e.getMessage());
        }

        // Ожидаем завершения потока отправки
        try {
            senderThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error waiting for sender thread: {}", e.getMessage());
        }
    }

    private void sendMessage(String messageContent) throws JMSException {
        if (session == null || messageProducer == null) {
            logger.error("Session or producer not initialized. Message cannot be sent.");
            return;
        }
        TextMessage message = session.createTextMessage(messageContent);
        messageProducer.send(message);
    }

    public void stop() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
            logger.info("Producer stopped.");
        } catch (JMSException e) {
            logger.error("Error stopping Producer: {}", e.getMessage());
        }
    }
}
