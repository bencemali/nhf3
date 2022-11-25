package gui;

import network.ContactData;
import network.Message;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

/**
 * TODO javadoc
 */
public class ChatPane extends JPanel {
    private ContactData contactData;
    private int focused;
    private JScrollPane messageScrollPane;
    private JPanel messagePanel;
    private JTextField messageTextField;
    private JButton sendButton;
    private JPanel sendPanel;

    public ChatPane(ContactData contactData) {
        System.out.println("ChatPane(ContactData)");
        this.contactData = contactData;
        focused = -1;
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
        sendPanel = new JPanel();
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
        sendButton = new JButton("Send");
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

    public void displayChat(int chatIndex) {
        System.out.println("displayChat()");
        focused = chatIndex;
        messagePanel.removeAll();
        messagePanel.validate();
        messageTextField.setEditable(contactData.getContact(focused).isConnected());
        List<Message> messages = contactData.getContact(focused).getMessages();
        System.out.println("List of messages: ");
        for(Message m : messages) {
            System.out.println(m.message);
        }
        contactData.getContact(focused).connect();
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        for(Message message : messages) {
            JLabel messageLabel = new JLabel(message.message);
            if(message.owned) {
                //messageLabel.setBackground(UIManager.getColor("TitlePane.background"));
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
        this.contactData.getContact(focused).setMessageListener(() -> {
            System.out.println("MessageListener");
            if(focused == chatIndex) {
                displayChat(focused);
            }
        });
        this.contactData.getContact(chatIndex).setConnectionListener((open) -> {
            System.out.println("ConnectionListener");
            if(!open) {
                messageTextField.setEditable(false);
            }
        });
    }
}
