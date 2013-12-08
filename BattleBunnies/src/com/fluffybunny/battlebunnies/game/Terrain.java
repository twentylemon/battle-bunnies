package com.fluffybunny.battlebunnies.game;

import java.io.Serializable;

import android.graphics.Color;

/**
 * The game terrain map.
 *
 * @author Taras Mychaskiw 
 * @version 1.0
 * @since 2013-11-23 
 */
public class Terrain implements Serializable {
	private static final long serialVersionUID = 1L;

    private int[][] map;    //the terrain map

    public final static int AIR = Color.parseColor("#E0FEFF");
    public final static int GRASS = Color.parseColor("#6E8B3D");
    public final static int ROCK = Color.parseColor("#8B6508");

    /**
     * Initializes the terrain.
     * 
     * @param width the width of the map
     * @param height the height of the map
     * @param generator tool to generate the terrain
     */
    public Terrain(int width, int height, Terrain.Generator generator){
        map = generator.generate(width, height);
    }


    /**
     * Getters/Setters.
     */
    public void destroyPoint(int x, int y){ map[x][y] = AIR; }
    public boolean isOnMap(int x, int y){ return x >= 0 && x < getWidth() && y >= 0 && y < getHeight(); }
    public int getWidth(){ return map.length; }
    public int getHeight(){ return map[0].length; }
    public int getPoint(int x, int y){
    	if (y >= getHeight()){
    		return ROCK;
    	}
    	else if (y < 0){
    		return AIR;
    	}
    	return map[x][y];
    }


    /**
     * Returns the highest point of terrain at some x coordinate.
     * 
     * @param x the x coordinate to find the highest point at
     * @return the highest Point on the x column
     */
    public Point getHighestPointAt(int x){
        for (int j = 0; j < map[x].length; j++){
            if (map[x][j] != AIR){
                return new Point(x, j);
            }
        }
        return new Point(x, map[x].length);
    }


    /**
     * Destroys all the terrain that the weapon's explosion will hit.
     * 
     * @param center the point where the weapon lands, center of it's explosion
     * @param weapon the weapon exploding
     */
    public void destroyTerrain(Point center, Weapon weapon){
        double radius = weapon.getExplosionRadius();
        int xmin = (int)(center.x - radius), xmax = (int)(center.x + radius);
        int ymin = (int)(center.y - radius), ymax = (int)(center.y + radius);

        radius = radius * radius;   //square for use below
        for (int i = xmin; i <= xmax; i++){
            for (int j = ymin; j <= ymax; j++){
            	if (isOnMap(i, j)){
	                double dist = (i - center.x)*(i - center.x) + (j - center.y)*(j - center.y);
	                if (dist < radius){ //the point (i,j) is in the circle
	                    destroyPoint(i, j);
	                }
            	}
            }
        }
    }

	
	public interface Generator extends Serializable {
	    /**
	     * Returns the terrain that should be used by the map. It is an integer array of pixel colours,
	     * such that is (x,y) is Terrain.AIR then it is not occupied by any terrain, otherwise the colour
	     * is displayed. The terrain will be in the form { new int[width][height] }.
	     * (0,0) is the top left corner.
	     *
	     * @param width the width of the terrain
	     * @param height the height of the terrain
	     * @return the terrain
	     */
	    public int[][] generate(int width, int height);
	    
	    public long getSeed();
	    public void setSeed(long seed);
	}
}
