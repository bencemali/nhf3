package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The thread waiting for the sockets of remote hosts to connect to this host
 */
public class Server extends Thread {
    /**
     * The constant port number the server waits on for connections
     */
    private final int portNumber = 53333;

    /**
     * The list of contacts
     */
    private ContactData contactData;

    /**
     * The server socket waiting for connections
     */
    private ServerSocket serverSocket;

    /**
     * The running state of the thread
     */
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Constructor
     *
     * @param contactData the list of contacts
     */
    public Server(ContactData contactData) {
        this.contactData = contactData;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch(IOException e) {}
        setDaemon(true);
    }

    /**
     * The "main" method of the thread
     */
    @Override
    public void run() {
        running.set(true);
        Socket socket = null;
        if(serverSocket != null) {
            while(running.get()) {
                try {
                    socket = serverSocket.accept();
                } catch(IOException e) {}
                if (socket != null) {
                    if (contactData != null) {
                        synchronized(contactData) {
                            if (!contactData.haveContact(socket.getInetAddress().toString())) {
                                Connection connection = new Connection(null, socket);
                                Contact contact = new Contact("New connection", socket.getInetAddress().toString(), connection);
                                contactData.addContact(contact);
                                connection.setContact(contact);
                            } else {
                                Contact contact = contactData.getContact(socket.getInetAddress().toString());
                                contact.setConnection(new Connection(contact, socket));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Stops the thead
     */
    public void dispose() {
        running.set(false);
        try {
            serverSocket.close();
        } catch(IOException e) {}
    }
}
