package test;

import network.Contact;
import network.ContactData;
import network.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Server class
 */
public class ServerTest {
    ContactData contactData;
    Server server;
    Contact contact;

    @Before
    public void setUp() {
        contactData = new ContactData();
        server = new Server(contactData);
        server.start();
    }

    @After
    public void finish() {
        contactData.dispose();
        server.dispose();
        contact.dispose();
    }

    @Test
    public void newConnectionTest() {
        contact = new Contact("name1", "127.0.0.1");
        try {
            Thread.sleep(1000);
        } catch(Exception e) {}
        assertEquals(1, contactData.getRowCount());
    }
}
