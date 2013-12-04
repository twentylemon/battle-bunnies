package ca.fluffybunny.battlebunnies.game;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import ca.fluffybunny.battlebunnies.util.Point;

/**
 * The player.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public class Bunny extends Drawable implements Serializable {

	private static final long serialVersionUID = 1L;
    private Point position;     //where we are located on the map
    private float radius;		//how big the bunny is
    private int movesRemaining; //how many times we can move
    private int score;          //how many points we have
    private String name;    	//our display name
    private Point[] extents;    //how big on the map the bunny is (for collision detection)
    private int resImage;

    public final static int DEFAULT_NUM_MOVES = 4;
    public final static int MOVE_SPEED = 50;
    public final static int RADIUS = 30;

    /**
     * Initializes the bunny.
     *
     * @param name the name to display on the screen
     * @param score how many points the bunny has
     * @param moves how many moves the bunny has remaining
     * @param loc where the bunny starts
     * @param radius the size of the bunny (for extents)
     * @param image the image resource that this bunny should use to draw
     */
    public Bunny(String name, int score, int moves, Point loc, int radius, int image){
        this.name = name;
        this.score = score;
        this.movesRemaining = moves;
        this.position = loc;
        this.radius = radius;
        this.resImage = image;
        extents = new Point[2];
        extents[0] = position.add(new Point(-radius, -radius));
        extents[1] = position.add(new Point(radius, radius));
    }


    /**
     * Initializes the bunny with 0 score, the default number of moves and default size.
     *
     * @param name the name to display on the screen
     * @param loc where the bunny starts
     * @param image the image resource that this bunny should use to draw
     */
    public Bunny(String name, Point loc, int image){
        this(name, 0, DEFAULT_NUM_MOVES, loc, RADIUS, image);
    }


    /**
     * Getters/Setters.
     */
    public void setPosition(Point position){ this.position = position; }
    public void setMovesRemaining(int movesRemaining){ this.movesRemaining = movesRemaining; }
    public void setScore(int score){ this.score = score; }
    public void setName(String name){ this.name = name; }
    public void setExtents(Point[] extents){ this.extents = extents; }
    public int getMovesRemaining(){ return movesRemaining; }
    public int getScore(){ return score; }
    public Point getPosition(){ return position; }
    public String getName(){ return name; }


    /**
     * Sets the extents of the bunny.
     *
     * @param p1 the minX, minY point
     * @param p2 the maxX, maxY point
     */
    public void setExtents(Point p1, Point p2){
        extents[0] = p1;
        extents[1] = p2;
    }


    /**
     * Fires the weapon given.
     *
     * @param power how much oomph to give to the shot
     * @param angle which angle the shot will be fired at
     * @param weapon the actual weapon to fire
     */
    public void fireWeapon(double power, double angle, Weapon weapon){
        // TODO change speed according to the weapon's mass
        double speed = power;
        weapon.onFire(position, speed, angle);
    }


    /**
     * Moves the bunny sideways. The bunny just moves MOVE_SPEED spaces in whatever direction,
     * regardless of the terrain.
     *
     * @param left true if the bunny is moving to the left, false if they are to move right
     * @param map the terrain to move on
     */
    public void moveSideways(boolean left, Terrain map){
    }


	@Override
	public void draw(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawCircle(position.x, position.y, radius, paint);
	}


	@Override
	public int getOpacity(){
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setAlpha(int alpha){
		// TODO Auto-generated method stub
	}


	@Override
	public void setColorFilter(ColorFilter cf){
		// TODO Auto-generated method stub
	}
}
