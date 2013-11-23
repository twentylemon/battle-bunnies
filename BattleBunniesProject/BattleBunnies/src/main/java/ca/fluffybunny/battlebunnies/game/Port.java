package ca.fluffybunny.battlebunnies.game;

import java.io.Closeable;
import java.io.Serializable;

/**
 * Port interface for communication between devices. Or in the single player case, communication
 * between the phone and itself.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 *
 */
public interface Port extends Closeable {

    /**
     * Sends the object over the communication pipeline.
     *
     * @param obj the object to send
     */
    public void send(Serializable obj);


    /**
     * Blocks and waits for an object to be received over the pipeline.
     *
     * @return the object received
     */
    public Serializable receive();
}
