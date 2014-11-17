package d22.dungeon.statements.conditionals;

/**
 * Helper class to wrap a statement to be evaluated.
 * 
 \* @author d22 et al. 
 
 
 
 
 */
public interface Evaluator {
    /**
     * Evaluates the nested statement.
     * 
     * @return true if successful, false if not.
     */
    public boolean eval();
}
