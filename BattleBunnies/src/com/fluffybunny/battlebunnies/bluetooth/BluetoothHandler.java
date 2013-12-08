package com.fluffybunny.battlebunnies.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fluffybunny.battlebunnies.activities.GameActivityMP;
import com.fluffybunny.battlebunnies.game.FireAction;
import com.fluffybunny.battlebunnies.game.MoveAction;
import com.fluffybunny.battlebunnies.game.Point;

@SuppressLint("NewApi")
public class BluetoothHandler {

	
	private final BluetoothAdapter adapter;
	
	private ServerThread serverThread;
	private ClientThread clientThread;
	private ConnectedThread connectedThread;
	
	private int currentState;
	
	public static final int STATE_NONE = 0;
	public static final int STATE_LISTEN = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;
	
	private Handler handler;
	
	public BluetoothHandler(Handler handler){
		this.handler = handler;
		currentState = STATE_NONE;
		adapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	
	/**
	 * Getters/Setters.
	 */
	public synchronized void setState(int state){ currentState = state; }
	public synchronized int getState(){ return currentState; }
	
	
	/**
	 * Starts the server thread if not started already.
	 */
	public synchronized void startServer(){
		if (connectedThread != null){
			connectedThread.cancel();
			connectedThread = null;
		}
		if (serverThread == null){
			serverThread = new ServerThread();
			serverThread.start();
		}
		setState(STATE_LISTEN);
	}
	
	
	/**
	 * Starts trying to connect to the remote device as a client.
	 * @param device the remote device acting as a server
	 */
	public synchronized void startClient(BluetoothDevice device){
		if (getState() == STATE_CONNECTING && clientThread != null){
			clientThread.cancel();
			clientThread = null;
		}
		clientThread = new ClientThread(device);
		clientThread.start();
		setState(STATE_CONNECTING);
	}
	
	
	/**
	 * Starts up the connected thread which can communicate between devices.
	 * 
	 * @param socket the connected socket
	 * @param device the remote server device (not used?)
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device){
        stop();
        setState(STATE_CONNECTED);
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
	}
	
	
	/**
	 * Stops all the threads and resets the current connection state.
	 */
	public synchronized void stop(){
        if (clientThread != null){
        	clientThread.cancel();
        	clientThread = null;
        }
        if (serverThread != null){
        	serverThread.cancel();
        	serverThread = null;
        }
        if (connectedThread != null){
        	connectedThread.cancel();
        	connectedThread = null;
        }
		setState(STATE_NONE);
	}
	
	
	/**
	 * Tells the connected thread to write the object to the output stream.
	 * @param obj the object to send
	 * @param messageType the type of object we're sending
	 */
	public void write(Object obj, int messageType){
		ConnectedThread t;
		synchronized (this){
			if (currentState == STATE_CONNECTED){
				t = connectedThread;
			}
			else {
				return;
			}
		}
		t.write(obj, messageType);
	}
	
	
	/**
	 * Reads an object from the stream.
	 * 
	 * @return an object sent
	 */
	public Object read(){
		ConnectedThread t;
		synchronized (this){
			if (getState() == STATE_CONNECTED){
				t = connectedThread;
			}
			else {
				return null;
			}
		}
		return t.read();
	}

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed(){
        setState(STATE_LISTEN);
    }

    
    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost(){
        setState(STATE_LISTEN);
    }

    
    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class ServerThread extends Thread {

        private final BluetoothServerSocket serverSocket;

        public ServerThread(){
            BluetoothServerSocket tmp = null;

            try {
                tmp = adapter.listenUsingRfcommWithServiceRecord(GameActivityMP.NAME, GameActivityMP.MY_UUID);
            } catch (IOException e){}
            serverSocket = tmp;
        }

        public void run(){
            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (currentState != STATE_CONNECTED){
                try {
                    socket = serverSocket.accept();
                } catch (IOException e){
                    socket = null;
                }

                //if a connection was accepted
                if (socket != null){
                    synchronized (BluetoothHandler.this){
                        switch (currentState){
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            //start the connected thread
                        	connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            //either not ready or already connected, terminate new socket.
                        	cancel();
                            break;
                        }
                    }
                }
            }
        }

        public void cancel(){
            try {
            	serverSocket.close();
            } catch (IOException e){}
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ClientThread extends Thread {
    
        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        public ClientThread(BluetoothDevice device){
            this.device = device;
            BluetoothSocket tmp = null;

            //get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(GameActivityMP.MY_UUID);
            } catch (IOException e){}
            socket = tmp;
        }

        public void run(){
            //always cancel discovery because it will slow down a connection
            adapter.cancelDiscovery();

            //make a connection to the BluetoothSocket
            try {
                socket.connect();
            } catch (IOException e){
                connectionFailed();
                try {
                    socket.close();
                } catch (IOException e2){
                	startClient(device);
                	return;
                }
            }

            //reset the ClientThread because we're done
            synchronized (BluetoothHandler.this){
                clientThread = null;
            }

            //start the connected thread
            connected(socket, device);
        }

        public void cancel(){
            try {
                socket.close();
            } catch (IOException e){}
        }
    }

    
    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private InputStream in;
        private OutputStream out;
        private final int BUFFER_SIZE = 1024;

		public ConnectedThread(BluetoothSocket socket){
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e){}
            in = tmpIn;
            out = tmpOut;
            Log.e("bt", "in = " + in.toString() +
            		"  out = " + out.toString());
        }

        public void run(){
            //keep listening to the InputStream while connected
            while (currentState == STATE_CONNECTED){
            	int messageType = -1;
            	Object obj = read();
            	if (obj instanceof Long){
            		messageType = GameActivityMP.MESSAGE_SEED;
            	}
            	else if (obj instanceof Point){
            		messageType = GameActivityMP.MESSAGE_SIZE;
            	}
            	else if (obj instanceof FireAction){
            		messageType = GameActivityMP.ACTION_FIRE;
            	}
            	else if (obj instanceof MoveAction){
            		messageType = GameActivityMP.ACTION_MOVE;
            	}
            	Message msg = handler.obtainMessage(messageType, obj);
            	handler.sendMessage(msg);
            }
        }
        
        
        /**
         * Unserializes the byte array into an object.
         * 
         * @param data the byte array to unserialize
         * @return the object represented by the data
         */
        private Object unserialize(byte[] data){
        	ByteArrayInputStream bais = new ByteArrayInputStream(data);
        	ObjectInputStream ois = null;
        	try {
				Log.e("unserialize", "making ois");
				ois = new ObjectInputStream(bais);
			} catch (StreamCorruptedException e){
				Log.e("unserialize", "stream corrupted while making ois");
				e.printStackTrace();
			} catch (IOException e){
				Log.e("unserialize", "exception while making ois");
				e.printStackTrace();
			}
        	try {
				Log.e("unserialize", "reading object");
				Object o = ois.readObject();
				Log.e("unserialize", "read a " + o);
				return o;
			} catch (OptionalDataException e){
				Log.e("unserialize", "OptionalDataException while reading object");
				e.printStackTrace();
			} catch (ClassNotFoundException e){
				Log.e("unserialize", "ClassNotFoundException while reading object");
				e.printStackTrace();
			} catch (IOException e){
				Log.e("unserialize", "IOException while reading object");
				e.printStackTrace();
			}
        	Log.e("unserialize", "returning null from unserialize()");
        	return null;
        }
        
        
        /**
         * Serializes the object sent.
         * 
         * @param obj the object to serialize
         * @return the serialization of the object
         */
        private byte[] serialize(Object obj){
        	ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(baos);
			} catch (IOException e){
				Log.e("serialize", "exception while making oos");
				e.printStackTrace();
			}
			try {
				Log.e("serialize", "serializing " + obj);
				oos.writeObject(obj);
			} catch (IOException e){
				Log.e("serialize", "exception while writing " + obj);
				e.printStackTrace();
			}
			try {
				oos.close();
			} catch (IOException e){
				Log.e("serialize", "exception while closing oos");
				e.printStackTrace();
			}
        	return baos.toByteArray();
        }
        
        
        /**
         * Reads an object from the input stream.
         * 
         * @return an object from the input stream
         */
        public Object read(){
        	byte[] buffer = new byte[BUFFER_SIZE];
        	try {
        		Log.e("read", "read " + in.read(buffer) + " bytes");
			} catch (IOException e){
				Log.e("read", "exception during read()");
				e.printStackTrace();
			}
        	return unserialize(buffer);
        }
        
       
        /**
         * Writes an object on the output stream.
         * 
         * @param obj the object to send
         */
        public void write(Object obj, int messageType){
        	byte[] buffer = serialize(obj);
        	try {
        		Log.e("write", "writing " + obj);
				out.write(buffer);
				out.flush();
				Log.e("write", "write successful");
			} catch (IOException e){
				Log.e("read", "exception during write()");
				e.printStackTrace();
			}
        	//handler.obtainMessage(messageType, obj).sendToTarget();
        }

        public void cancel(){
            try {
            	connectionLost();
            	synchronized (this){
            		notify();
            	}
            	in.close();
            	out.close();
                socket.close();
            } catch (IOException e){}
        }
    }
}
