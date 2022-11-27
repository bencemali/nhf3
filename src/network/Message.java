package network;

import java.io.Serializable;

/**
 * Represents a message sent and received through a network connection
 */
public class Message implements Serializable {
    /**
     * The textual content of the message
     */
    private String string;

    /**
     * Represents whether the message was sent or received by this host
     */
    private Boolean owned;

    /**
     * Constructor
     *
     * @param string the content
     * @param owned whether the message was sent or received
     */
    public Message(String string, Boolean owned) {
        this.string = string;
        this.owned = owned;
    }

    /**
     * Returns the content string
     *
     * @return the content string
     */
    synchronized public String getString() {
        return string;
    }

    /**
     * Returns whether the message was sent or received by this host
     *
     * @return whether the message was sent or received
     */
    synchronized public Boolean getOwned() {
        return owned;
    }
}
