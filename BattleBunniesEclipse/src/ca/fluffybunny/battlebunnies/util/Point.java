package ca.fluffybunny.battlebunnies.util;

import java.io.Serializable;

/**
 * Defines a point in space.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public class Point implements Serializable {

	private static final long serialVersionUID = 1L;
	public int x;   //public is fine
    public int y;

    /**
     * Default constructors. Set the x and y points.
     * @param x the x value
     * @param y the y value
     */
    public Point (int x, int y){
        this.x = x;
        this.y = y;
    }
    public Point(double x, double y){
        this((int) x, (int) y);
    }

    /**
     * Setters.
     */
    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }


    /**
     * Adds this point to another and returns the result.
     * @param p the other point to add to this one
     * @return the result of adding the two points
     */
    public Point add(Point p){
        return new Point(x + p.x, y + p.y);
    }
}
