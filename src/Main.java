import gui.*;
import network.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static ContactData contactData;
    private static Server server;
    private static MainFrame frame;

    public static void main(String[] args) {
        contactData = loadData();
        server = new Server(contactData);
        server.run();
        frame = new MainFrame(contactData);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.closeAllSockets();
                saveData(contactData);
            }
        });
    }

    static private ContactData loadData() {
        ContactData data;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("contacts.dat"));
            data = new ContactData((List<Contact>)ois.readObject());
            ois.close();
        } catch (Exception e) {
            data = new ContactData(new ArrayList<>());
        }
        return data;
    }

    static private void saveData(ContactData data) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("contacts.dat"));
            oos.writeObject(data.contacts);
            oos.close();
        } catch(IOException e) {}
        System.exit(0);
    }
}
