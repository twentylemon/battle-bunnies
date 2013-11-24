package ca.fluffybunny.battlebunnies.game;

/**
 * This class handles the message passing for one player.
 * 
 * 
 */
public class Player implements Runnable {

    private int playerID;
    private String name;
    private Port port;

    /**
     * Initializes this player with the name and port.
     *
     * @param id the id of this player
     * @param name the name of this player
     * @param port the communication port for this player
     */
    public Player(int id, String name, Port port){
        this.playerID = id;
        this.name = name;
        this.port = port;
    }


    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run(){
        GameInfo game = (GameInfo) port.receive();
        Bunny bunny = game.getBunny(playerID);

        while (!game.isGameOver()){
        	/**
        	 * assuming we're playerID = 0 (order reversed for playerID = 1)
        	 * wait for user input
        	 * on move
        	 * 		send our move to the GameMaster
        	 * 		wait for our message to return
        	 * 		execute our move on our device
        	 * 		continue our turn
        	 * on fire
        	 * 		send our fire action to the GameMaster
        	 * 		wait for our message to return
        	 * 		execute the fire action on our device
        	 * 		our turn ends
        	 * wait for playerID = 1 to take their turn
        	 */
        }
    }
}
