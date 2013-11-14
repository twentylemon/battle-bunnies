package ca.fluffybunny.battlebunnies;

import android.graphics.Point;

/**
 * Provides a default implementation for a projectile-like weapon.
 *
 * @author Taras Mychaskiw
 * @version 0.1
 * @since 2013-11-14
 */
public abstract class ProjectileWeapon implements Weapon {

    /**
     * Returns the weapon's location after some number of frames has passed.
     * @param initialSpeed the starting speed the weapon was shot at
     * @param initialDirection the angle at which the weapon was shot
     * @param time how many frames have passed
     * @return the location of the weapon
     */
    @Override
    public Point getLocation(int initialSpeed, int initialDirection, int time){
        return new Point(
                (int)(initialSpeed * time * Math.cos(initialDirection)),
                (int)(initialSpeed * time * Math.sin(initialDirection) - GRAVITY * time*time / 2.0)
        );
    }
}
