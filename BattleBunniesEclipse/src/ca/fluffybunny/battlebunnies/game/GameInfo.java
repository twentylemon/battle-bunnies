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

	private static final long serialVersionUID = 1L;
    private Terrain terrain;
    private Bunny[] bunnies;
    private int weaponID;				//weapon being fired
    private List<Weapon> weaponList;	//list of all selectable weapons
    private int numShots;   			//number of shots so far (turns = numShots / 2)
    private final static int MAX_TURNS = 10;

    /**
     * Initializes a new game.
     *
     * @param images which images each bunny is using
     * @param names the names of all the players
     * @param terrain the terrain to be used in this game
     */
    public GameInfo(int[] images, String[] names, Terrain terrain){
        numShots = 0;
        weaponID = -1;
        this.terrain = terrain;
        Point[] startLocations = new Point[names.length];
        // TODO dymanicness this stuff?
        startLocations[0] = terrain.getHighestPointAt((int)(0.2 * terrain.getWidth()));
        startLocations[1] = terrain.getHighestPointAt((int)(0.8 * terrain.getWidth()));

        bunnies = new Bunny[2];
        for (int i = 0; i < bunnies.length; i++){
            bunnies[i] = new Bunny(names[i], startLocations[i], images[i]);
        }

        //create the weapon list
        weaponList = new ArrayList<Weapon>();
        weaponList.add(new RockWeapon());
    }


    /**
     * Initializes a new game given a size and terrain generator.
     *
     * @param images which images each bunny is using
     * @param names the names of all the players
     * @param width the width of the terrain
     * @param height the height of the terrain
     * @param generator the terrain generator...
     */
    public GameInfo(int[] images, String[] names, int width, int height, TerrainGenerator generator){
        this(images, names, new Terrain(width, height, generator));
    }


    /**
     * Getters/Setters.
     */
    public Terrain getTerrain(){ return terrain; }
    public int getNumberOfPlayers(){ return bunnies.length; }
    public Bunny getBunny(int id){ return bunnies[id]; }
    public List<Weapon> getWeaponList(){ return weaponList; }
    public Weapon getWeapon(int id){ return weaponList.get(id); }
    public Weapon getFiredWeapon(){ return weaponList.get(weaponID); }
    public int getNumWeapons(){ return weaponList.size(); }
    public boolean isGameOver(){ return getTurnNumber() >= MAX_TURNS; }
    public int getNumShots(){ return numShots; }
    public int getTurnNumber(){ return numShots / 2; }
    public void forceGameEnd(){ numShots = Integer.MAX_VALUE; }
    
    
    /**
     * Fires the weapon on this game.
     * 
     * @param playerID who shot the weapon
     * @param weapon the weapon to fire
     */
    public void takeShot(int playerID, Weapon weapon){
    	numShots++;
    	weaponID = weaponList.indexOf(weapon);
    	// TODO do the stuff
    }
}
