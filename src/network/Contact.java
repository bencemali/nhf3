package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable {
    private final String name;
    private final String ipAddress;
    private final List<Message> messages;
    transient private Connection connection;
    transient private MessageListener listener; //???? 1 listener

    public Contact(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
        messages = new ArrayList<>();
        connection = new Connection(this);
        listener = null;
    }

    public Contact(String name, String ipAddress, Connection connection) {
        this.name = name;
        this.ipAddress = ipAddress;
        messages = new ArrayList<>();
        this.connection = connection;
        listener = null;
    }

    public String getName() { return name; }

    public String getIp() { return ipAddress; }

    public List<Message> getMessages() { return messages; }

    public void receiveMessage(String message) {
        messages.add(new Message(message, false));
        if(listener != null) {
            listener.newMessage();
        }
    }

    public void sendMessage(String message) {
        messages.add(new Message(message, true));
        connection.send(message);
    }

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }
}
