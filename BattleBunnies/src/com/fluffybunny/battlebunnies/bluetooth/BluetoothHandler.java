package com.fluffybunny.battlebunnies.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.fluffybunny.battlebunnies.activities.GameActivityMP;

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
	
	public BluetoothHandler(){
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
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        setState(STATE_CONNECTED);
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
	 */
	public void write(Object obj){
		ConnectedThread t;
		synchronized (this){
			if (getState() == STATE_CONNECTED){
				t = connectedThread;
			}
			else {
				return;
			}
		}
		t.write(obj);
	}
	
	
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

        private final BluetoothServerSocket mmServerSocket;

        public ServerThread(){
            BluetoothServerSocket tmp = null;

            try {
                tmp = adapter.listenUsingRfcommWithServiceRecord(GameActivityMP.NAME, GameActivityMP.MY_UUID);
            } catch (IOException e){}
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (currentState != STATE_CONNECTED){
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e){
                    socket = null;
                }

                //if a connection was accepted
                if (socket != null){
                    synchronized (BluetoothHandler.this){
                        switch (currentState){
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            //start the connected thread.
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            //either not ready or already connected, terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e){}
                            break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
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

        public ClientThread(BluetoothDevice device) {
            this.device = device;
            BluetoothSocket tmp = null;

            //get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(GameActivityMP.MY_UUID);
            } catch (IOException e){}
            socket = tmp;
        }

        public void run() {
            //always cancel discovery because it will slow down a connection
            adapter.cancelDiscovery();

            //make a connection to the BluetoothSocket
            try {
                socket.connect();
            } catch (IOException e){
                connectionFailed();
                try {
                    socket.close();
                } catch (IOException e2){}
                //start the service over to restart listening mode
                BluetoothHandler.this.startServer();
                return;
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
        private ObjectInputStream inStream;
        private ObjectOutputStream outStream;

        public ConnectedThread(BluetoothSocket socket){
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e){}

            try {
				inStream = new ObjectInputStream(tmpIn);
	            outStream = new ObjectOutputStream(tmpOut);
			} catch (StreamCorruptedException e){
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
        }

        public void run(){
            //keep listening to the InputStream while connected
            while (currentState == STATE_CONNECTED){
            	try {
					Thread.sleep(250);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
            }
        }
        
        public Object read(){
        	Object obj = null;
            try {
                obj = inStream.readObject();
            } catch (IOException e){
                connectionLost();
            } catch (ClassNotFoundException e){
				e.printStackTrace();
			}
            return obj;
        }
        
        public void write(Object obj){
        	try {
				outStream.writeObject(obj);
			} catch (IOException e){
				e.printStackTrace();
			}
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e){}
        }
    }
}
