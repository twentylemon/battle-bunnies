package com.fluffybunny.battlebunnies.activities;

import android.content.Intent;

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
		Intent intent = getIntent();
		
		isServer = intent.getBooleanExtra(IS_SERVER, false);
		
		//server side, we are going to create the game info and send it to the other player
		if (isServer){
			GameInfo g = super.makeGameInfo();
			//send g
			return g;
		}
		//client side, we are going to receive the game info and add in stuff for the canvas etc
		else {
			
		}
		
		return null;
	}
	
	
	/**
	 * Handles pressing of the fire button.
	 */
	@Override
	protected void firePressed(){
	}
}
