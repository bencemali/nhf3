package gui;

import network.ContactData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * TODO javadoc
 */
public class MainFrame extends JFrame {
    private ContactData contactData;
    private JMenuBar menuBar;
    private ContactPane contactPane;
    private ChatPane chatPane;

    public MainFrame(ContactData data) {
        super("Chat application");
        contactData = data;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(1000, 600));

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        //TODO initialize:
        // menubar
        // contact list + double click event handler
        // chat pane
        initMenuBar();
        JTable table = new JTable(contactData);
        contactPane = new ContactPane(contactData, table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = contactPane.getIndexForClick(e);
                if(idx != -1) {
                    chatPane.displayChat(idx);
                }
            }
        });
        add(contactPane, BorderLayout.WEST);
        chatPane = new ChatPane(contactData);
        add(chatPane, BorderLayout.CENTER);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        //File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exportXml = new JMenuItem("Export to XML");
        exportXml.addActionListener(e -> {
            //TODO open dialog panel
        });
        JMenuItem importXml = new JMenuItem("Import from XML");
        importXml.addActionListener(e -> {
            //TODO open dialog panel
        });
        JMenuItem saveOut = new JMenuItem("Save to .dat");
        saveOut.addActionListener(e -> {
            //TODO open dialog panel
        });
        JMenuItem loadIn = new JMenuItem("Load from .dat");
        loadIn.addActionListener(e -> {
            //TODO open dialog panel
        });
        fileMenu.add(exportXml);
        fileMenu.add(importXml);
        fileMenu.add(saveOut);
        fileMenu.add(loadIn);
        menuBar.add(fileMenu);

        //Contacts menu
        JMenu contactMenu = new JMenu("Contacts");
        JMenuItem add = new JMenuItem("Add");
        add.addActionListener(e -> {
            //TODO open dialog panel
        });
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(e -> {
            //TODO open dialog panel
        });
        contactMenu.add(add);
        contactMenu.add(delete);
        menuBar.add(contactMenu);
        //TODO more menus
        setJMenuBar(menuBar);
    }
}
