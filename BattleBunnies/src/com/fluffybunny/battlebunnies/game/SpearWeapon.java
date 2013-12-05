package com.fluffybunny.battlebunnies.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;

import com.fluffybunny.battlebunnies.util.Point;

public class SpearWeapon extends Weapon {
	private static final long serialVersionUID = 1L;

	private final float RADIUS = 10;
	private final Paint PAINT = new Paint();
	Bitmap bitmap ;

	public SpearWeapon(){
		name ="Chuck a Spear";
		PAINT.setColor(Color.parseColor("#C0C0C0"));
		scoreValue = 100;
		
		//bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.spear);
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
	

	@Override
	public void draw(Canvas canvas, Point where) {
		canvas.drawCircle(where.x, where.y, RADIUS, PAINT);

	}


	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

}
