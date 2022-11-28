import com.formdev.flatlaf.FlatLightLaf;
import gui.*;
import network.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * The starting point of the program
 */
public class Main {
    /**
     * The list of contacts
     */
    private static ContactData contactData;

    /**
     * The server thread waiting for connections
     */
    private static Server server;

    /**
     * The frame displaying all content
     */
    private static MainFrame frame;

    /**
     * The program's main method and execution entry point
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();
        contactData = new ContactData();
        contactData.loadIn(new File("contacts.dat"));
        server = new Server(contactData);
        server.start();
        frame = new MainFrame(contactData);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                contactData.dispose();
                server.dispose();
                contactData.saveOut(new File("contacts.dat"));
                System.exit(0);
            }
        });
    }
}
