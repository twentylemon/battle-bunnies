package ca.fluffybunny.battlebunnies;


import android.graphics.Point;

/**
 * Interface for a basic weapon in the game.
 * A weapon basically can be drawn and you can ask it where it the projectile currently is.
 *
 * @author Taras Mychaskiw
 * @version 0.1
 * @since 2013-11-14
 */
public interface Weapon {

    public final static double GRAVITY = -9.8;

    /**
     * Returns the weapon's location after some number of frames has passed.
     * @param initialSpeed the starting speed the weapon was shot at
     * @param initialDirection the angle at which the weapon was shot
     * @param time how many frames have passed
     * @return the location of the weapon
     */
    public Point getLocation(int initialSpeed, int initialDirection, int time);
}
