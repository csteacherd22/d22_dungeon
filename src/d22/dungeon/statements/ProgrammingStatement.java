package d22.dungeon.statements;

import processing.core.PImage;

/**
 * 
 * @author d22 et al. 
 */
public abstract class ProgrammingStatement {

    /**
     * Spawns an instance of the statement type.
     * 
     * @return the spawned instance.
     */
    public abstract StatementInstance spawnInstance();

    public abstract PImage getImage();
}
