package d22.dungeon.api;

import processing.core.PGraphics;

/**
 * 
 * @author d22 et al. 
 */
public interface Drawable {
    /**
     * Updates the given object.
     * 
     * @param $dt
     *            The change in time since the last frame.
     */
    public void update(final float $dt);

    /**
     * Draws the object on the given graphics canvas.
     * 
     * @param $graphics
     *            The graphics canvas to draw on.
     */
    public void draw(final PGraphics $graphics);
}
