package ca.fluffybunny.battlebunnies.game;

import java.io.IOException;
import java.io.Serializable;

import ca.fluffybunny.battlebunnies.util.Queue;

/**
 * The single player Port implementation. No real communication needs to happen here since the
 * GameMaster and the Player will be running on the same device.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public class Channel {

    private Queue<Serializable> masterToClient;
    private Queue<Serializable> clientToMaster;

    /**
     * Default constructor. Initializes the queues.
     */
    public Channel(){
        masterToClient = new Queue<Serializable>();
        clientToMaster = new Queue<Serializable>();
    }


    /**
     * Pushes an item from the master to the client.
     *
     * @param obj the object to send
     */
    public void pushMasterPort(Serializable obj){
        masterToClient.push(obj);
    }


    /**
     * Receives an item from the a client.
     *
     * @return the item received
     */
    public Serializable pullMasterPort(){
        return clientToMaster.pull();
    }


    /**
     * Pushes an item from the client to the master.
     *
     * @param obj the object to send
     */
    public void pushClientPort(Serializable obj){
        clientToMaster.push(obj);
    }


    /**
     * Receives an item from the game master.
     *
     * @return the item received
     */
    public Serializable pullClientPort(){
        return masterToClient.pull();
    }


    /**
     * Returns the port to be used by the { @link GameMaster }.
     *
     * @return the port the GameMaster needs to use
     */
    public Port asMasterPort(){
        return new Port(){

            /**
             * Sends the object over the communication pipeline.
             *
             * @param obj the object to send
             */
            @Override
            public void send(Serializable obj) {
                pushMasterPort(obj);
            }

            /**
             * Blocks and waits for an object to be received over the pipeline.
             *
             * @return the object received
             */
            @Override
            public Serializable receive() {
                return pullMasterPort();
            }

            /**
             * Closes the object and release any system resources it holds.
             * <p/>
             * <p>Although only the first call has any effect, it is safe to call close
             * multiple times on the same object. This is more lenient than the
             * overridden {@code AutoCloseable.close()}, which may be called at most
             * once.
             */
            @Override
            public void close() throws IOException {
            }
        };
    }


    /**
     * Returns the port to be used by the { @link Player }.
     *
     * @return the port a Player needs to use
     */
    public Port asClientPort(){
        return new Port(){

            /**
             * Sends the object over the communication pipeline.
             *
             * @param obj the object to send
             */
            @Override
            public void send(Serializable obj) {
                pushClientPort(obj);
            }

            /**
             * Blocks and waits for an object to be received over the pipeline.
             *
             * @return the object received
             */
            @Override
            public Serializable receive() {
                return pullClientPort();
            }

            /**
             * Closes the object and release any system resources it holds.
             * <p/>
             * <p>Although only the first call has any effect, it is safe to call close
             * multiple times on the same object. This is more lenient than the
             * overridden {@code AutoCloseable.close()}, which may be called at most
             * once.
             */
            @Override
            public void close() throws IOException {
            }
        };
    }
}
