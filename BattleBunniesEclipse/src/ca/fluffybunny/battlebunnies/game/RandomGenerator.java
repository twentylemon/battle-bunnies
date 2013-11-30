package ca.fluffybunny.battlebunnies.game;

import java.io.Serializable;
import java.util.Random;

/**
 * Returns a random terrain.
 *
 * @author Lachlan Plant
 * @version 1.0
 * @since 2013-11-23
 */
public class RandomGenerator implements TerrainGenerator, Serializable {
	private static final long serialVersionUID = 1L;
    /**
     * Returns the terrain that should be used by the map. It is an integer array of pixel colours,
     * such that is (x,y) is 0 (black) then it is not occupied by any terrain, otherwise the colour
     * is displayed. The terrain will be in the form { new int[width][height] }.
     * (0,0) is the bottom left corner.
     *
     * @param width  the width of the terrain
     * @param height the height of the terrain
     * @return the terrain
     */
    @Override
    public int[][] generateTerrain(int width, int height) {
        int[][] terrain = new int[width][height];
        Random rng = new Random();
        topline(terrain, width, height);
        smooth(terrain, rng.nextInt(width / 10));
        fillLower(terrain, Terrain.GRASS, Terrain.ROCK, Terrain.AIR);
        return terrain;
    }


    /**
     * Takes an empty array and first chooses the type(peak or valley) and
     * balance point for that type. the creates a new top line with a flatter
     * area where the bunnies start.
     *
     * @param c an empty int[][]
     * @param width the width of the terrain
     * @param height the height of the terrain
     */
    public void topline(int[][] c, int width, int height){
        int prev;
        Random rng = new Random();

        boolean b = rng.nextBoolean();
        int scale = width/4;
        int modifier = rng.nextInt(scale);
        if (!b){
            scale *= -1;
        }
        int central = (width/2) + modifier;

        boolean peak = rng.nextBoolean();
        int type = 1;
        if (!peak){
            type *= -1;
        }
        if (peak){
            prev = height/4;
        }
        else {
            prev = height - height/4;
        }

        int p1w = rng.nextInt(width/20) + 20;
        int p2w = rng.nextInt(width/20) + 20;

        int p1cent = width/5;
        int p2cent = width - width/5;

        for (int i = 0; i < c.length; i++){
            if (i == central){
                type *= -1;
                c[i][prev] = 1;
            }
            else if ((i > p1cent-p1w && i < p1cent+p1w) || (i > p2cent-p2w && i < p2cent+p2w)){
                int next = rng.nextInt(3);
                int bias = rng.nextInt(10);
                if (bias <= 4){
                    next *= -1;
                }
                next *= type;
                if (prev+next < height-(height/10) && prev+next > height/10){
                    prev += next;
                }
                c[i][prev] = 1;
            }
            else {
                int next = rng.nextInt(3);
                int bias = rng.nextInt(25);
                if (bias <= 3){
                    next *= -1;
                }
                next *= type;
                if (prev+next < height-(height/10) && prev+next > height/10){
                    prev += next;
                }
                c[i][prev] = 1;
            }
        }
    }


    /**
     * Takes in a generated  terrain array and averages the top line over
     * a fact values to the left and right of it, creating a smoother line.
     *
     * @param c the terrain array
     * @param fact the amount of coordinates on either side to be averaged
     */
    public void smooth(int[][] c, int fact){
        int count, total;
        for (int i = 0; i < c.length; i++){
            int temp = i - fact;
            count = 0;
            total = 0;
            for (int j = 0; j <= 2*fact; j++){
                if (temp >= 0 && temp < c.length){
                    total += getVal(c, temp);
                    count++;
                }
                temp++;
            }

            total /= count;
            int t = getVal(c, i);
            c[i][t] = 0;

            if (total < c[i].length){
                c[i][total] = 1;
            }
            else {
                c[i][t] = 1;
            }
        }
    }


    /**
     * Returns the y coord where c is 1.
     *
     * @param c the terrain to search
     * @param loc the x coord to find c = 1
     * @return the y index in c
     */
    public int getVal(int[][] c, int loc){
        for (int i = 0; i < c[loc].length; i++){
            if(c[loc][i] == 1){
                return i;
            }
        }
        //should be return -1?
        return 0;
    }


    /**
     * Takes a terrain array and finds the top line fills the soil and grass below it.
     *
     * @param c the unfilled terrain array
     * @param top represents the color of the top grass layer
     * @param soil represents the color of the under soil
     * @param sky represents the color of the sky
     */
    public void fillLower(int[][] c, int top, int soil,int sky){
        boolean lower = false;
        for (int i = 0; i < c.length; i++){
            lower = false;
            int grassDepth = 30;
            for(int j = 0; j < c[i].length; j++){
                if (c[i][j] != 0){
                    lower = true;
                    c[i][j] = 1;
                }
                if (lower && grassDepth > 0){
                    grassDepth--;
                    c[i][j] = top;

                }
                if (lower && grassDepth <= 0){
                    c[i][j] = soil;
                }
                if (!lower){
                    c[i][j] = sky;
                }
            }
        }
    }
}
