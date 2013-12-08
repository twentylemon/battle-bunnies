package com.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SpearWeapon extends Weapon {
	private static final long serialVersionUID = 1L;

	private final float RADIUS = 3;

	public SpearWeapon(){
		name ="Chuck a Spear";
		scoreValue = 100;
		explosionRadius = 3;
	}


	@Override
	public void explode(Canvas canvas, Point where){
		draw(canvas, where);
	}
	

	@Override
	public void draw(Canvas canvas, Point where){
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#8B4513"));
		canvas.drawCircle(where.x, where.y, RADIUS, paint);
	}
}
