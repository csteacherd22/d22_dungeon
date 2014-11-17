package d22.dungeon.blocks;

import processing.core.PVector;

/**
 * 
 * @author d22 et al.  
 */
public class BlockFactory {
    /**
     * Creates a Block for you based on the position and tile type you want
     * 
     * @param $pos
     * @param $blockType
     * @return
     */
    public static Block create(final PVector $pos, final Block.Type $blockType) {
        switch ($blockType) {
        case FOOD:
            return new FoodBlock($pos);
        case TREASURE:
            return new TreasureBlock($pos);
        default:
            return new EmptyBlock($pos);
        }
    }
}
