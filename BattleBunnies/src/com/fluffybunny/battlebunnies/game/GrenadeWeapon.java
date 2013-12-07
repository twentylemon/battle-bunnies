package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;

public class GrenadeWeapon extends Weapon {

	private static final long serialVersionUID = 1L;

	private float RADIUS = 5;
	private float BOOMRADIUS = 50;
	private Paint PAINT = new Paint();
	private Paint BOOMPAINT = new Paint();

	public GrenadeWeapon(){
		name = "Toss a 'Nade";
		explosionRadius = BOOMRADIUS;
		PAINT.setColor(Color.parseColor("#EEC900"));
		BOOMPAINT.setColor(Color.parseColor("#CD0000"));
		scoreValue = 25;
	}


	@Override
	public void explode(Canvas canvas, Point where){
		canvas.drawCircle(where.x, where.y, BOOMRADIUS, BOOMPAINT);
	}

	
	/**
	 * Draws this weapon on the canvas provided. This method should not be used, as
	 * { @code draw(Canvas, Point) } animates the weapon.
	 * 
	 * @param canvas where to draw
	 */
	@Override
	public void draw(Canvas canvas){
		draw(canvas, fireLocation);
	}

	
	/**
	 * Draws this weapon on the canvas provided.
	 * 
	 * @param canvas where to draw
	 * @param where where the weapon currently is { @see getPosition() }
	 */
	@Override
	public void draw(Canvas canvas, Point where){
		canvas.drawCircle(where.x, where.y, RADIUS, PAINT);
	}

	@Override
	public int getOpacity(){
		return 0;
	}

	@Override
	public void setAlpha(int alpha){
	}

	@Override
	public void setColorFilter(ColorFilter cf){
	}

}
