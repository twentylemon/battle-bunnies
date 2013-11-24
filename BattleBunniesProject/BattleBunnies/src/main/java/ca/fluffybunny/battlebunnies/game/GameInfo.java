package ca.fluffybunny.battlebunnies.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.fluffybunny.battlebunnies.util.Point;

/**
 * All of the game information. This is to be passed over Bluetooth or whatever to each client.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public class GameInfo implements Serializable {

    private Terrain terrain;
    private Bunny[] bunnies;
    private List<Weapon> weaponList;
    private boolean gameOver;

    /**
     * Initializes a new game.
     *
     * @param names the names of all the players
     * @param terrain the terrain to be used in this game
     */
    public GameInfo(String[] names, Terrain terrain){
        gameOver = false;
        this.terrain = terrain;
        Point[] startLocations = new Point[names.length];
        // TODO dymanicness this stuff?
        startLocations[0] = terrain.getHighestPointAt((int)(0.2 * terrain.getWidth()));
        startLocations[1] = terrain.getHighestPointAt((int)(0.8 * terrain.getWidth()));

        bunnies = new Bunny[2];
        for (int i = 0; i < bunnies.length; i++){
            bunnies[i] = new Bunny(names[i], startLocations[i]);
        }

        //create the weapon list
        weaponList = new ArrayList<Weapon>();
    }


    /**
     * Initializes a new game given a size and terrain generator.
     *
     * @param names the names of all the players
     * @param width the width of the terrain
     * @param height the height of the terrain
     * @param generator the terrain generator...
     */
    public GameInfo(String[] names, int width, int height, TerrainGenerator generator){
        this(names, new Terrain(width, height, generator));
    }


    /**
     * Getters/Setters.
     */
    public void setGameOver(boolean over){ gameOver = over; }
    public Terrain getTerrain(){ return terrain; }
    public int getNumberOfPlayers(){ return bunnies.length; }
    public Bunny getBunny(int id){ return bunnies[id]; }
    public List<Weapon> getWeaponList(){ return weaponList; }
    public Weapon getWeapon(int id){ return weaponList.get(id); }
    public boolean isGameOver(){ return gameOver; }
}
