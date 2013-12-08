package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RockWeapon extends Weapon {
	private static final long serialVersionUID = 1L;

	private final float RADIUS = 10;

	public RockWeapon(){
		explosionRadius = RADIUS;
		name = "Throw a Rock";
		scoreValue = 50;
	}


	@Override
	public void explode(Canvas canvas, Point where){
		draw(canvas, where);
	}

	
	/**
	 * Draws this weapon on the canvas provided.
	 * 
	 * @param canvas where to draw
	 * @param where where the weapon currently is { @see getPosition() }
	 */
	@Override
	public void draw(Canvas canvas, Point where){
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#C0C0C0"));
		canvas.drawCircle(where.x, where.y, RADIUS, paint);
	}
}
