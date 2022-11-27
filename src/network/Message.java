package network;

import java.io.Serializable;

public class Message implements Serializable {
    public String string;
    public Boolean owned;

    public Message(String string, Boolean owned) {
        this.string = string;
        this.owned = owned;
    }
}
