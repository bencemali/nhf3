package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable {
    private String name;
    private final String ipAddress;
    private final List<Message> messages;
    transient private Connection connection;
    transient private MessageListener messageListener;
    transient private ConnectionListener connectionListener;

    public Contact(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
        messages = new ArrayList<>();
        connection = new Connection(this);
        connection.setListener((up) -> {
            if(connectionListener != null) {
                connectionListener.connectionChanged(up);
            }
        });
        messageListener = null;
        connectionListener = null;
    }

    public Contact(String name, String ipAddress, Connection connection) {
        this.name = name;
        this.ipAddress = ipAddress;
        messages = new ArrayList<>();
        this.connection = connection;
        connection.setListener((up) -> {
            if(connectionListener != null) {
                connectionListener.connectionChanged(up);
            }
        });
        messageListener = null;
        connectionListener = null;
    }

    public void connect() {
        System.out.println("Contact::connect");
        if(connection == null) {
            connection = new Connection(this);
            connection.setListener((up) -> {
                connectionListener.connectionChanged(up);
            });
        } else if(!connection.isOpen()) {
            connection.dispose();
            connection = new Connection(this);
            connection.setListener((up) -> {
                connectionListener.connectionChanged(up);
            });
        }
    }

    public boolean isConnected() {
        System.out.println("Contact::isConnected");
        if(connection == null) {
            return false;
        } else {
            return connection.isOpen();
        }
    }

    public void setConnection(Connection connection) {
        System.out.println("Contact::setConnection");
        this.connection = connection;
        connection.setListener((up) -> {
            if(connectionListener != null) {
                connectionListener.connectionChanged(up);
            }
        });
    }

    public void setName(String name) {
        this.name = name;
    }

    public void dispose() {
        if(connection != null) {
            connection.dispose();
        }
    }

    public String getName() { return name; }

    public String getIp() { return ipAddress; }

    public List<Message> getMessages() { return messages; }

    public void receiveMessage(String message) {
        System.out.println("Contact::receiveMessage");
        messages.add(new Message(message, false));
        if(messageListener != null) {
            messageListener.newMessage();
        }
    }

    public void sendMessage(String message) {
        System.out.println("Contact::sendMessage");
        if(connection.send(message)) {
            messages.add(new Message(message, true));
            if(messageListener != null) {
                messageListener.newMessage();
            }
        } else {
            if(connectionListener != null) {
                connectionListener.connectionChanged(false);
            }
        }
    }

    public void setMessageListener(MessageListener listener) {
        System.out.println("Contact::setMessageListener");
        messageListener = listener;
    }

    public void setConnectionListener(ConnectionListener listener) {
        System.out.println("Contact::setConnectionListener");
        connectionListener = listener;
        connection.setListener((up) -> {
            if(connectionListener != null) {
                connectionListener.connectionChanged(up);
            }
        });
    }
}
