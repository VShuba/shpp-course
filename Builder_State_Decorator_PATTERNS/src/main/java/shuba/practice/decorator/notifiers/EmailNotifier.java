package shuba.practice.decorator.notifiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotifier implements Notifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotifier.class);

    private final String email;

    public EmailNotifier(String email) {
        this.email = email;
    }

    @Override
    public void send(String message) {
        LOGGER.info("Sending on Email {}: {}", email, message);
    }
}
