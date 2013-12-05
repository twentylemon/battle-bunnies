package ca.fluffybunny.battlebunnies.game;

/**
 * This denotes a bunny is firing a weapon and ending their turn.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-24
 */
public class FireAction extends Action {
	private static final long serialVersionUID = 1L;
	private Weapon weapon;
	
	/**
	 * Initializes this fire action.
	 * 
	 * @param playerID the player who is firing a weapon
	 * @param weapon the weapon the player is firing
	 */
	public FireAction(int playerID, Weapon weapon){
		setPlayerID(playerID);
		this.weapon = weapon;
	}

	
	/**
	 * Executes the shot on the game.
	 * @param game the game
	 */
	@Override
	public void execute(GameInfo game){
		game.takeShot(getPlayerID(), game.getFiredWeapon());
	}
}
