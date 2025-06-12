package CreateJson;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String username;

    public Message(String message) {
        setMessage(message);
    }

    public Map<String, String> sayHello() {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", "Привіт " + username + "!");
        return messageMap;
    }

    public void setMessage(String message) {
        this.username = message;
    }
}