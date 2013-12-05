package com.fluffybunny.battlebunnies.game;

/**
 * Interface for terrain generation. Only one real method, which is to generate the map.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public interface TerrainGenerator {
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
    public int[][] generateTerrain(int width, int height);
}
