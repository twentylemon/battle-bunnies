package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

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
			canvas.drawBitmap(game.getTerrain().getBitmap(), 0, 0, new Paint());
			
			//draw the bunnies
			for (int i = 0; i < game.getNumberOfPlayers(); i++){
				game.getBunny(i).draw(canvas);
			}
			
			//draw the weapon, if any
			if (firing){
				
			}
			
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}
}
