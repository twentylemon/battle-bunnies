package com.fluffybunny.battlebunnies.game;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * A weapon. My goodness. Contains default implementations for a standard projectile weapon.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public abstract class Weapon extends Drawable implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

    protected double mass;              //how heavy the weapon is, decreased initial velocity
    protected double explosionRadius;   //how big the explosion is
    protected double scoreValue;        //max score we can get from using this weapon
    protected Point fireLocation;       //where the shot was fired from
    protected double fireSpeed;         //initial fire speed, for use in position finding later
    protected double fireAngle;         //initial fire angle, for use in position finding later
    protected String name;				//name as it appears in the list of weapons
    
    public final static double GRAVITY = 5.0 * 9.8;
    
    /**
     * Getters/Setters.
     */
    public void setMass(double mass){ this.mass = mass; }
    public void setExplosionRadius(double radius){ explosionRadius = radius; }
    public void setScoreValue(double score){ scoreValue = score; }
    public void setFireLocation(Point loc){ fireLocation = loc; }
    public void setFireSpeed(double speed){ fireSpeed = speed; }
    public void setFireAngle(double angle){ fireAngle = Math.toRadians(360 - angle); }
    public double getMass(){ return mass; }
    public double getExplosionRadius(){ return explosionRadius; }
    public double getScoreValue(){ return scoreValue; }
    public Point getFireLocation() { return fireLocation; }
    public double getFireSpeed(){ return fireSpeed; }
    public double getFireAngle(){ return fireAngle; }
    public String getName(){ return name; }
    

    /**
     * Returns where the projectile is after some amount of time.
     *
     * @param time how much time has passed (frames)
     * @return location of the weapon in the world
     */
    public Point getPosition(double time){
        return fireLocation.add(new Point(
            fireSpeed * time * Math.cos(fireAngle),
            fireSpeed * time * Math.sin(fireAngle) + GRAVITY * time*time / 2.0
        ));
    }
    
 
    /**
     * Initializes the weapon for the fired state. This must be called before
     * any { @link getPosition() } calls.
     *
     * @param start where the shot was taken from
     * @param speed how fast the weapon is moving
     * @param angle at which angle the shot was taken
     */
    public void initFire(Point start, double speed, double angle){
        setFireLocation(start);
        setFireSpeed(speed);
        setFireAngle(angle);
    }

    
    /**
     * Stuff for drawing the weapon.
     * 
     * @param canvas where to draw
     * @param where where the weapon is
     */
    public abstract void explode(Canvas canvas, Point where);
    public abstract void draw(Canvas canvas, Point where);


    /**
     * Clones this weapon.
     *
     * @return a clone of this weapon
     */
    public Weapon clone(){
        try {
            return (Weapon)super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    
    /**
     * Override this from Object. Two weapons are equal if they have the same name.
     * 
     * @param other the object to compare to
     */
    @Override
    public boolean equals(Object other){
    	if (other instanceof Weapon){
    		Weapon weapon = (Weapon) other;
    		return getName().equals(weapon.getName());
    	}
    	return false;
    }
}
