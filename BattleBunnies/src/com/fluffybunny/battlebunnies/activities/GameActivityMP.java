package com.fluffybunny.battlebunnies.activities;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.fluffybunny.battlebunnies.bluetooth.BluetoothHandler;
import com.fluffybunny.battlebunnies.game.Action;
import com.fluffybunny.battlebunnies.game.FireAction;
import com.fluffybunny.battlebunnies.game.GameCanvas;
import com.fluffybunny.battlebunnies.game.GameInfo;
import com.fluffybunny.battlebunnies.game.MoveAction;
import com.fluffybunny.battlebunnies.game.Point;

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
	
	public static final int MESSAGE_SEED = 1;
	public static final int MESSAGE_SIZE = 2;
	public static final int ACTION_MOVE = 3;
	public static final int ACTION_FIRE = 4;
	
	private boolean isServer;
	private String remoteAddress;

	
	/**
	 * Handles messages/actions being passed.
	 */
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what){
			case MESSAGE_SEED:
				generator.setSeed((Long) msg.obj);
				break;
			case MESSAGE_SIZE:
				Point s = (Point) msg.obj;
				game = new GameInfo(playerImages, playerNames, s.x, s.y, generator);
				game.setID(1);
				break;
			case ACTION_MOVE:
				MoveAction move = (MoveAction) msg.obj;
				move.execute(game);
				break;
			case ACTION_FIRE:
				FireAction fire = (FireAction) msg.obj;
				fire.execute(game);
				gameCanvas.setFiring(true);
				meGo = true;
				waitForCanvas();
				break;
			}
		}
	};
	

	@Override
	public void onDestroy(){
		if (btHandler != null){
			btHandler.stop();
		}
		super.onDestroy();
	}
	

	/**
	 * Stops the bluetooth handler and the game canvas.
	 */
	@Override
	public void onBackPressed(){
		btHandler.stop();
		super.onBackPressed();
	}
	
	
	/**
	 * Starts the game. The client is always player 2, so they have to wait for
	 * player 1's turn first.
	 */
	protected void startGame(){
		if (isServer){
			super.startGame();
			meGo = true;
		}
		else {
			gameCanvas = new GameCanvas(game, surfaceHolder, getResources());
			gameCanvas.setBitmapSize(size.x, size.y);
			gameCanvas.start();
			meGo = false;
		}
	}
		
	
	/**
	 * Performs the player's turn.
	 */
	@Override
	protected void myTurn(){
		FireAction action = new FireAction(game.getMyID(), shotPower, shotAngle, getSelectedWeapon());
		btHandler.write(action, ACTION_FIRE);
		action.execute(game);
		gameCanvas.setFiring(true);
		meGo = false;
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
			if (action instanceof FireAction){
				gameCanvas.setFiring(true);
			}
			waitForCanvas();
		}
		meGo = true;
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
		btHandler.write(new MoveAction(playerID, left), ACTION_MOVE);
	}
	
	
	/**
	 * Create the game info for the mutliplayer game.
	 * Any { @code onCreate } work should be done here.
	 * 
	 * @return the game info
	 */
	@Override
	protected GameInfo makeGameInfo(){
		while (btHandler.getState() != BluetoothHandler.STATE_CONNECTED){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Log.e("GameActivityMP", "waiting for bluetooth to connect");
			}
		}
		//server side, we are going to create the game info and send it to the other player
		if (isServer){
			GameInfo g = super.makeGameInfo();
			btHandler.write(generator.getSeed(), MESSAGE_SEED);
			btHandler.write(new Point(size.x, size.y), MESSAGE_SIZE);
			return g;
		}
		//client side, we are going to receive the game info and add in stuff for the canvas etc
		else {
			while (game == null){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			return game;
		}
	}
	
	
	/**
	 * Handles pressing of the fire button.
	 */
	@Override
	protected void firePressed(){
		if (meGo && !gameCanvas.isFiring()){
			myTurn();
			checkEndGame();
		}
	}
	
	
	/**
	 * Checks for the game being over. If it is, the activity ends.
	 * Increments the campaign if they won and it is campaign mode.
	 */
	@Override
	protected void checkEndGame(){
		if (game.isGameOver()){
			waitForCanvas();
			gameCanvas.stop();
			SharedPreferences prefs = getSharedPreferences(ProfileActivity.PREFS_NAME, MODE_PRIVATE);
			int wins = prefs.getInt(ProfileActivity.PLAYER_WINS, 0);
			int loss = prefs.getInt(ProfileActivity.PLAYER_LOSS, 0);
			int myScore = game.getBunny(game.getMyID()).getScore();
			int aiScore = game.getBunny(game.otherID(game.getMyID())).getScore();
			if (myScore > aiScore){
				Toast.makeText(getApplicationContext(), "You won! " + myScore + " to " + aiScore, Toast.LENGTH_LONG).show();
				wins++;
			}
			else if (myScore < aiScore){
				loss++;
				Toast.makeText(getApplicationContext(), "Failure. " + myScore + " to " + aiScore, Toast.LENGTH_LONG).show();
			}
			else {	//tie game
				Toast.makeText(getApplicationContext(), "You may or may not have won.", Toast.LENGTH_LONG).show();
			}
			
			SharedPreferences.Editor editor = prefs.edit();
		    editor.putInt(ProfileActivity.PLAYER_WINS, wins);
		    editor.putInt(ProfileActivity.PLAYER_LOSS, loss);
		    editor.commit();
			onBackPressed();
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
		btHandler = new BluetoothHandler(handler);
		
		if (isServer){
			meGo = true;
			btHandler.startServer();
		}
		else {
			meGo = false;
			remoteAddress = intent.getStringExtra(REMOVE_DEVICE_ADDRESS);
			BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteAddress);
			btHandler.startClient(device);
		}
	}
}
