package ca.fluffybunny.battlebunnies.game;

/**
 * Created by Taras on 24/11/13.
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
            //wait for user input
            //mark the weapon we want to fire, send that info to GameMaster
            //GameMaster send the weapon info to all
            //each player runs the weapon firing on their own device
            //block for next player's turn
        }
    }
}
