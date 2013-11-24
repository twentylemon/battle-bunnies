package ca.fluffybunny.battlebunnies.game;

/**
 * Created by Taras on 24/11/13.
 */
public class Player implements Runnable {

    private String name;
    private Port port;

    /**
     * Initializes this player with the name and port.
     *
     * @param name the name of this player
     * @param port the communication port for this player
     */
    public Player(String name, Port port){
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
    }
}
