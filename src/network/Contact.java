package network;

import java.io.Serializable;
import java.sql.Struct;
import java.util.List;

public class Contact implements Serializable {
    private String name;
    private String ipAddress;
    private List<Message> messages;
    transient private Connection connection;

    public Contact(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
        connection = new Connection(this);
    }

    class Message {
        String message;
        Boolean owned;

        public Message(String message, Boolean owned) {
            this.message = message;
            this.owned = owned;
        }
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ipAddress;
    }

    public void receiveMessage(String message) {
        messages.add(new Message(message, false));
    }

    public void sendMessage(String message) {
        //TODO push message as incoming
        //      + send message through Connection
    }
}
