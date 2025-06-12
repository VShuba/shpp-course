package shuba.practice.decorator.decorator;

import shuba.practice.decorator.notifiers.Notifier;

// Базовий декоратор, що містить посилання на вкладений об'єкт
public class NotifierDecorator implements Notifier {

    protected Notifier notifier;

    public NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void send(String message) {
        notifier.send(message);
    }
}
