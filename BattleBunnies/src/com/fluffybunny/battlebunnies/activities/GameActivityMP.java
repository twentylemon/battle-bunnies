package com.fluffybunny.battlebunnies.activities;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

import com.fluffybunny.battlebunnies.bluetooth.BluetoothHandler;
import com.fluffybunny.battlebunnies.game.Action;
import com.fluffybunny.battlebunnies.game.FireAction;
import com.fluffybunny.battlebunnies.game.GameCanvas;
import com.fluffybunny.battlebunnies.game.GameInfo;
import com.fluffybunny.battlebunnies.game.MoveAction;

/**
 * The multiplayer game screen
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-12-07
 */
public class GameActivityMP extends GameActivitySP {
	
	public static final String IS_SERVER = "isServer";
	public static final String REMOVE_DEVICE_ADDRESS = "remoteAddress";
	
	public static final String NAME = "GameActivityMP";
	public static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-9750200c9a66");
	private BluetoothHandler btHandler;

	private boolean isServer;
	private String remoteAddress;
	
	@Override
	public void onDestroy(){
		if (btHandler != null){
			btHandler.stop();
		}
	}
		
	
	/**
	 * Performs the player's turn.
	 */
	@Override
	protected void myTurn(){
		FireAction action = new FireAction(game.getMyID(), shotPower, shotAngle, getSelectedWeapon());
		btHandler.write(action);
		action.execute(game);
		waitForCanvas();
	}
	
	
	/**
	 * Performs the other player's turn. In multiplayer, we keep reading remote actions
	 * until we read a FireAction. Then it's our turn again.
	 */
	@Override
	protected void otherTurn(){
		Action action = null;
		while (!(action instanceof FireAction)){
			action = (Action) btHandler.read();
			action.execute(game);
		}
		waitForCanvas();
	}


	/**
	 * Moves the bunny specified. In multiplayer, we need to send this information
	 * to the remote device.
	 * 
	 * @param playerID the bunny to move
	 * @param left true if they are moving left, false if right
	 */
	protected void moveBunny(int playerID, boolean left){
		super.moveBunny(playerID, left);
		btHandler.write(new MoveAction(playerID, left));
	}
	
	
	/**
	 * Create the game info for the mutliplayer game.
	 * Any { @code onCreate } work should be done here.
	 * 
	 * @return the game info
	 */
	@Override
	protected GameInfo makeGameInfo(){
		//server side, we are going to create the game info and send it to the other player
		if (isServer){
			GameInfo g = super.makeGameInfo();
			while (btHandler.getState() != BluetoothHandler.STATE_CONNECTED){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.e("GameActivityMP", "waiting for bluetooth to connect");
				}
			}
			btHandler.write(g);
			return g;
		}
		//client side, we are going to receive the game info and add in stuff for the canvas etc
		else {
			while (btHandler.getState() != BluetoothHandler.STATE_CONNECTED){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.e("GameActivityMP", "waiting for bluetooth to connect");
				}
			}
			GameInfo g = (GameInfo) btHandler.read();
			g.setID(1);
			g.getBunny(0).setGameCanvas(null);
			game.getTerrain().setBitmapSize(size.x, size.y);
			gameCanvas = new GameCanvas(g, surfaceHolder);
			g.getBunny(1).setGameCanvas(gameCanvas);
		}
		return null;
	}
	
	
	/**
	 * Handles pressing of the fire button.
	 */
	@Override
	protected void firePressed(){
		if (!gameCanvas.isFiring()){
			if (isServer){
				myTurn();
				otherTurn();
			}
			else {
				otherTurn();
				myTurn();
			}
			checkEndGame();
		}
	}
	
	
	/**
	 * Sets up stuff. Like UI stuff and stuff.
	 */
	@Override
	protected void setup(){
		super.setup();

		Intent intent = getIntent();
		
		isServer = intent.getBooleanExtra(IS_SERVER, false);
		btHandler = new BluetoothHandler();
		
		if (isServer){
			btHandler.startServer();
		}
		else {
			remoteAddress = intent.getStringExtra(REMOVE_DEVICE_ADDRESS);
			BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteAddress);
			btHandler.startClient(device);
		}
	}
}
