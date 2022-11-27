package network;

/**
 * Listener listening for the disposal of a contact
 */
public interface DisposeListener {
    /**
     * Signals the disposal of a contact
     *
     * @param idx the index of the contact disposed of
     */
    void disposed(int idx);
}
