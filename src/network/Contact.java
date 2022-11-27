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

    synchronized public void connect() {
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

    synchronized public boolean isConnected() {
        if(connection == null) {
            return false;
        } else {
            return connection.isOpen();
        }
    }

    synchronized public void setConnection(Connection connection) {
        this.connection = connection;
        connection.setListener((up) -> {
            if(connectionListener != null) {
                connectionListener.connectionChanged(up);
            }
        });
    }

    synchronized public void setName(String name) {
        this.name = name;
    }

    synchronized public void dispose() {
        if(connection != null) {
            connection.dispose();
        }
    }

    synchronized public String getName() { return name; }

    synchronized public String getIp() { return ipAddress; }

    synchronized public List<Message> getMessages() { return messages; }

    synchronized public void addMessage(Message message) {
        messages.add(message);
    }

    synchronized public void receiveMessage(String message) {
        messages.add(new Message(message, false));
        if(messageListener != null) {
            messageListener.newMessage();
        }
    }

    synchronized public void sendMessage(String message) {
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

    synchronized public void setMessageListener(MessageListener listener) {
        messageListener = listener;
    }

    synchronized public void setConnectionListener(ConnectionListener listener) {
        connectionListener = listener;
        if(connection != null) {
            connection.setListener((up) -> {
                if (connectionListener != null) {
                    connectionListener.connectionChanged(up);
                }
            });
        }
    }
}
