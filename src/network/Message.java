package network;

import java.io.Serializable;

public class Message implements Serializable {
    public String message;
    public Boolean owned;

    public Message(String message, Boolean owned) {
        this.message = message;
        this.owned = owned;
    }
}
