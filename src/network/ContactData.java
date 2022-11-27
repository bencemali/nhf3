package network;

import javax.print.Doc;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Model class holding the contact list and enabling table view
 */
public class ContactData extends AbstractTableModel {
    /**
     * The list of contacts
     */
    private final List<Contact> contacts;

    /**
     * The listener listening for a contact's disposal
     */
    private DisposeListener disposeListener;

    /**
     * Constructor
     */
    public ContactData() {
        contacts = new ArrayList<>();
    }

    /**
     * Returns the number of rows
     *
     * @return number of rows
     */
    @Override
    synchronized public int getRowCount() {
        return contacts.size(); }

    /**
     * Returns the number of columns
     *
     * @return number of columns
     */
    @Override
    synchronized public int getColumnCount() { return 1; }

    /**
     * Returns the contact's name at a certain index
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the contact's name
     */
    @Override
    synchronized public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= contacts.size() || columnIndex != 0) {
            return null;
        } else {
            return contacts.get(rowIndex).getName();
        }
    }

    /**
     *  Returns if a cell is editable
     *
     * @param rowIndex  the row being queried
     * @param columnIndex the column being queried
     * @return no cell is editable (always false)
     */
    @Override
    synchronized public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Returns the only columns class - String.class
     *
     * @param columnIndex  the column being queried
     * @return Always String.class
     */
    @Override
    synchronized public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * Adds a contact to the list
     *
     * @param name the new contact's name
     * @param ip the new contact's ip address
     */
    synchronized public void addContact(String name, String ip) {
        Iterator<Contact> it = contacts.iterator();
        while(it.hasNext()) {
            Contact c = it.next();
            if(c.getName().equals(name)) return;
            if(c.getIp().equals(ip)) return;
        }
        contacts.add(new Contact(name, ip));
        fireTableDataChanged();
    }

    /**
     * Adds an existing contact to the list
     *
     * @param contact the existing contact
     */
    synchronized public void addContact(Contact contact) {
        contacts.add(contact);
        fireTableDataChanged();
    }

    /**
     * Deletes a contact from the list
     *
     * @param contact the contact to delete
     */
    synchronized public void deleteContact(Contact contact) {
        for(int i = 0; i < contacts.size(); ++i) {
            if(contacts.get(i) == contact) {
                contacts.get(i).dispose();
                contacts.remove(i);
                fireTableDataChanged();
                if(disposeListener != null) {
                    disposeListener.disposed(i);
                }
                break;
            }
        }
    }

    /**
     * Renames a contact in the list
     *
     * @param contact the contact to rename
     * @param name the new name of the contact
     */
    synchronized public void renameContact(Contact contact, String name) {
        Iterator<Contact> it = contacts.iterator();
        while(it.hasNext()) {
            Contact c = it.next();
            if(c == contact) {
                c.setName(name);
                fireTableDataChanged();
                break;
            }
        }
    }

    /**
     * Gives the contact at a given index
     *
     * @param rowIndex the index
     * @return the contact at the given index
     */
    synchronized public Contact getContact(int rowIndex) {
        if(rowIndex >= contacts.size() || contacts.size() == 0 || rowIndex < 0) return null;
        return contacts.get(rowIndex);
    }

    /**
     * Gives the contact for the given ip address
     *
     * @param ipAddress the ip address to search against
     * @return the contact having the given ip address
     */
    synchronized public Contact getContact(String ipAddress) {
        for(Contact contact : contacts) {
            if(contact.getIp().equals(ipAddress)) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Returns if there's a contact having the given ip address
     *
     * @param ipAddress the ip address to search against
     * @return if there's a contact having the given ip address
     */
    synchronized public boolean haveContact(String ipAddress) {
        for(Contact contact : contacts) {
            if(contact.getIp().equals(ipAddress)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets disposeListener to the provided one
     *
     * @param listener the listener to set to
     */
    synchronized public void setDisposeListener(DisposeListener listener) {
        disposeListener = listener;
    }

    /**
     * Saves the list of contacts to a file by serialization
     *
     * @param file the file to save to
     */
    synchronized public void saveOut(File file) {
        if(file != null) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(contacts);
                oos.close();
            } catch(Exception e) {}
        }
    }

    /**
     * Loads the list of contacts saved to a file
     *
     * @param file the file to load from
     */
    synchronized public void loadIn(File file) {
        if(file != null) {
            List<Contact> list = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                list = (List<Contact>)ois.readObject();
                ois.close();
            } catch (Exception e) {}
            if(list != null) {
                for(Contact newContact : list) {
                    boolean found = false;
                    for(Contact oldContact : contacts) {
                        if(newContact.getIp().equals(oldContact.getIp())) {
                            found = true;
                            break;
                        } else if(newContact.getName().equals(oldContact.getName())) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        contacts.add(newContact);
                    }
                }
            }
        }
    }

    /**
     * Saves the list of contacts to an xml
     *
     * @param file the xml file to save to
     */
    synchronized public void saveToXml(File file) {
        if (file != null) {
            synchronized(contacts) {
                Element contactsElement = new Element("contacts");
                Document document = new Document(contactsElement);
                for (Contact contact : contacts) {
                    Element contactElement = new Element("contact");
                    contactElement.setAttribute(new Attribute("name", contact.getName()));
                    contactElement.setAttribute(new Attribute("ip", contact.getIp()));
                    for (Message message : contact.getMessages()) {
                        Element messageElement = new Element("message");
                        messageElement.setAttribute(new Attribute("owned", message.getOwned().toString()));
                        messageElement.setText(message.getString());
                        contactElement.addContent(messageElement);
                    }
                    document.getRootElement().addContent(contactElement);
                }
                XMLOutputter xmlOutputter = new XMLOutputter();
                xmlOutputter.setFormat(Format.getPrettyFormat());
                try {
                    xmlOutputter.output(document, new FileOutputStream(file));
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Loads the list of contacts saved to an xml file
     *
     * @param file the file to load from
     */
    synchronized public void loadFromXml(File file) {
        if(file != null) {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = null;
            try {
                document = saxBuilder.build(file);
                Element contactsElement = document.getRootElement();
                List<Element> contactList = contactsElement.getChildren();
                for(Element contactElement : contactList) {
                    String name = contactElement.getAttributeValue("name");
                    String ip = contactElement.getAttributeValue("ip");
                    Contact contact = new Contact(name, ip);
                    List<Element> messageList = contactElement.getChildren();
                    for(Element messageElement : messageList) {
                        contact.addMessage(new Message(messageElement.getText(), Boolean.parseBoolean(messageElement.getAttributeValue("owned"))));
                    }
                    contacts.add(contact);
                }
            } catch(Exception e) {}
            fireTableDataChanged();
        }
    }

    /**
     * Disposes of all contacts and their connections
     */
    synchronized public void dispose() {
        for(Contact contact : contacts) {
            contact.dispose();
        }
    }
}
