package ca.fluffybunny.battlebunnies.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


import ca.fluffybunny.battebunnies.activities.MultiplayerLaunchActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;



public class ConnectionHandler {
	public static final UUID MY_UUID = new UUID(15644184L,15649461L);
	public static final String TAG = "ConnectionHandler";
    private static final boolean D = true;
    
 // Name for the SDP record when creating server socket
    public static final String NAME = "BattleBunnies";
    // Member fields
    private final BluetoothAdapter mAdapter;
 //   private final Handler mHandler;
    private Accepter mAccepter;
    private Connecter mConnecter;
    public int mState;
    
    //possible states for the bluetooth device
    public static final int STATE_NONE = 0;     
    public static final int STATE_LISTEN = 1;     
    public static final int STATE_CONNECTING = 2; 
    public static final int STATE_CONNECTED = 3;  
    
    //the Bluetooth socket used for communication
    private BluetoothSocket mySock;
    
    //context passed to object from multiplayer launch activity
    private Context cont;

    public ConnectionHandler(Context context) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        cont=context;
    }
    /**
     * returns the BluetoothSocket held by the class
     * 
     * @return BluetoothSocket
     */
    public synchronized BluetoothSocket getBluetoothSocket(){
    	return mySock;
    }
    
    /**
     * Initializes the thread used for the server side of the connection
     * 
     */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnecter != null) {mConnecter.cancel(); mConnecter = null;}


        // Start the thread to listen on a BluetoothServerSocket
        if (mAccepter == null) {
            mAccepter = new Accepter();
            mAccepter.start();
        }
        mState=STATE_LISTEN;
    }
    
    
    /**
     * Initializes the thread used for the client side of the connection
     * Stops any current connections
     * 
     */
    public synchronized void connect(BluetoothDevice device) {
    	
    	if (D) Log.d(TAG, "connect to: " + device);

        if (mState == STATE_CONNECTING) {
            if (mConnecter != null) {
            	mConnecter.cancel(); 
            	mConnecter = null;
            	}
        }

        // Start the thread to connect with the given device
        mConnecter = new Connecter(device);
        mConnecter.start();
        mState=STATE_CONNECTING;
    }
    
    /**
     * Sets the State on a failed connection
     */
    private void connectionFailed() {
        mState=STATE_LISTEN;
    }
    
    /**
     * The Server side thread for the connection.
     * Gets a BluetoothSocket using the UUID declared above
     */
	   private class Accepter extends Thread {
	        
	        private final BluetoothServerSocket mmServerSocket;

	        public Accepter() {
	            BluetoothServerSocket temp = null;

	            // Create a new listening server socket
	            try {
	                temp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
	            } catch (IOException e) {
	                Log.e(TAG, "listen() failed", e);
	            }
	            mmServerSocket = temp;
	            
	        }

	        public void run() {
	            if (D) Log.d(TAG, "BEGIN mAccepter" + this);
	            setName("Accepter");
	            BluetoothSocket socket = null;

	            // loop till the connection is done, and we are paired
	            while (mState != STATE_CONNECTED) {
	                try {
	                    // This is a blocking call and will only return on a
	                    // successful connection or an exception
	                    socket = mmServerSocket.accept();
	                } catch (IOException e) {
	                    Log.e(TAG, "accept() failed", e);
	                    break;
	                }

	                // If a connection was accepted
	                if (socket != null) {
	                    synchronized (ConnectionHandler.this) {
	                        switch (mState) {
	                        case STATE_LISTEN:
	                        case STATE_CONNECTING:
	                            // Situation normal. Start the connected thread.
	                            break;
	                        case STATE_NONE:
	                        case STATE_CONNECTED:
	                            //there were isues with threading if this has been reached. 
	                        	//shut it down
	                            try {
	                                socket.close();
	                            } catch (IOException e) {
	                                Log.e(TAG, "Could not close unwanted socket", e);
	                            }
	                            break;
	                        }
	                    }
	                }
	            }
	            mySock=socket;
	            if (D) Log.i(TAG, "END mAccepter");
	        }

	        public void cancel() {
	            if (D) Log.d(TAG, "cancel " + this);
	            try {
	                mmServerSocket.close();
	            } catch (IOException e) {
	                Log.e(TAG, "cancel did not work", e);
	            }
	        }
	    }


	    /**
	     * The client side thread for the connection.
	     * Gets a BluetoothSocket using the UUID declared above
	     */
	    private class Connecter extends Thread {
	        private final BluetoothSocket mmSocket;
	        private final BluetoothDevice mmDevice;

	        public Connecter(BluetoothDevice device) {
	            mmDevice = device;
	            BluetoothSocket tmp = null;
	            try {
	                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	            } catch (IOException e) {
	                Log.e(TAG, "create() failed", e);
	            }
	            mmSocket = tmp;
	            mySock=tmp;
	            if(mySock != null){
	            Toast.makeText(cont, "Conection Established", Toast.LENGTH_LONG).show();
	            }
	        }

	        public void run() {
	            Log.i(TAG, "BEGIN mConnecter");
	            setName("Connecter");

	           //save battery
	            mAdapter.cancelDiscovery();

	            // Make a connection to the BluetoothSocket
	            try {
	                mmSocket.connect();
	            } catch (IOException e) {
	                connectionFailed();
	                try {
	                    mmSocket.close();
	                } catch (IOException e2) {
	                    Log.e(TAG, "unable to close socket ", e2);
	                }
	                
	                return;
	            }

	            
	            synchronized (ConnectionHandler.this) {
	                mConnecter = null;
	            }
	        }

	        public void cancel() {
	            try {
	                mmSocket.close();
	            } catch (IOException e) {
	                Log.e(TAG, "cancel did not work", e);
	            }
	        }
	    }

	}

