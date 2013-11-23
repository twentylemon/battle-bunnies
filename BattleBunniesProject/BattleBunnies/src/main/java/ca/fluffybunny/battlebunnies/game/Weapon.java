package ca.fluffybunny.battlebunnies.game;

/**
 * A weapon. My goodness. Contains default implementations for a standard projectile weapon.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public abstract class Weapon {

    protected double mass;              //how heavy the weapon is, decreased initial velocity
    protected double explosionRadius;   //how big the explosion is
    protected double scoreValue;        //max score we can get from using this weapon
    protected double fireSpeed;         //initial fire speed, for use in position finding later
    protected double fireAngle;         //initial fire angle, for use in position finding later

    public final static double GRAVITY = -9.8;

    /**
     * Getters/Setters.
     */
    public void setMass(double mass){ this.mass = mass; }
    public void setExplosionRadius(double radius){ explosionRadius = radius; }
    public void setScoreValue(double score){ scoreValue = score; }
    public void setFireSpeed(double speed){ fireSpeed = speed; }
    public void setFireAngle(double angle){ fireAngle = angle; }
    public double getMass(){ return mass; }
    public double getExplosionRadius(){ return explosionRadius; }
    public double getScoreValue(){ return scoreValue; }
    public double getFireSpeed(){ return fireSpeed; }
    public double getFireAngle(){ return fireAngle; }


    /**
     * Returns where the projectile is after some amount of time.
     * @param time how much time has passed (frames)
     * @return location of the weapon in the world
     */
    public Point getPosition(double time){
        return new Point(
            fireSpeed * time * Math.cos(fireAngle),
            fireSpeed * time * Math.sin(fireAngle) + GRAVITY * time*time / 2.0
        );
    }
}
