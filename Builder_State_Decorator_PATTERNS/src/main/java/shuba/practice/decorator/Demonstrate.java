package shuba.practice.decorator;

import shuba.practice.decorator.notifiers.EmailNotifier;
import shuba.practice.decorator.notifiers.Notifier;
import shuba.practice.decorator.notifiers.specific.notifiers.DiscordNotifier;
import shuba.practice.decorator.notifiers.specific.notifiers.FacebookNotifier;
import shuba.practice.decorator.notifiers.specific.notifiers.SMSNotifier;

public class Demonstrate {

    public static void main(String[] args) {
        Notifier notifier = new EmailNotifier("v.o.shuba@student.shpp"); // 1
        notifier = new FacebookNotifier(new SMSNotifier(notifier,"0970003032"),"vova"); // 2
        notifier = new DiscordNotifier(notifier,"discord");

        notifier.send("so what we gonna do?");

    }
}
