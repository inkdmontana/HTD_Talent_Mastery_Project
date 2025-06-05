package htd.mastery.domain;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {
    private T payload;
    private final List<String> messages = new ArrayList<>();

    public boolean isSuccess() {
        return messages.isEmpty();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
