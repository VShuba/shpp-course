package shuba.practice.decorator.notifiers.specific.notifiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.decorator.notifiers.Notifier;
import shuba.practice.decorator.decorator.NotifierDecorator;

public class FacebookNotifier extends NotifierDecorator {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookNotifier.class);

    private final String username;

    public FacebookNotifier(Notifier notifier, String username) {
        super(notifier);
        this.username = username;
    }

    @Override
    public void send(String message) {
        super.send(message);
        LOGGER.info("Sending on Facebook to user {} : {} ", username, message);
    }
}
