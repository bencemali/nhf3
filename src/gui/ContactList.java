package gui;

import javax.swing.*;

/**
 * TODO javadoc
 */
public class ContactList extends JPanel {
    private JTable table;

    public ContactList(ContactData data) {
        table = new JTable(data); //TODO ContactData ctor could be changed later
    }
}
