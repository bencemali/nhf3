package test;

import network.Contact;
import network.ContactData;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ContactDataTest {
    ContactData contactData;
    Contact contact1;
    Contact contact2;
    Contact contact3;

    @Before
    public void setUp() {
        contactData = new ContactData();
        contact1 = new Contact("name1", "ip1");
        contact2 = new Contact("name2", "ip2");
        contact3 = new Contact("name3", "ip3");
    }

    @Test
    public void getSetTest() {
        assertEquals(0, contactData.getRowCount());
        assertEquals(1, contactData.getColumnCount());

        Contact contact = new Contact("name1", "ip1");
        contactData.addContact(contact);
        assertEquals(1, contactData.getRowCount());
        assertEquals("name1", contactData.getValueAt(0, 0));

        contactData.addContact("name2", "ip1");
        contactData.addContact("name1", "ip2");
        assertEquals(1, contactData.getRowCount());

        contactData.addContact(new Contact("name2", "ip1"));
        contactData.addContact(new Contact("name1", "ip2"));
        assertEquals(1, contactData.getRowCount());

        assertEquals(String.class, contactData.getColumnClass(0));

        contactData.deleteContact(null);
        assertEquals(1, contactData.getRowCount());

        contactData.deleteContact(contact);
        assertEquals(0, contactData.getRowCount());
    }

    @Test
    public void serializationTest() {
        contactData.addContact(contact1);
        contactData.addContact(contact2);
        contactData.addContact(contact3);

        File testFile = new File("test.dat");

        contactData.saveOut(testFile);

        contactData.deleteContact(contact1);
        contactData.deleteContact(contact2);
        contactData.deleteContact(contact3);

        contactData.loadIn(testFile);
        assertEquals(3, contactData.getRowCount());
        for(int i = 1; i < 4; ++i) {
            assertEquals("name" + i, contactData.getContact(i - 1).getName());
            assertEquals("ip" + i, contactData.getContact(i - 1).getIp());
        }

        testFile.delete();
    }

    @Test
    public void xmlTest() {
        contactData.addContact(contact1);
        contactData.addContact(contact2);
        contactData.addContact(contact3);

        File testFile = new File("test.xml");

        contactData.saveToXml(testFile);

        contactData.deleteContact(contact1);
        contactData.deleteContact(contact2);
        contactData.deleteContact(contact3);

        contactData.loadFromXml(testFile);
        assertEquals(3, contactData.getRowCount());
        for(int i = 1; i < 4; ++i) {
            assertEquals("name" + i, contactData.getContact(i - 1).getName());
            assertEquals("ip" + i, contactData.getContact(i - 1).getIp());
        }

        testFile.delete();
    }
}
