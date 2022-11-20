package gui;

import network.ContactData;
import network.Message;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * TODO javadoc
 */
public class ChatPane extends JPanel {
    private ContactData contactData;
    private int focused;
    private JPanel messagePanel;
    private JPanel sendPanel;

    public ChatPane(ContactData contactData) {
        System.out.println("ChatPane(ContactData)");
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
            this.contactData.getContact(focused).sendMessage(messageTextField.getText());
        });
        sendPanel.add(sendButton);
        setLayout(new BorderLayout());
        add(messagePanel, BorderLayout.CENTER);
        add(sendPanel, BorderLayout.SOUTH);
    }

    public void displayChat(int chatIndex) {
        System.out.println("displayChat()");
        focused = chatIndex;
        messagePanel.removeAll();
        List<Message> messages = contactData.getContact(chatIndex).getMessages();
        contactData.getContact(chatIndex).connect();
        for(Message message : messages) {
            JLabel messageLabel = new JLabel(message.message);
            if(message.owned) {
                messageLabel.setBackground(Color.CYAN);
            }
            messagePanel.add(messageLabel);
        }
        this.contactData.getContact(chatIndex).setMessageListener(() -> {
            System.out.println("MessageListener");
            if(focused == chatIndex) {
                displayChat(focused);
            }
        });
    }
}
