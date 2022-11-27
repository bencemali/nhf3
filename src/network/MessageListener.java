package network;

/**
 * Listener listening for the arrival or the sending of a new message
 */
public interface MessageListener {
    /**
     * Signals the arrival or sending of a new message
     */
    void newMessage();
}
