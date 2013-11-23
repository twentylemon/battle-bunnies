package ca.fluffybunny.battlebunnies.game;

/**
 *
 */
public class Point {

    public int x;
    public int y;

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
