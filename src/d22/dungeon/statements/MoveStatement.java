package d22.dungeon.statements;


import d22.dungeon.Player;
import d22.dungeon.screens.Board;

import processing.core.PGraphics;
import processing.core.PImage;

/**
 * 
 * @author d22 et al. 
 */
public class MoveStatement extends ProgrammingStatement {
    private static final int FILL_COLOR = 0xFF00FF00;
    private static final int BASE_WIDTH = 120;
    private static final int BASE_HEIGHT = 40;
    public static PImage image;

    public PImage getImage() {
        return image;
    }

    public StatementInstance spawnInstance() {
        return new MoveStatementInstance();
    }

    protected class MoveStatementInstance extends StatementInstance {
        private Player.Movement _movement = Player.Movement.UP;
        private String _label = "Move Up";
        private boolean _complete = true;
        private boolean _executed = false;

        public void update(final float $dt) {
        }

        public void draw(final PGraphics $graphics) {
            $graphics.fill(FILL_COLOR);
            $graphics.rect(_pos.x, _pos.y, BASE_WIDTH, BASE_HEIGHT);
            $graphics.fill(0xFF000000);
            $graphics.text(_label, _pos.x + 8, _pos.y + 25);
        }

        public int getHeight() {
            return BASE_HEIGHT;
        }

        public int getWidth() {
            return BASE_WIDTH;
        }

        public StatementInstance instanceUnder(final float $x, final float $y) {
            if ($x > _pos.x && $y > _pos.y && $x < _pos.x + BASE_WIDTH && $y < _pos.y + BASE_HEIGHT) {
                return this;
            }
            return null;
        }

        public void handleClick() {
            _movement = _movement.next();
            _label = "Move " + _movement.getLabel();
        }

        public boolean isDone() {
            return _complete;
        }

        public boolean executed() {
            return _executed;
        }

        public void reset() {
            if (!_complete) {
                Board.getInstance().getPlayer().stopMoving();
            }
            _complete = true;
            _executed = false;
        }

        public void eval() {
            if (_complete && !_executed) {
                _complete = false;
                Board.getInstance().getPlayer().move(_movement);
            }

            if (!Board.getInstance().getPlayer().isMoving()) {
                _complete = _executed = true;
            }
        }
    }
}
