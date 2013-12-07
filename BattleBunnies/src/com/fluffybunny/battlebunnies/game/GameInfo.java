package com.fluffybunny.battlebunnies.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Terrain terrain;
	private Bunny[] bunnies;
    private int weaponID;				//weapon being fired
    private List<Weapon> weaponList;	//list of all selectable weapons
    private int numShots;   			//number of shots so far (turns = numShots / 2)
    private int myID;					//this player's player id
    private FireAction fireAction;		//the most recent shot taken
	
	public static final int MAX_TURNS = 10;

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
        weaponList.add(new SpearWeapon());
        weaponList.add(new GrenadeWeapon());
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
    public GameInfo(int[] images, String[] names, int width, int height, Terrain.Generator generator){
        this(images, names, new Terrain(width, height, generator));
    }


    /**
     * Getters/Setters.
     */
    public void setID(int id){ myID = id; }
    public int getMyID(){ return myID; }
    public Terrain getTerrain(){ return terrain; }
    public int getNumberOfPlayers(){ return bunnies.length; }
    public Bunny getBunny(int id){ return bunnies[id]; }
    public List<Weapon> getWeaponList(){ return weaponList; }
    public Weapon getWeapon(int id){ return weaponList.get(id); }
    public Weapon getFiredWeapon(){ return weaponList.get(weaponID); }
    public FireAction getFireAction(){ return fireAction; }
    public int getNumWeapons(){ return weaponList.size(); }
    public boolean isGameOver(){ return getTurnNumber() >= MAX_TURNS; }
    public int getNumShots(){ return numShots; }
    public int getTurnNumber(){ return numShots / 2; }
    public void forceGameEnd(){ numShots = Integer.MAX_VALUE; }
    
    
    /**
     * Fires the weapon on this game.
     * 
     * @param fire the fire action
     */
    public void takeShot(FireAction fire){
    	fireAction = fire;
    	numShots++;
    	int playerID = fire.getPlayerID();
    	weaponID = fire.getWeaponID();
    	getBunny(playerID).fireWeapon(fire.getPower(), fire.getAngle(), getWeapon(weaponID));
    }
    
    
    /**
     * Calculates how many points to give to the player.
     * 
     * @param fire the action performed
     * @param landing where the shot landed and exploded
     */
    public void addScore(FireAction fire, Point landing){
    	int playerID = fire.getPlayerID();
    	weaponID = fire.getWeaponID();
    	int score = (int)getWeapon(weaponID).getScoreValue();
    	double radius = getWeapon(weaponID).getExplosionRadius();
    	radius = radius * radius;
    	
    	for (int i = 0; i < getNumberOfPlayers(); i++){
    		Point[] extents = getBunny(i).getExtents();
    		for (Point extent : extents){
        		if (extent.distance(landing) < radius){
        			if (playerID == i){
        				getBunny(playerID).addScore(-score);
        			}
        			else {
        				getBunny(playerID).addScore(score);
        			}
        		}
    		}
    	}
    }
}
