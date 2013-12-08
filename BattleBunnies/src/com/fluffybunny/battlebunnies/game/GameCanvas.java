package com.fluffybunny.battlebunnies.game;

import com.fluffybunny.battlebunnies.activities.GameActivityMP;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameCanvas implements Runnable {

	private GameInfo game;					//all the game information
	private SurfaceHolder surfaceHolder;	//where the canvas is
	private Thread thread;					//our canvas thread
	private boolean running;				//continue to update while true
	private double fireTime;				//when the last shot was taken
	private boolean firing;					//if a weapon is in the air
	private ScoreBox scoreBox;				//for displaying the scores
	private Bitmap[] bunnyImages;			//for displaying the bunnies
	private Bitmap terrainImage;			//the terrain image
	private int width, height;				//server's screen size
	private double xRatio, yRatio;			//screne ratios
	private int bitWidth, bitHeight;		//new dimensions for terrain image

	/**
	 * Default constructor.
	 * 
	 * @param game the game info to paint on the screen
	 * @param surfaceHolder where to paint
	 */
	public GameCanvas(GameInfo game, SurfaceHolder surfaceHolder, Resources resources){
		this.game = game;
		this.surfaceHolder = surfaceHolder;
		fireTime = 0;
		firing = false;
		running = false;
		scoreBox = new ScoreBox(game.getTerrain().getWidth(), game.getTerrain().getHeight());
		
		bunnyImages = new Bitmap[game.getNumberOfPlayers()];
		for (int i = 0; i < game.getNumberOfPlayers(); i++){
			bunnyImages[i] = BitmapFactory.decodeResource(resources, game.getBunny(i).getImageResource());
		}
		width = game.getTerrain().getWidth();
		height = game.getTerrain().getHeight();
		bitWidth = 0;
		bitHeight = 0;
		generateTerrainBitmap();
	}

	
	/**
	 * Getters/Setters.
	 */
	public void setFireTime(double time){ fireTime = time; }
	public boolean isFiring(){ return firing; }
	public GameInfo getGameInfo(){ return game; }
	public void setFiring(boolean fire){
		firing = fire;
		if (firing){
			setFireTime(System.currentTimeMillis());
		}
	}
	
    
    /**
     * Changes the size of the bitmap this terrain will produce.
     * 
     * @param width the new width of the bitmap
     * @param height the new height of the bitmap
     */
    public void setBitmapSize(int width, int height){
    	/*
    	bitWidth = width;
    	bitHeight = height;
    	xRatio = (double)bitWidth / (double)width;
    	yRatio = (double)bitHeight / (double)height;
    	generateTerrainBitmap();
    	*/
    }
	
	
	/**
	 * Recreates the terrain bitmap.
	 */
	public void generateTerrainBitmap(){
		Terrain map = game.getTerrain();
    	int[] data = new int[map.getWidth() * map.getHeight()];
    	for (int x = 0; x < map.getWidth(); x++){
    		for (int y = 0; y < map.getHeight(); y++){
    			data[x + y * map.getWidth()] = map.getPoint(x, y);
			}
		}
    	terrainImage = Bitmap.createBitmap(data, map.getWidth(), map.getHeight(), Bitmap.Config.ARGB_8888);
		if (bitWidth != 0 && bitHeight != 0){
			terrainImage = Bitmap.createScaledBitmap(terrainImage, bitWidth, bitHeight, true);
		}
	}
	
	
	/**
	 * Starts the thread.
	 */
	public void start(){
		thread = new Thread(this);
		running = true;
		thread.start();
	}
	
	
	/**
	 * Stops the thread.
	 */
	public void stop(){
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e){}
	}
	
	
	/**
	 * Changes the position to match this screen.
	 * @param p the point to alter
	 */
	private void translate(Point p){
		if (bitWidth != 0 && bitHeight != 0){
			p.x *= xRatio;
			p.y *= yRatio;
		}
	}


	@Override
	public void run(){
		firing = false;
		Canvas canvas = null;
		while (running){
			canvas = surfaceHolder.lockCanvas();
			if (canvas == null){
				try {
					surfaceHolder.unlockCanvasAndPost(canvas);
				} catch (IllegalArgumentException e){}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e){}
				continue;
			}
			
			canvas.drawBitmap(terrainImage, 0, 0, new Paint());
			
			//draw the bunnies
			for (int i = 0; i < game.getNumberOfPlayers(); i++){
				game.getBunny(i).fall(game.getTerrain());		
				Point[] extents = game.getBunny(i).getExtents();
				translate(extents[1]);
				canvas.drawBitmap(bunnyImages[i], extents[1].x, extents[1].y, null);
				scoreBox.draw(canvas, game.getBunny(i).getScore(), i);
			}
			
			//draw the weapon, if any
			if (firing){
				long currentTime = System.currentTimeMillis();
				Point pos = game.getFiredWeapon().getPosition((currentTime - fireTime)/1000.0);
				//check if we went off the screen
				if (pos.x < 0 || pos.x >= game.getTerrain().getWidth()){
					firing = false;
				}
				//check if we hit the terrain or bunnies
				else if (game.getTerrain().getPoint(pos.x, pos.y) != Terrain.AIR ||
						game.getBunny(0).inExtents(pos) || game.getBunny(1).inExtents(pos)){
					game.getFiredWeapon().explode(canvas, pos);
					game.getTerrain().destroyTerrain(pos, game.getFiredWeapon());
					generateTerrainBitmap();
					game.addScore(game.getFireAction(), pos);
					firing = false;
				}
				//else of, just keep flying
				else {
					game.getFiredWeapon().draw(canvas, pos);
				}
			}
			
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}
}
