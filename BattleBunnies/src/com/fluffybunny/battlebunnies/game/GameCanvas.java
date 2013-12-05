package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import com.fluffybunny.battlebunnies.util.Point;

public class GameCanvas implements Runnable {
	
	private GameInfo game;
	private SurfaceHolder surfaceHolder;
	private Thread thread;
	private boolean running;
	private double fireTime;
	private boolean firing;

	/**
	 * Default constructor.
	 * 
	 * @param game the game info to paint on the screen
	 * @param surfaceHolder where to paint
	 */
	public GameCanvas(GameInfo game, SurfaceHolder surfaceHolder){
		this.game = game;
		this.surfaceHolder = surfaceHolder;
		fireTime = 0;
		firing = false;
		running = false;
	}

	
	/**
	 * Getters/Setters.
	 */
	public void setFireTime(double time){ fireTime = time; }
	public boolean isFiring(){ return firing; }
	public void setFiring(boolean fire){
		firing = fire;
		if (firing){
			setFireTime(System.currentTimeMillis());
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


	@Override
	public void run(){
		firing = false;
		Canvas canvas = null;
		while (running){
			canvas = surfaceHolder.lockCanvas();
			if (canvas == null){
				Log.e("tag", "canvas is null");
				try {
					surfaceHolder.unlockCanvasAndPost(canvas);
				} catch (IllegalArgumentException e){}
				try {
					Thread.sleep(1000);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
					continue;
				}
			}
			canvas.drawBitmap(game.getTerrain().getBitmap(), 0, 0, new Paint());
			
			//draw the bunnies
			for (int i = 0; i < game.getNumberOfPlayers(); i++){
				game.getBunny(i).draw(canvas);
			}
			
			//draw the weapon, if any
			if (firing){
				long currentTime = System.currentTimeMillis();
				Point pos = game.getFiredWeapon().getPosition((currentTime - fireTime)/1000.0);
				//check if we went off the screen
				if (pos.x < 0 || pos.x >= game.getTerrain().getWidth()){
					firing = false;
				}
				//check if we hit the terrain
				else if (game.getTerrain().getPoint(pos.x, pos.y) != Terrain.AIR){
					game.getFiredWeapon().explode(canvas, pos);
					game.getTerrain().destroyTerrain(pos, game.getFiredWeapon());
					firing = false;
				}
				// TODO check if we hit bunnies
				//else of, just keep flying
				else {
					game.getFiredWeapon().draw(canvas, pos);
				}
			}
			
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}
}
