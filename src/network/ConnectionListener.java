package network;

/**
 * Listener to listen to connection updates
 */
public interface ConnectionListener {
    /**
     * The state of the connection has changed
     *
     * @param up whether the connection is up
     */
    void connectionChanged(boolean up);
}
