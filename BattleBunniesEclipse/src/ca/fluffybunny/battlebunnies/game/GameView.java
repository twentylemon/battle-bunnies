package ca.fluffybunny.battlebunnies.game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;


/**
 * Contains the canvas to draw the game onto.
 * 
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-30
 */
public class GameView extends SurfaceView {

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
