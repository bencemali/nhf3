package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server extends Thread {
    private final int portNumber = 53333;
    private ContactData contactData;
    private List<Socket> sockets;
    private ServerSocket serverSocket;

    public Server(ContactData contactData) {
        this.contactData = contactData;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch(IOException e) {}
        setDaemon(true);
    }

    @Override
    public void run() {
        //TODO wait for connections and add to ContactData
        Socket socket = null;
        while(true) {
            try {
                socket = serverSocket.accept();
            } catch(IOException e) {}
            if(socket != null) {
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

    public void closeAllSockets() {
        if(sockets != null) {
            for (Socket socket : sockets) {
                try {
                    if(socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }
}
