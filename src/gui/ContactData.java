package gui;

import network.*;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO javadoc
 */
public class ContactData extends AbstractTableModel {
    final List<Contact> contacts;

    public ContactData(List<Contact> c) {
        contacts = c;
        //TODO ctor
    }

    @Override
    public int getRowCount() { return contacts.size(); }

    @Override
    public int getColumnCount() { return 1; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= contacts.size() || columnIndex != 0) {
            return null;
        } else {
            return contacts.get(rowIndex);
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

    public void addContact() {
        //TODO what parameters
    }
}
