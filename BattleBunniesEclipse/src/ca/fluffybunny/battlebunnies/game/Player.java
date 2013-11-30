package ca.fluffybunny.battlebunnies.game;

import android.util.Log;
import android.view.SurfaceHolder;

/**
 * This class handles the message passing for one player.
 * 
 * 
 */
public class Player implements Runnable {

    protected int playerID;
    protected String name;
    protected Port port;
    protected GameInfo game;
    protected SurfaceHolder surfaceHolder;
    protected GameCanvas gameCanvas;

    /**
     * Initializes this player with the name and port.
     *
     * @param id the id of this player
     * @param name the name of this player
     * @param port the communication port for this player
     * @param holder the surface that has the canvas to draw to
     */
    public Player(int id, String name, Port port, SurfaceHolder holder){
        this.playerID = id;
        this.name = name;
        this.port = port;
        surfaceHolder = holder;
    }

    
    /**
     * Stops this runnable.
     */
    public void stop(){
    	game.forceGameEnd();
    	gameCanvas.stop();
    }


    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run(){
        game = (GameInfo) port.receive();
        
        gameCanvas = new GameCanvas(game, surfaceHolder);
        gameCanvas.start();

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
        
        stop();
    }
}
