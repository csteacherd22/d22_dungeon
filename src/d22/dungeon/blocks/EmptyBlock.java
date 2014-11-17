package d22.dungeon.blocks;

import d22.dungeon.Player;

import processing.core.PVector;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * The blank block, it holds nothing
 * 
 * @author d22 et al. 
 
 
 
 
 */
public class EmptyBlock extends Block {
    public static PImage img;

    public EmptyBlock(final PVector $pos) {
        super($pos);
    }

    public void update(final float $dt) {
    }

    public void draw(final PGraphics $graphics) {
        super.draw($graphics);

        $graphics.popMatrix();
    }

    public void doAction(Player p) {
    	p.addToScore(-1);
    }

    public boolean claimed() {
        return false;
    }

    public void reset() {
    }
}
