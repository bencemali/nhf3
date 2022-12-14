package gui;

import network.ContactData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * The panel displaying the list of contacts
 */
public class ContactPane extends JPanel {
    /**
     * The table model storing the contacts
     */
    private final ContactData contactData;

    /**
     * The table formed from the contactData table model
     */
    private final JTable table;

    /**
     * Constructor
     *
     * @param data the table model storing the contacts
     * @param contactTable the table formed from the model
     */
    public ContactPane(ContactData data, JTable contactTable) {
        contactData = data;

        setPreferredSize(new Dimension(180, 600));
        setLayout(new BorderLayout());
        table = contactTable;
        table.setTableHeader(null);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel("Name");
        JTextField nameField = new JTextField(20);
        JLabel ipLabel = new JLabel("Ip");
        JTextField ipField = new JTextField(20);
        JPanel addPanel = new JPanel(new GridBagLayout());
        JButton addButton = new JButton("Add Connection");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!nameField.getText().equals("") && !ipField.getText().equals("")) {
                    contactData.addContact(nameField.getText().trim(), ipField.getText().trim());
                    nameField.setText("");
                    ipField.setText("");
                }
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        addPanel.add(nameLabel, gbc);
        gbc.weightx = 0.8;
        gbc.gridx = 1;
        addPanel.add(nameField, gbc);
        gbc.weightx = 0.2;
        gbc.gridy = 1;
        gbc.gridx = 0;
        addPanel.add(ipLabel, gbc);
        gbc.weightx = 0.2;
        gbc.gridx = 1;
        addPanel.add(ipField, gbc);
        gbc.weightx = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        addPanel.add(addButton, gbc);
        add(addPanel, BorderLayout.SOUTH);
    }

    /**
     * Method to get the index of the contact that was clicked on
     *
     * @param e the mouse click event
     * @return the index of the contact that was clicked on
     */
    synchronized public int getIndexForClick(MouseEvent e) {
        return table.rowAtPoint(e.getPoint());
    }
}
