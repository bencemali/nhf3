import com.formdev.flatlaf.FlatLightLaf;
import gui.*;
import network.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Main {
    private static ContactData contactData;
    private static Server server;
    private static MainFrame frame;

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
                server.closeAllSockets();
                contactData.saveOut(new File("contacts.dat"));
                System.exit(0);
            }
        });
    }
}
