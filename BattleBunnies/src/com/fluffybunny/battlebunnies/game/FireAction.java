package com.fluffybunny.battlebunnies.game;

public class FireAction extends Action {
	private static final long serialVersionUID = 1L;

	private double power;
	private double angle;
	private int weaponID;
	
	/**
	 * Initializes this fire action.
	 * 
	 * @param playerID the player who is firing a weapon
	 * @param weapon the weapon the player is firing
	 */
	public FireAction(int playerID, double power, double angle, int weaponID){
		setPlayerID(playerID);
		this.power = power;
		this.angle = angle;
		this.weaponID = weaponID;
	}

	/**
	 * Getters/Setters
	 */
	public double getPower(){ return power; }
	public double getAngle(){ return angle; }
	public int getWeaponID(){ return weaponID; }
	
	
	/**
	 * Executes this action on the game.
	 * 
	 * @param game the game to run the action on
	 */
	@Override
	public void execute(GameInfo game){
		game.takeShot(this);
	}
}
