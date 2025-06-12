package shuba.practice.decorator.notifiers.specific.notifiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shuba.practice.decorator.decorator.NotifierDecorator;
import shuba.practice.decorator.notifiers.Notifier;

public class DiscordNotifier extends NotifierDecorator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordNotifier.class);
    private final String channel;

    public DiscordNotifier(Notifier notifier, String channel) {
        super(notifier);
        this.channel = channel;
    }

    @Override
    public void send(String message) {
        super.send(message);
        LOGGER.info("Sending on discord channel {}: {}", channel, message);
    }
}
