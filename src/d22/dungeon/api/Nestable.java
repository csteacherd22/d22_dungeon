package d22.dungeon.api;

import d22.dungeon.statements.StatementInstance;

/**
 * 
 * @author d22 et al. 
 */
public interface Nestable {
    /**
     * Adds a child StatementInstance to this nestable.
     * 
     * @param $instance
     *            The instance to be added.
     */
    public void addChild(StatementInstance $instance);

    /**
     * Removes all instances of the StatementInstance from this nestable.
     * 
     * @param $instance
     *            The instance to be removed.
     */
    public void removeAllInstances(StatementInstance $instance);
}
