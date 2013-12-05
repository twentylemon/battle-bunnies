package com.fluffybunny.battlebunnies.game;

import java.io.Serializable;

/**
 * Actions are sent back and forth between devices.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-24
 */
public abstract class Action implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int playerID;	//which player this message this belongs to
	
	/**
	 * Getters/Setters.
	 */
	public void setPlayerID(int id){ playerID = id; }
	public int getPlayerID(){ return playerID; }
	
	
	/**
	 * Runs this action on the game.
	 * @param game the game to run this action on...
	 */
	public abstract void execute(GameInfo game);
}

