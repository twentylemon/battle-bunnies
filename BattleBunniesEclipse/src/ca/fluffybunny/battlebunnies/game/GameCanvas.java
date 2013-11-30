package ca.fluffybunny.battlebunnies.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import ca.fluffybunny.battebunnies.activities.MainActivity;

public class GameCanvas implements Runnable {
	
	private GameInfo game;
	private SurfaceHolder surfaceHolder;
	private Thread thread;
	private boolean running;

	/**
	 * Default constructor.
	 * 
	 * @param game the game info to paint on the screen
	 * @param surfaceHolder where to paint
	 */
	public GameCanvas(GameInfo game, SurfaceHolder surfaceHolder){
		this.game = game;
		this.surfaceHolder = surfaceHolder;
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
			try {
				Canvas canvas = surfaceHolder.lockCanvas();
				Terrain terrain = game.getTerrain();
				int[] data = new int[terrain.getWidth() * terrain.getHeight()];
				for (int x = 0; x < terrain.getWidth(); x++){
					for (int y = 0; y < terrain.getHeight(); y++){
						data[x + y * terrain.getWidth()] = terrain.getPoint(x, y);
					}
				}
				Bitmap map = Bitmap.createBitmap(data, terrain.getWidth(), terrain.getHeight(), Bitmap.Config.ARGB_8888);
				canvas.drawBitmap(map, 0, 0, new Paint());
				
				for (int i = 0; i < game.getNumberOfPlayers(); i++){
					game.getBunny(i).draw(canvas);
				}
				
				surfaceHolder.unlockCanvasAndPost(canvas);
			} catch (NullPointerException e){}
		}
	}
}
