package com.fluffybunny.battlebunnies.game;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.fluffybunny.battlebunnies.util.Point;

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
    private Bitmap bitmap;	//the game map image

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
    public Terrain(int width, int height, TerrainGenerator generator){
        map = generator.generateTerrain(width, height);
        generateBitmap();
    }


    /**
     * Getters/Setters.
     */
    public void destroyPoint(int x, int y){ map[x][y] = AIR; }
    public Bitmap getBitmap(){ return bitmap; }
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
     * Regenerates the bitmap for this terrain.
     */
    private void generateBitmap(){
    	int[] data = new int[getWidth() * getHeight()];
    	for (int x = 0; x < getWidth(); x++){
    		for (int y = 0; y < getHeight(); y++){
    			data[x + y * getWidth()] = getPoint(x, y);
			}
		}
		bitmap = Bitmap.createBitmap(data, getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
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
                double dist = (i - center.x)*(i - center.x) + (j - center.y)*(j - center.y);
                if (dist < radius){ //the point (i,j) is in the circle
                    destroyPoint(i, j);
                }
            }
        }
        generateBitmap();
    }
}