package network;

import javax.swing.table.AbstractTableModel;
import java.util.Iterator;
import java.util.List;

/**
 * TODO javadoc
 */
public class ContactData extends AbstractTableModel {
    public final List<Contact> contacts;
    private DisposeListener disposeListener;

    public ContactData(List<Contact> c) {
        contacts = c;
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
}
