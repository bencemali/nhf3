package network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a network connection
 */
public class Connection {
    /**
     * The port number of the application's server socket
     */
    private static final Integer serverPort = 53333;

    /**
     * The contact the connection belongs to
     */
    private Contact contact;

    /**
     * The network socket
     */
    private Socket socket;

    /**
     * The connection's state
     */
    private Boolean open;

    /**
     * The Writer acting as the output
     */
    private PrintWriter output;

    /**
     * The Reader acting as the input
     */
    private BufferedReader input;

    /**
     *  The thread listening for input, and saving it to the contact's message list
     */
    private InputHandlerThread inputHandler;

    /**
     *
     */
    private ConnectionListener listener;

    /**
     * Constructor
     *
     * @param contact the contact the connection belongs to
     */
    public Connection(Contact contact) {
        this.contact = contact;
        this.open = false;
        try {
            if(InetAddress.getByName(contact.getIp()).isReachable(800)) {
                socket = new Socket(contact.getIp(), serverPort);
                open = true;
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                inputHandler = new InputHandlerThread(input, contact);
                inputHandler.start();
            } else {
                socket = null;
                output = null;
                input = null;
            }
        } catch(Exception e) {}
    }

    /**
     * Constructor
     *
     * @param contact the contact the connection belongs to
     * @param socket previously opened socket from the server thread
     */
    public Connection(Contact contact, Socket socket) {
        this.contact = contact;
        if(socket != null) {
            this.socket = socket;
            open = true;
            try {
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch(Exception e) {}
        }
        if(contact != null) {
            inputHandler = new InputHandlerThread(input, contact);
            inputHandler.start();
        }
    }

    /**
     * Method to set the contact to which the connection belongs to
     *
     * @param contact the new contact to belong to
     */
    synchronized public void setContact(Contact contact) {
        if(inputHandler != null) {
            inputHandler.halt();
        }
        this.contact = contact;
        if(this.contact != null) {
            inputHandler = new InputHandlerThread(input, this.contact);
            inputHandler.start();
        }
    }

    /**
     * Return whether the connection is open
     *
     * @return the connection is open
     */
    synchronized public boolean isOpen() {
        if(socket == null) return false;
        return open && socket.isConnected();
    }

    /**
     * Sends a message through the network socket
     *
     * @param message message to be sent
     * @return whether it could be sent
     */
    synchronized public boolean send(String message) {
        if(isOpen()) {
            output.println(message);
            output.flush();
            return true;
        }
        return false;
    }

    /**
     * Disposes of the connection and input handler thread
     */
    synchronized public void dispose() {
        open = false;
        if(listener != null) {
            listener.connectionChanged(false);
        }
        if(socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
        if(inputHandler != null) {
            if(inputHandler.isRunning()) {
                inputHandler.halt();
            }
        }
    }

    /**
     * Sets the listener
     *
     * @param listener listener to be set to
     */
    synchronized public void setListener(ConnectionListener listener) {
        this.listener = listener;
    }

    /**
     * Thread to wait for input and save it to the contact's message list
     */
    private class InputHandlerThread extends Thread {
        private final BufferedReader input;
        private final Contact contact;
        private final AtomicBoolean running = new AtomicBoolean(false);

        /**
         * Constructor
         *
         * @param br input Reader
         * @param contact to which the connection and input handler belong to
         */
        public InputHandlerThread(BufferedReader br, Contact contact) {
            input = br;
            this.contact = contact;
            setDaemon(true);
        }

        /**
         * The thread's "main" method
         */
        public void run() {
            running.set(true);
            String message = null;
            if(input == null) return;
            while(running.get()) {
                try {
                    synchronized(input) {
                        message = input.readLine();
                    }
                    if(message == null) {
                        running.set(false);
                    }
                } catch(IOException e) {}
                if(message != null) {
                    synchronized(contact) {
                        contact.receiveMessage(message);
                    }
                }
            }
            Connection.this.open = false;
            if(Connection.this.listener != null) {
                Connection.this.listener.connectionChanged(false);
            }
        }

        /**
         * Stops the thread's execution
         */
        synchronized public void halt() { running.set(false); }

        /**
         * Returns whether the thread is running
         *
         * @return the thread's running state
         */
        synchronized public boolean isRunning() { return running.get(); }
    }
}
