package ca.fluffybunny.battlebunnies.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameCanvas implements Runnable {
	
	private GameInfo game;
	private Canvas canvas;
	private Thread thread;
	private boolean running;

	/**
	 * Default constructor.
	 * 
	 * @param game the game info to paint on the screen
	 * @param canvas where to paint
	 */
	public GameCanvas(GameInfo game, Canvas canvas){
		this.game = game;
		this.canvas = canvas;
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
	
	
	@Override
	public void run(){
		long lastLoopTime = System.currentTimeMillis();
		long currentTime, delta;
		while (running){
			currentTime = System.currentTimeMillis();
			delta = currentTime - lastLoopTime;
			lastLoopTime = currentTime;
			if (delta < 10){
				try {	//block the thread for a bit
					Thread.sleep(10 - delta);
				} catch (InterruptedException e){}
			}
			/**
			 * update the canvas using the GameInfo
			 */
			Terrain terrain = game.getTerrain();
			int[] data = new int[terrain.getWidth() * terrain.getHeight()];
			for (int x = 0; x < terrain.getWidth(); x++){
				for (int y = 0; y < terrain.getHeight(); y++){
					data[x + y * terrain.getWidth()] = terrain.getPoint(x, y);
				}
			}
			Bitmap map = Bitmap.createBitmap(data, terrain.getWidth(), terrain.getHeight(), Bitmap.Config.ARGB_8888);
			canvas.drawBitmap(map, 0, 0, null);
		}
	}
}
