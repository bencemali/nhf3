package gui;

import gui.theme.dark.DarkTheme;
import gui.theme.light.LightTheme;
import network.Contact;
import network.ContactData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * The main frame/window
 */
public class MainFrame extends JFrame {
    /**
     * The data object storing the contacts
     */
    private final ContactData contactData;

    /**
     * The panel listing the contacts
     */
    private ContactPane contactPane;

    /**
     * The chat panel showing the selected chat
     */
    private ChatPane chatPane;

    /**
     * Constructor
     * Sets parameters and look'n'feel
     *
     * @param data the data object storing the contacts
     */
    public MainFrame(ContactData data) {
        super("Chat application");
        contactData = data;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(1000, 600));
        try {
            UIManager.setLookAndFeel(new DarkTheme());
            UIManager.put("TitlePane.unifiedBackground", false);
        } catch(Exception e) {}
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes the components of the frame
     */
    private void initComponents() {
        initMenuBar();
        JTable table = new JTable(contactData);
        contactPane = new ContactPane(contactData, table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = contactPane.getIndexForClick(e);
                if(idx != -1) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        chatPane.displayChat(idx);
                    } else if(SwingUtilities.isRightMouseButton(e)) {
                        ContactPopup popup = new ContactPopup(contactData.getContact(idx));
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
        add(contactPane, BorderLayout.WEST);
        chatPane = new ChatPane(contactData);
        add(chatPane, BorderLayout.CENTER);
    }

    /**
     * Initializes the menu bar
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JButton theme = new JButton("Light");
        theme.setBackground(Color.decode("#8a8181"));
        theme.setForeground(Color.BLACK);
        theme.setBorderPainted(false);
        theme.addActionListener(e -> {
            try {
                if (UIManager.getLookAndFeel().getName().equals("DarkTheme")) {
                    UIManager.setLookAndFeel(new LightTheme());
                    theme.setText("Dark");
                    theme.setBackground(Color.decode("#1f1d1d"));
                    theme.setForeground(Color.WHITE);
                    SwingUtilities.updateComponentTreeUI(this);
                } else {
                    UIManager.setLookAndFeel(new DarkTheme());
                    theme.setText("Light");
                    theme.setBackground(Color.decode("#8a8181"));
                    theme.setForeground(Color.decode("#0e0d0d"));
                    SwingUtilities.updateComponentTreeUI(this);
                }
            } catch(Exception ex) {}
        });
        menuBar.add(theme);

        menuBar.add(Box.createHorizontalStrut(8));

        //File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exportXml = new JMenuItem("Export to XML");
        exportXml.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int returnVal = fileChooser.showDialog(this, "Export");
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                contactData.saveToXml(fileChooser.getSelectedFile());
            }
        });
        JMenuItem importXml = new JMenuItem("Import from XML");
        importXml.addActionListener(e -> {
            //TODO open dialog panel
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int returnVal = fileChooser.showDialog(this, "Import");
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                contactData.loadFromXml(fileChooser.getSelectedFile());
            }
        });
        JMenuItem saveOut = new JMenuItem("Save out");
        saveOut.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int returnVal = fileChooser.showDialog(this, "Save");
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                contactData.saveOut(fileChooser.getSelectedFile());
            }
        });
        JMenuItem loadIn = new JMenuItem("Load in");
        loadIn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int returnVal = fileChooser.showDialog(this, "Load");
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                contactData.loadIn(fileChooser.getSelectedFile());
            }
        });
        fileMenu.add(exportXml);
        fileMenu.add(importXml);
        fileMenu.add(saveOut);
        fileMenu.add(loadIn);
        menuBar.add(fileMenu);

        //TODO more menus
        setJMenuBar(menuBar);
    }

    /**
     * Popup for options when right-clicking on a contact
     */
    class ContactPopup extends JPopupMenu {
        /**
         * Constructor
         *
         * @param contact the contact that's been clicked on
         */
        public ContactPopup(Contact contact) {
            super("Rename");
            JMenuItem rename = new JMenuItem("Rename");
            rename.addActionListener(e -> {
                String name = JOptionPane.showInputDialog(MainFrame.this, "Enter new name:", "Rename Contact", JOptionPane.QUESTION_MESSAGE);
                if(name != null) {
                    contactData.renameContact(contact, name);
                }
            });
            JMenuItem delete = new JMenuItem("Delete");
            delete.addActionListener(e -> {
                contactData.deleteContact(contact);
            });
            JMenuItem information = new JMenuItem("Information");
            information.addActionListener(e -> {
                JOptionPane.showMessageDialog(MainFrame.this, "Name:  " + contact.getName() + "\nIp:  " + contact.getIp(), "Contact Information", JOptionPane.INFORMATION_MESSAGE);
            });
            add(rename);
            add(delete);
            add(information);
        }
    }
}
