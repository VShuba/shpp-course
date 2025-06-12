package shuba.practice.decorator.notifiers.specific.notifiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.decorator.decorator.NotifierDecorator;
import shuba.practice.decorator.notifiers.Notifier;

public class SMSNotifier extends NotifierDecorator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSNotifier.class);
    private final String phone;

    public SMSNotifier(Notifier notifier, String phone) {
        super(notifier);
        this.phone = phone;
    }

    @Override
    public void send(String message) {
        super.send(message); // new facebook(notifier, page facebook) -> send
        LOGGER.info("Sending SMS on phone {}: {}", phone, message);
    }
}
