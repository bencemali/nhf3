package network;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection {
    private final Integer serverPort = 53333;
    private Contact contact;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private InputHandler inputHandler;

    public Connection(Contact contact) {
        this.contact = contact;
        try {
            socket = new Socket(contact.getIp(), serverPort);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            //TODO signal if unknown host
        }
        InputHandler inputHandler = new InputHandler(input, contact);
    }

    public Connection(Contact contact, Socket socket) {
        this.contact = contact;
        if(socket != null) {
            this.socket = socket;
            try {
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch(Exception e) {
                //TODO signal if unknown host
            }
        }
        if(contact != null) {
            inputHandler = new InputHandler(input, contact);
            inputHandler.start();
        }
    }

    public void setContact(Contact contact) {
        if(inputHandler != null) {
            inputHandler.halt();
        }
        this.contact = contact;
        if(this.contact != null) {
            InputHandler inputHandler = new InputHandler(input, this.contact);
            inputHandler.start();
        }
    }

    private class InputHandler extends Thread {
        private BufferedReader input;
        private Contact contact;
        private final AtomicBoolean running = new AtomicBoolean(false);

        public InputHandler(BufferedReader br, Contact contact) {
            input = br;
            this.contact = contact;
            setDaemon(true);
        }

        public void run() {
            running.set(true);
            String message = null;
            while(running.get()) {
                try {
                    message = input.readLine();
                } catch(IOException e) {}
                if(message != null) {
                    contact.receiveMessage(message);
                }
            }
        }

        public void halt() {
            running.set(false);
        }

        public boolean isRunning() {
            return running.get();
        }
    }

    public boolean isOpen() {
        if(socket.isConnected() && !socket.isClosed()) {
            return true;
        } else {
            return false;
        }
    }

    public void reconnect() {
        try {
            socket = new Socket(contact.getIp(), serverPort);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(Exception e) {}
        if(inputHandler != null) {
            inputHandler.halt();
        }
        InputHandler inputHandler = new InputHandler(input, contact);
        inputHandler.start();
    }

    public void send(String message) {
        if(!isOpen()) {
            reconnect();
        }
        if(output != null) {
            output.println(message);
            output.flush();
        }
    }
}
