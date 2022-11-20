package gui;

import network.ContactData;
import network.Message;

import javax.swing.*;
import java.awt.*;

/**
 * TODO javadoc
 */
public class ChatPane extends JPanel {
    private ContactData contactData;
    private int focused;
    private JPanel messagePanel;
    private JPanel sendPanel;

    public ChatPane(ContactData contactData) {
        this.contactData = contactData;
        focused = -1;
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
        sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.LINE_AXIS));
        JTextField messageTextField = new JTextField(40);
        sendPanel.add(messageTextField);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            contactData.getContact(focused).sendMessage(messageTextField.getText());
        });
        sendPanel.add(sendButton);
        setLayout(new BorderLayout());
        add(messagePanel, BorderLayout.CENTER);
        add(sendPanel, BorderLayout.SOUTH);
    }

    public void displayChat(int chatIndex) {
        focused = chatIndex;
        messagePanel.removeAll();
        for(Message message : contactData.getContact(chatIndex).getMessages()) {
            JLabel messageLabel = new JLabel(message.message);
            if(message.owned) {
                messageLabel.setBackground(Color.CYAN);
            }
            messagePanel.add(messageLabel);
        }
        this.contactData.getContact(chatIndex).setMessageListener(() -> {
            if(focused == chatIndex) {
                displayChat(focused);
            }
        });
    }
}
