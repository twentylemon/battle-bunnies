package ca.fluffybunny.battlebunnies.game;

import java.io.Serializable;

/**
 * Initial game information for each player to receive. Contains information like
 * the player's names and the board and such.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public class StartInfo implements Serializable {

    private int playerID;   //which player "we" are
    private String[] names; //the names of all players in the game

}
