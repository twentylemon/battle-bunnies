package ca.fluffybunny.battlebunnies.game;

import java.util.Random;

/**
 * The AI player. Le gasp.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-24
 */
public class AIPlayer extends Player {

	private Random rand;

	/**
	 * Default awesomeness constructor.
	 * 
	 * @param id our playerID, since we're AI, should always be 1
	 * @param name our name
	 * @param port our communication port (a Channel)
	 */
	public AIPlayer(int id, String name, Port port) {
		super(id, name, port);
		rand = new Random();
	}


	/**
	 * Calculates the perfect shot for a random weapon.
	 * @return the FireAction of the shot
	 */
	public FireAction getShot(){
		//get a random weapon
		Weapon weapon = game.getWeaponList().get(rand.nextInt(game.getNumWeapons()));
		game.getBunny(playerID).fireWeapon(50 + rand.nextInt(50), 90 + rand.nextInt(60), weapon);
		return new FireAction(playerID, weapon);
	}


    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run(){
        game = (GameInfo) port.receive();	//initialize our game info

        while (!game.isGameOver()){
        	Action humanAction = null;
        	//loop until we receive a fire action
        	while (!(humanAction instanceof FireAction)){
        		humanAction = (Action) port.receive();	//block for the human action
        		humanAction.execute(game);				//execute the action
        	}
        	
        	Action myAction = getShot();		//get the shot we are going to take
        	port.send(myAction);				//send our shot to the game master
        	myAction = (Action) port.receive();	//get our action back
        	myAction.execute(game);				//take our shot
        }
    }
}
