package ca.fluffybunny.battlebunnies.game;

import java.io.IOException;

import android.util.Log;

/**
 * The game server. All messages get sent to the GameMaster, then the GameMaster sends those
 * messages out to all other players. Then each player executes the action on their own device.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-24
 */
public class GameMaster implements Runnable {
	private final String TAG = "Game";
	
	private StartInfo info;
	private static int GAME_WIDTH = 500;
	private static int GAME_HEIGHT = 250;
	
	/**
	 * Default constructor.
	 * 
	 * @param info all the info needed to start a game
	 */
	public GameMaster(StartInfo info){
		this.info = info;
	}

	
	/**
	 * Getters/Setters.
	 */
	public static void setGameWidth(int width){ GAME_WIDTH = width; }
	public static void setGameHeight(int height){ GAME_HEIGHT = height; }
	public static int getGameWidth(){ return GAME_WIDTH; }
	public static int getGameHeight(){ return GAME_HEIGHT; }
	
	
	/**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
	 */
	@Override
	public void run(){
		GameInfo game = new GameInfo(info.getImages(), info.getNames(), GAME_WIDTH, GAME_HEIGHT, info.getTerrainGenerator());
		
		//send the game info to each player
		for (int i = 0; i < info.getNumberOfPlayers(); i++){
			Log.e("tag", "send player " + i + " the game info");
			info.getPort(i).send(game);
		}
		
		//play the game now
		while (!game.isGameOver()){
			/**
			 * wait for the players to move or fire
			 * on move
			 * 		send move message to each player
			 * 		execute the move on our game
			 * 		each player executes the move on their own game
			 * 		continue this player's turn
			 * on fire
			 * 		send fire message to each player
			 * 		execute the fire on our game
			 * 		each player executes the fire on their own game
			 * 		this player's turn ends
			 * do player 2's turn (same thing again)
			 * after GameInfo.MAX_TURNS turns, stop
			 */
			
			//for each player, get their actions and send them to each player
			for (int playerID = 0; playerID < game.getNumberOfPlayers(); playerID++){
				Action action = null;
				while (!(action instanceof FireAction)){
					action = (Action) info.getPort(playerID).receive();
					for (int i = 0; i < game.getNumberOfPlayers(); i++){
						info.getPort(i).send(action);	//send the action to each player
					}
					action.execute(game);	//execute the action on our game
				}
			}
		}

		for (int i = 0; i < info.getNumberOfPlayers(); i++){
			try {
				info.getPort(i).close();
			} catch (IOException e){}
		}
	}
}
