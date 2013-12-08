package com.fluffybunny.battlebunnies.game;


/**
 * Action wrapping a bunny moving.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-12-07
 */
public class MoveAction extends Action {
	private static final long serialVersionUID = 1L;
	
	private boolean left;
	
	/**
	 * Initialized this move action.
	 * 
	 * @param playerID the bunny moving
	 * @param left true if they are moving left, false if right
	 */
	public MoveAction(int playerID, boolean left){
		setPlayerID(playerID);
		this.left = left;
	}
	
	
	/**
	 * Getters/Setters.
	 */
	public boolean isLeft(){ return left; }

	
	/**
	 * Moves the bunny specified.
	 */
	@Override
	public void execute(GameInfo game){
		game.getBunny(playerID).moveSideways(left, game.getTerrain());
	}
}
