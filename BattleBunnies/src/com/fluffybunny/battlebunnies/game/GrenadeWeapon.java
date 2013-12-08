package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GrenadeWeapon extends Weapon {
	private static final long serialVersionUID = 1L;

	private float RADIUS = 5;
	private float BOOMRADIUS = 50;

	public GrenadeWeapon(){
		name = "Toss a 'Nade";
		explosionRadius = BOOMRADIUS;
		scoreValue = 25;
	}


	@Override
	public void explode(Canvas canvas, Point where){
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#CD0000"));
		canvas.drawCircle(where.x, where.y, BOOMRADIUS, paint);
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
		paint.setColor(Color.parseColor("#EEC900"));
		canvas.drawCircle(where.x, where.y, RADIUS, paint);
	}
}
