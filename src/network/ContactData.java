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
 * TODO javadoc
 */
public class ContactData extends AbstractTableModel {
    private final List<Contact> contacts;
    private DisposeListener disposeListener;

    public ContactData() {
        contacts = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return contacts.size(); }

    @Override
    public int getColumnCount() { return 1; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= contacts.size() || columnIndex != 0) {
            return null;
        } else {
            return contacts.get(rowIndex).getName();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public void addContact(String name, String ip) {
        Iterator<Contact> it = contacts.iterator();
        while(it.hasNext()) {
            Contact c = it.next();
            if(c.getName().equals(name)) return;
            if(c.getIp().equals(ip)) return;
        }
        contacts.add(new Contact(name, ip));
        fireTableDataChanged();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        fireTableDataChanged();
    }

    public void deleteContact(Contact contact) {
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

    public void renameContact(Contact contact, String name) {
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

    public Contact getContact(int rowIndex) {
        if(rowIndex >= contacts.size() || contacts.size() == 0 || rowIndex < 0) return null;
        return contacts.get(rowIndex);
    }

    public Contact getContact(String ipAddress) {
        for(Contact contact : contacts) {
            if(contact.getIp().equals(ipAddress)) {
                return contact;
            }
        }
        return null;
    }

    public boolean haveContact(String ipAddress) {
        for(Contact contact : contacts) {
            if(contact.getIp().equals(ipAddress)) {
                return true;
            }
        }
        return false;
    }

    public void setDisposeListener(DisposeListener listener) {
        disposeListener = listener;
    }

    public void saveOut(File file) {
        if(file != null) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(contacts);
                oos.close();
            } catch(Exception e) {}
        }
    }

    public void loadIn(File file) {
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

    public void saveToXml(File file) {
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
                        messageElement.setAttribute(new Attribute("owned", message.owned.toString()));
                        messageElement.setText(message.string);
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

    public void loadFromXml(File file) {
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
}
