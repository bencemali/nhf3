package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * TODO javadoc
 */
public class MainFrame extends JFrame {
    private ContactData data;
    private JMenuBar menubar;
    private ContactList contactlist;
    private ChatPane chatpane;

    public MainFrame() {
        super("Chat application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}

        startProcedure();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });

        initComponents();
    }

    private void initComponents() {
        //TODO initialize:
        // menubar
        // contact list
        // chat pane
    }

    private void startProcedure() {
        //TODO load into
    }

    private void exitProcedure() {
        //TODO save off
    }
}
