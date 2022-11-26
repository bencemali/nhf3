package gui;

import network.Contact;
import network.ContactData;
import network.Message;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

/**
 * The panel listing the previous messages of the selected chat, and holding a text field to send new messages
 */
public class ChatPane extends JPanel {
    /**
     * The data object storing the contacts
     */
    private final ContactData contactData;

    /**
     * The index of the currently focused/displayed contact
     */
    private int focused;

    /**
     * The scrollpane holding the messagePanel
     */
    private final JScrollPane messageScrollPane;

    /**
     * The panel holding the listed previous messages
     */
    private final JPanel messagePanel;
    /**
     * TextField to write new message
     */
    private final JTextField messageTextField;

    /**
     * Constructor
     *
     * @param contactData the data object storing the contacts
     */
    public ChatPane(ContactData contactData) {
        System.out.println("ChatPane(ContactData)");
        this.contactData = contactData;
        focused = -1;
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.LINE_AXIS));
        messageTextField = new JTextField();
        messageTextField.setEditable(false);
        messageTextField.addActionListener(e -> {
            if(!messageTextField.getText().equals("")) {
                this.contactData.getContact(focused).sendMessage(messageTextField.getText());
                messageTextField.setText("");
            }
        });
        sendPanel.add(messageTextField);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            if(!messageTextField.getText().equals("")) {
                this.contactData.getContact(focused).sendMessage(messageTextField.getText());
                messageTextField.setText("");
            }
        });
        sendPanel.add(sendButton);
        setLayout(new BorderLayout());
        messageScrollPane = new JScrollPane(messagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(messageScrollPane, BorderLayout.CENTER);
        add(sendPanel, BorderLayout.SOUTH);
    }

    /**
     * Focuses a chat, displaying the previous messages
     *
     * @param chatIndex the index of the chat to display
     */
    public void displayChat(int chatIndex) {
        System.out.println("displayChat()");
        if(focused != -1) {
            Contact c = this.contactData.getContact(focused);
            if(c != null) {
                c.setConnectionListener(null);
            }
        }
        focused = chatIndex;
        messagePanel.removeAll();
        messagePanel.validate();
        Contact focusedContact = contactData.getContact(focused);
        if(focusedContact != null) {
            messageTextField.setEditable(focusedContact.isConnected());
            List<Message> messages = focusedContact.getMessages();
            focusedContact.connect();
            Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
            for (Message message : messages) {
                JLabel messageLabel = new JLabel(message.message);
                if (message.owned) {
                    messageLabel.setOpaque(true);
                    messageLabel.setVisible(true);
                }
                messagePanel.add(messageLabel);
                messagePanel.add(Box.createVerticalStrut(4));
                messagePanel.validate();
            }
            messagePanel.repaint();
            messageScrollPane.validate();
            JScrollBar verticalBar = messageScrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
            messageScrollPane.repaint();
            focusedContact.setMessageListener(() -> {
                if (focused == chatIndex) {
                    displayChat(focused);
                }
            });
            focusedContact.setConnectionListener((up) -> {
                messageTextField.setEditable(up);
            });
        }
    }
}
