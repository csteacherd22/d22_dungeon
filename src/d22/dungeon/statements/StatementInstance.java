package d22.dungeon.statements;

import d22.dungeon.api.Drawable;
import d22.dungeon.api.Nestable;

import processing.core.PVector;

/**
 * 
 * @author d22 et al. 
 */
public abstract class StatementInstance implements Drawable {
    /**
     * The spacing between each StatementInstance.
     */
    public static int SPACING = 10;

    /**
     * The absolute position of the instance.
     */
    protected PVector _pos;
    private Nestable _parent;

    /**
     * Creates a new StatementInstance and sets the absolute position to 0, 0.
     */
    public StatementInstance() {
        _pos = new PVector();
    }

    /**
     * Sets the absolute position of the instance.
     * 
     * @param $x
     *            The x coordinate.
     * @param $y
     *            The y coordinate.
     */
    public void setPos(final float $x, final float $y) {
        _pos.x = $x;
        _pos.y = $y;
    }

    /**
     * Gets the absolute position of the statement instance.
     * 
     * @return the absolute position of the statement instance.
     */
    public PVector getPos() {
        return _pos;
    }

    /**
     * Returns if the StatementInstance inherits from nestable.
     * 
     * @return true if the instance is nestable, false otherwise.
     */
    public boolean isNestable() {
        return this instanceof Nestable;
    }

    /**
     * Determines if the statement is a child of another statement.
     * 
     * @return true if no parent exists, false otherwise.
     */
    public boolean isChild() {
        return _parent != null;
    }

    /**
     * Gets the parent of this instance.
     * 
     * @return the parent.
     */
    public Nestable getParent() {
        return _parent;
    }

    /**
     * Sets the parent of the statement, which must be nestable.
     * 
     * @param $parent
     *            The parent statement.
     */
    public void setParent(final Nestable $parent) {
        // If the previous parent already exists, remove it.
        if (_parent != null) {
            _parent.removeAllInstances(this);
            _parent = null;
        }

        // Set up the base association.
        if ($parent != null) {
            _parent = $parent;
            return;
        }
    }

    /**
     * Get the StatementInstance under the given absolute coordinates. This is handy for nested
     * structures.
     * 
     * @param $x
     *            The x coordinate.
     * @param $y
     *            The y coordinate.
     * @return The StatementInstance found, null if nothing is under the cursor.
     */
    public abstract StatementInstance instanceUnder(final float $x, final float $y);

    /**
     * Gets the height of the StatementInstance.
     * 
     * @return the height of the StatementInstance.
     */
    public abstract int getHeight();

    /**
     * Gets the width of the StatementInstance.
     * 
     * @return the width of the StatementInstance.
     */
    public abstract int getWidth();

    /**
     * Handle a click event on the instance.
     */
    public abstract void handleClick();

    /**
     * Evaluate this statement.
     */
    public abstract void eval();

    /**
     * Checks if the statement has completed its action or animation. It is important to note that
     * this will return true if the instance has never been executed.
     * 
     * @return true if the animation is complete, false otherwise.
     */
    public abstract boolean isDone();

    /**
     * Checks if the statement has been executed. This is useful for checking if the statement
     * hasn't been run yet.
     * 
     * @return true if the statement has been executed, false if not.
     */
    public abstract boolean executed();

    /**
     * Resets the statement to its default, unexecuted state.
     */
    public abstract void reset();
}
