package com.fluffybunny.battlebunnies.activities;

import android.content.Intent;

import com.fluffybunny.battlebunnies.game.GameCanvas;
import com.fluffybunny.battlebunnies.game.GameInfo;

/**
 * The multiplayer game screen
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-12-07
 */
public class GameActivityMP extends GameActivitySP {
	
	public static final String IS_SERVER = "isServer";

	private boolean isServer;
	
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
			//send g
			return g;
		}
		//client side, we are going to receive the game info and add in stuff for the canvas etc
		else {
			//receive g
			GameInfo g = null;
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
			}
			else {
			}
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
	}
}
