
public interface TerrainGenerator {
    /**
     * Returns the terrain that should be used by the map. It is an integer array of pixel colours,
     * such that is (x,y) is 0 (black) then it is not occupied by any terrain, otherwise the colour
     * is displayed.
     * @param width the width of the terrain
     * @param height the height of the terrain
     * @return the terrain
     */
    public int[][] generateTerrain(int width, int height);
}
