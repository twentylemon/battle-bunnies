package ca.fluffybunny.battlebunnies.game;

/**
 * This denotes a bunny is moving. They can move left or right... le gasp.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-24
 */
public class MoveAction extends Action {
	private static final long serialVersionUID = 1L;
	private boolean left;
	
	
	/**
	 * Initializes this move action.
	 * 
	 * @param playerID the player that should move
	 * @param left which direction the bunny should move
	 */
	public MoveAction(int playerID, boolean left){
		setPlayerID(playerID);
		this.left = left;
	}

	
	/**
	 * Performs this action. Moves the bunny on the game.
	 * @param game the game
	 */
	@Override
	public void execute(GameInfo game){
		game.getBunny(getPlayerID()).moveSideways(left, game.getTerrain());
	}
}
