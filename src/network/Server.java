package network;

import java.net.Socket;
import java.util.List;

public class Server extends Thread {
    private ContactData contactData;
    private List<Socket> sockets;

    public Server(ContactData contactData) {
        this.contactData = contactData;
        setDaemon(true);
    }

    @Override
    public void run() {
        //TODO wait for connections and add to ContactData
    }

    public void closeAllSockets() {

    }
}
