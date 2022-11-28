package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a contact (meaning a chat with another user)
 */
public class Contact implements Serializable {
    /**
     * The other user's name
     */
    private String name;

    /**
     * The ip address of the other user
     */
    private final String ipAddress;

    /**
     * The list of messages sent and received
     */
    private final List<Message> messages;

    /**
     * The network connection with the other user
     */
    transient private Connection connection;

    /**
     * Listens for the arrival of a new message
     */
    transient private MessageListener messageListener;

    /**
     * Listens for change in the connection status
     */
    transient private ConnectionListener connectionListener;

    /**
     * Constructor
     *
     * @param name the other user's name
     * @param ipAddress the other user's ip address
     */
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

    /**
     * Constructor
     *
     * @param name the other user's name
     * @param ipAddress the other user's ip address
     * @param connection the network connection
     */
    public Contact(String name, String ipAddress, Connection connection) {
        this.name = name;
        this.ipAddress = ipAddress;
        messages = new ArrayList<>();
        this.connection = connection;
        if(connection != null) {
            connection.setListener((up) -> {
                if (connectionListener != null) {
                    connectionListener.connectionChanged(up);
                }
            });
        }
        messageListener = null;
        connectionListener = null;
    }

    /**
     * Tries to open a network connection if there's no connection or if the connection is closed
     */
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
                if(connectionListener != null) {
                    connectionListener.connectionChanged(up);
                }
            });
        }
    }

    /**
     * Returns the connection state
     *
     * @return connection state
     */
    synchronized public boolean isConnected() {
        if(connection == null) {
            return false;
        } else {
            return connection.isOpen();
        }
    }

    /**
     * Sets the connection
     *
     * @param connection the connection to set to
     */
    synchronized public void setConnection(Connection connection) {
        this.connection = connection;
        connection.setListener((up) -> {
            if(connectionListener != null) {
                connectionListener.connectionChanged(up);
            }
        });
    }

    /**
     * Sets the name
     *
     * @param name the name to set to
     */
    synchronized public void setName(String name) {
        this.name = name;
    }

    /**
     * Disposes of the contact - meaning disposing of the connection
     */
    synchronized public void dispose() {
        if(connection != null) {
            connection.dispose();
        }
    }

    /**
     * Returns the name
     *
     * @return the name
     */
    synchronized public String getName() { return name; }

    /**
     * Returns the ip address
     *
     * @return the ip address
     */
    synchronized public String getIp() { return ipAddress; }

    /**
     * Returns the list of messages
     *
     * @return the list of messages
     */
    synchronized public List<Message> getMessages() { return messages; }

    /**
     * Adds a message to the message list
     *
     * @param message the message to be added to the list
     */
    synchronized public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Adds message to the list and notifies listener of it's arrival
     *
     * @param message the message to be saved
     */
    synchronized public void receiveMessage(String message) {
        messages.add(new Message(message, false));
        if(messageListener != null) {
            messageListener.newMessage();
        }
    }

    /**
     * Sends a message through the network connection
     *
     * @param message the message to be sent
     */
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

    /**
     * Sets the message listener
     *
     * @param listener the listener to set
     */
    synchronized public void setMessageListener(MessageListener listener) {
        messageListener = listener;
    }

    /**
     * Sets the connectionListener
     *
     * @param listener the listener to set to
     */
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
