package network;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Integer serverPort = 53333;
    private final Contact contact;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;

    public Connection(Contact contact) {
        this.contact = contact;
        try {
            socket = new Socket(contact.getIp(), serverPort);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(Exception e) {}
        InputHandler inputHandler = new InputHandler(input, contact);
    }

    private class InputHandler extends Thread {
        BufferedReader input;
        Contact contact;

        public InputHandler(BufferedReader br, Contact contact) {
            input = br;
            this.contact = contact;
            setDaemon(true);
        }

        public void run() {
            //TODO read input + write to contact
            String message = null;
            while(true) {
                try {
                    message = input.readLine();
                } catch(IOException e) {}
                if(message != null) {
                    contact.receiveMessage(message);
                }
            }
        }
    }

    public boolean isOpen() {
        if(socket.isConnected() && !socket.isClosed()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean reconnect() {
        try {
            socket = new Socket(contact.getIp(), serverPort);
            if(!socket.isConnected()) {
                return false;
            }
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(Exception e) {}
        InputHandler inputHandler = new InputHandler(input, contact);
        return true;
    }

    public void send(String message) {
        output.println(message);
        output.flush();
    }
}
