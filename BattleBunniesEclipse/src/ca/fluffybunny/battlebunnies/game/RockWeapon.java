package ca.fluffybunny.battlebunnies.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import ca.fluffybunny.battlebunnies.util.Point;

public class RockWeapon extends Weapon {
	private static final long serialVersionUID = 1L;

	private final float RADIUS = 10;
	private final Paint PAINT = new Paint();

	public RockWeapon(String name){
		super(name);
		PAINT.setColor(Color.parseColor("#C0C0C0"));
	}


	@Override
	public void onExplode(){
	}

	
	/**
	 * Draws this weapon on the canvas provided. This method should not be used, as
	 * { @code draw(Canvas, double) } animates the weapon.
	 * 
	 * @param canvas where to draw
	 */
	@Override
	public void draw(Canvas canvas){
		draw(canvas, 0);
	}

	
	/**
	 * Draws this weapon on the canvas provided.
	 * 
	 * @param canvas where to draw
	 * @param time how much time has passed in the flight
	 */
	@Override
	public void draw(Canvas canvas, double time){
		Point position = getPosition(time);
		canvas.drawCircle(position.x, position.y, RADIUS, PAINT);
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
