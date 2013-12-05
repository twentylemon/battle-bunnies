package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;

import com.fluffybunny.battlebunnies.util.Point;

public class RockWeapon extends Weapon {
	private static final long serialVersionUID = 1L;

	private final float RADIUS = 10;
	private final Paint PAINT = new Paint();

	public RockWeapon(){
		name = "Throw a Rock";
		PAINT.setColor(Color.parseColor("#C0C0C0"));
		scoreValue = 50;
	}


	@Override
	public void explode(Canvas canvas, Point where){
		draw(canvas, where);
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
