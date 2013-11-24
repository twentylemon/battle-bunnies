package ca.fluffybunny.battlebunnies.game;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Bluetooth Port implementation.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-24
 */
public class BluetoothChannel implements Port {

    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    /**
     * Initializes this communication channel.
     *
     * @param socket the bluetooth socket to communicate over
     */
    public BluetoothChannel(BluetoothSocket socket){
        this.socket = socket;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            reader = new ObjectInputStream(in);
            writer = new ObjectOutputStream(out);
        } catch (StreamCorruptedException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Sends the object over the communication pipeline.
     *
     * @param obj the object to send
     */
    @Override
    public void send(Serializable obj){
        try {
            writer.writeObject(obj);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Blocks and waits for an object to be received over the pipeline.
     *
     * @return the object received
     */
    @Override
    public Serializable receive(){
        try {
            return (Serializable)reader.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        }
        return null;
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
        reader.close();
        writer.close();
        in.close();
        out.close();
        socket.close();
    }
}
