package com.fluffybunny.battlebunnies.game;

import java.io.Serializable;

public class Bunny implements Serializable {
	private static final long serialVersionUID = 1L;

    private Point position;     //where we are located on the map
    private int movesRemaining; //how many times we can move
    private int score;          //how many points we have
    private String name;    	//our display name
    private int radius;			//the size of the bunny
    private Point[] extents;    //how big on the map the bunny is (for collision detection)
    private int myID;			//our player ID
    private int resImage;		//resource id of our image

    public final static int DEFAULT_NUM_MOVES = 15;
    public final static int MOVE_SPEED = 50;
    public final static int RADIUS = 32;
    
    /**
     * Initializes the bunny.
     *
     * @param name the name to display on the screen
     * @param score how many points the bunny has
     * @param moves how many moves the bunny has remaining
     * @param loc where the bunny starts, (minX,minY) point
     * @param radius the size of the bunny (for extents)
     * @param image the image resource that this bunny should use to draw
     * @param id the player ID
     */
    public Bunny(String name, int score, int moves, Point loc, int radius, int image, int id){
        this.name = name;
        this.score = score;
        this.movesRemaining = moves;
        this.resImage = image;
        this.radius = radius;
        extents = new Point[2];
        this.position = loc;
        myID = id;
        extents[0] = position.add(new Point(-radius, -radius));
        extents[1] = position.add(new Point(radius, radius));
        extents[0] = loc;
        extents[1] = extents[0].add(new Point(-2*radius, -2*radius));
        position = new Point((extents[0].x + extents[1].x)/2.0, (extents[0].y + extents[1].y)/2.0);
    }


    /**
     * Initializes the bunny with 0 score, the default number of moves and default size.
     *
     * @param name the name to display on the screen
     * @param loc where the bunny starts
     * @param image the image resource that this bunny should use to draw
     * @param id the player ID
     */
    public Bunny(String name, Point loc, int image, int id){
        this(name, 0, DEFAULT_NUM_MOVES, loc, RADIUS, image, id);
    }


    /**
     * Getters/Setters.
     */
    public void setMovesRemaining(int movesRemaining){ this.movesRemaining = movesRemaining; }
    public void setScore(int score){ this.score = score; }
    public void setName(String name){ this.name = name; }
    public void setExtents(Point[] extents){ this.extents = extents; }
    public void addScore(int score){ this.score += score; }
    public int getMovesRemaining(){ return movesRemaining; }
    public int getScore(){ return score; }
    public Point getPosition(){ return position; }
    public String getName(){ return name; }
    public Point[] getExtents(){ return extents; }
    public int getImageResource(){ return resImage; }
    
    
    /**
     * Changes the position of the bunny. The extents are updated as well.
     * 
     * @param position where to bunny should end up
     * @throws NullPointerException if bitmap is not initialized
     */
    public void setPosition(Point position){
    	this.position = position;
    	Point size = new Point(radius, radius);
    	Point e0 = position.add(size);
    	size.x = -size.x; size.y = -size.y;
    	Point e1 = position.add(size);
    	setExtents(e0, e1);
    }


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
     * Returns true if the point is inside this bunny's extents.
     * 
     * @param point the point to test
     * @return true if point is in our extents
     */
    public boolean inExtents(Point point){
    	return point.x < extents[0].x && point.x > extents[1].x &&
    			point.y < extents[0].y && point.y > extents[1].y;
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
        Point point = new Point(extents[0].x, extents[1].y);
        if (myID != 0){
        	point = new Point(extents[1].x, extents[1].y);
        }
        weapon.initFire(point, speed, angle);
    }

    
    /**
     * Causes the bunny to fall down due to gravity.
     * 
     * @param terrain the terrain to fall onto
     * @throws NullPointerException if bitmap has not been initialized
     */
    public void fall(Terrain terrain){
    	Point pos = terrain.getHighestPointAt(position.x);
    	pos.y -= radius;
    	setPosition(pos);
    }


    /**
     * Moves the bunny sideways. The bunny just moves MOVE_SPEED spaces in whatever direction,
     * regardless of the terrain.
     *
     * @param left true if the bunny is moving to the left, false if they are to move right
     * @param terrain the terrain to move on
     * @throws NullPointerException if bitmap has not been initialized
     */
    public void moveSideways(boolean left, Terrain terrain){
    	if (movesRemaining > 0){
    		movesRemaining--;
	    	int x = 0;
	    	if (left){
	    		x = Math.max(0, position.x - MOVE_SPEED);
	    	}
	    	else {
	    		x = Math.min(terrain.getWidth() - 1, position.x + MOVE_SPEED);
	    	}
	    	Point pos = terrain.getHighestPointAt(x);
	    	pos.y -= radius;
	    	setPosition(pos);
    	}
    }
}
