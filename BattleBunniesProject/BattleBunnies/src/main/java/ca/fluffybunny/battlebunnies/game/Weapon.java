package ca.fluffybunny.battlebunnies.game;

import java.io.Serializable;

import ca.fluffybunny.battlebunnies.util.Point;

/**
 * A weapon. My goodness. Contains default implementations for a standard projectile weapon.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public abstract class Weapon implements Serializable, Cloneable {

    protected double mass;              //how heavy the weapon is, decreased initial velocity
    protected double explosionRadius;   //how big the explosion is
    protected double scoreValue;        //max score we can get from using this weapon
    protected Point fireLocation;       //where the shot was fired from
    protected double fireSpeed;         //initial fire speed, for use in position finding later
    protected double fireAngle;         //initial fire angle, for use in position finding later

    public final static double GRAVITY = -9.8;

    /**
     * Getters/Setters.
     */
    public void setMass(double mass){ this.mass = mass; }
    public void setExplosionRadius(double radius){ explosionRadius = radius; }
    public void setScoreValue(double score){ scoreValue = score; }
    public void setFireLocation(Point loc){ fireLocation = loc; }
    public void setFireSpeed(double speed){ fireSpeed = speed; }
    public void setFireAngle(double angle){ fireAngle = angle; }
    public double getMass(){ return mass; }
    public double getExplosionRadius(){ return explosionRadius; }
    public double getScoreValue(){ return scoreValue; }
    public Point getFireLocation() { return fireLocation; }
    public double getFireSpeed(){ return fireSpeed; }
    public double getFireAngle(){ return fireAngle; }


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
    public void onFire(Point start, double speed, double angle){
        setFireLocation(start);
        setFireSpeed(speed);
        setFireAngle(angle);
    }


    /**
     * To be called in OpenGL callbacks. Draws the weapon in flight.
     */
    public abstract void onDraw();


    /**
     * To be called in OpenGL callbacks. Draws the explosion of the weapon.
     */
    public abstract void onExplode();


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
}
