package ca.fluffybunny.battlebunnies.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;




import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class ConnectionHandler {
	private static final UUID MY_UUID = new UUID(15644184L,15649461L);
	private static final String TAG = "ConnectionHandler";
    private static final boolean D = true;
    
 // Name for the SDP record when creating server socket
    private static final String NAME = "BattleBunnies";
    // Member fields
    private final BluetoothAdapter mAdapter;
 //   private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private int mState;
    
    public static final int STATE_NONE = 0;     
    public static final int STATE_LISTEN = 1;     
    public static final int STATE_CONNECTING = 2; 
    public static final int STATE_CONNECTED = 3;  
    private BluetoothSocket mySock;

    public ConnectionHandler(Context context) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
      //  mHandler = handler;
    }
    public BluetoothSocket getBluetoothSocket(){
    	return mySock;
    }
    
    public synchronized void connect(BluetoothDevice device) {

        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
            	mConnectThread.cancel(); 
            	mConnectThread = null;
            	}
        }

        // Cancel any thread currently running a connection
      //  if (mConnectedThread != null) {
      //  	mConnectedThread.cancel(); 
      //  	mConnectedThread = null;
      //  }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        mState=STATE_CONNECTING;
    }
    
    private void connectionFailed() {
        mState=STATE_LISTEN;

    }

	   private class AcceptThread extends Thread {
	        // The local server socket
	        private final BluetoothServerSocket mmServerSocket;

	        public AcceptThread() {
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
	            if (D) Log.d(TAG, "BEGIN mAcceptThread" + this);
	            setName("AcceptThread");
	            BluetoothSocket socket = null;

	            // Listen to the server socket if we're not connected
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
	                            // Either not ready or already connected. Terminate new socket.
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
	            if (D) Log.i(TAG, "END mAcceptThread");
	        }

	        public void cancel() {
	            if (D) Log.d(TAG, "cancel " + this);
	            try {
	                mmServerSocket.close();
	            } catch (IOException e) {
	                Log.e(TAG, "close() of server failed", e);
	            }
	        }
	    }


	    /**
	     * This thread runs while attempting to make an outgoing connection
	     * with a device. It runs straight through; the connection either
	     * succeeds or fails.
	     */
	    private class ConnectThread extends Thread {
	        private final BluetoothSocket mmSocket;
	        private final BluetoothDevice mmDevice;

	        public ConnectThread(BluetoothDevice device) {
	            mmDevice = device;
	            BluetoothSocket tmp = null;

	            // Get a BluetoothSocket for a connection with the
	            // given BluetoothDevice
	            try {
	                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	            } catch (IOException e) {
	                Log.e(TAG, "create() failed", e);
	            }
	            mmSocket = tmp;
	            mySock=tmp;
	        }

	        public void run() {
	            Log.i(TAG, "BEGIN mConnectThread");
	            setName("ConnectThread");

	           
	            mAdapter.cancelDiscovery();

	            // Make a connection to the BluetoothSocket
	            try {
	                // This is a blocking call and will only return on a
	                // successful connection or an exception
	                mmSocket.connect();
	            } catch (IOException e) {
	                connectionFailed();
	                // Close the socket
	                try {
	                    mmSocket.close();
	                } catch (IOException e2) {
	                    Log.e(TAG, "unable to close() socket during connection failure", e2);
	                }
	                
	                return;
	            }

	            // Reset the ConnectThread because we're done
	            synchronized (ConnectionHandler.this) {
	                mConnectThread = null;
	            }
	        }

	        public void cancel() {
	            try {
	                mmSocket.close();
	            } catch (IOException e) {
	                Log.e(TAG, "close() of connect socket failed", e);
	            }
	        }
	    }

	}

