package ca.fluffybunny.battlebunnies.game;

/**
 * Contains all the information for all of the players. Used to initialize the GameMaster.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-24
 */
public class StartInfo {

	private int[] images;
	private String[] names;
	private Port[] ports;
	private TerrainGenerator generator;
	
	/**
	 * Default constructor.
	 * 
     * @param images which images each bunny is using
	 * @param names the names of all the players
	 * @param ports how to communicate with all the players
	 * @param generator the terrain generator to use
	 */
	public StartInfo(int[] images, String[] names, Port[] ports, TerrainGenerator generator){
		this.images = images;
		this.names = names;
		this.ports = ports;
		this.generator = generator;
	}
	
	
	/**
	 * Getters/Setters.
	 */
	public int getNumberOfPlayers(){ return names.length; }
	public int[] getImages(){ return images; }
	public String[] getNames(){ return names; }
	public String getName(int id){ return names[id]; }
	public Port[] getPorts(){ return ports; }
	public Port getPort(int id){ return ports[id]; }
	public TerrainGenerator getTerrainGenerator(){ return generator; }
}
