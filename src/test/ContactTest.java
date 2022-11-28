package test;

import com.formdev.flatlaf.IntelliJTheme;
import network.*;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class ContactTest {
    private static Contact contact;
    private static Contact valid;
    private static ContactData contactData;
    private static Server server;

    @BeforeClass
    public static void setUp() {
        contactData = new ContactData();
        server = new Server(contactData);
        server.start();
        valid = new Contact("loopback", "127.0.0.1");
        contact = new Contact("name", "ip", null);
    }

    @AfterClass
    public static void finish() {
        server.dispose();
        contactData.dispose();
        contact.dispose();
        valid.dispose();
    }

    @Test
    public void getSetTest() {
        assertEquals("name", contact.getName());
        assertEquals("ip", contact.getIp());
        contact.addMessage(new Message("message1", true));
        contact.addMessage(new Message("message2", true));
        List<Message> messages = contact.getMessages();
        assertEquals("message1", messages.get(0).getString());
        assertEquals("message2", messages.get(1).getString());
    }

    @Test
    public void connectTest() {
        assertFalse(contact.isConnected());
        Connection connection = new Connection(contact);
        contact.setConnection(connection);
        assertFalse(contact.isConnected());
        assertTrue(valid.isConnected());
    }

    @Test
    public void messageTest() {
        Contact valid2 = contactData.getContact(0);
        valid2.sendMessage("hello");
        try {
            Thread.sleep(100);
        } catch(Exception e) {}
        assertEquals("hello", valid.getMessages().get(0).getString());
        assertFalse(valid.getMessages().get(0).getOwned());
        valid.sendMessage("olleh");
        assertEquals("olleh",valid.getMessages().get(1).getString());
        assertTrue(valid.getMessages().get(1).getOwned());
    }
}
