package d22.dungeon.screens;

import processing.core.PVector;
import processing.core.PGraphics;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;


import d22.dungeon.Button;
import d22.dungeon.Game;
import d22.dungeon.Player;
import d22.dungeon.api.Screen;
import d22.dungeon.blocks.Block;
import d22.dungeon.blocks.BlockFactory;
import d22.dungeon.statements.StatementInstance;

/**
 * 
 * @author d22 et al. 
 */
public class Board implements Screen {
    // Instance variables.
    private Block[][] _blocks;
    private int _size_x, _size_y;
    private PVector _dims;
    private static Board _board;
    private Player _player;
    private int level, _foodcount;
    private boolean _running = false, _reset = true;
    private static final String LEVEL ="Level";

    private Button nextLevelButton, previousLevelButton, editorButton, execButton, resetButton;

    /**
     * Creates a new board instance
     * 
     * @param $sx
     *            The width of the board
     * @param $sy
     *            The height of the board
     */
    public Board(final int $sx, final int $sy) {
        _size_x = $sx;
        _size_y = $sy;
        _dims = new PVector($sx * Block.SIZE, $sy * Block.SIZE);
        level = 0;

        int offset = (Block.SIZE - Player.SIZE) / 2;
        _player = new Player(new PVector(offset, offset));

        /*
         * Rather than create a new button class for each individual action, it made more sense to
         * just build anonymous like classes where they will be defined here.
         */
        editorButton = new Button(10, 720, 50, 50) {
            public void draw(final PGraphics $graphics) {
                $graphics.pushMatrix();
                $graphics.translate(this.x, this.y);
                if (this.clicked)
                    $graphics.fill(200, 176, 232);
                else
                    $graphics.fill(255, 255, 255);
                $graphics.noStroke();
                $graphics.rect(0, 0, this.w, this.h);
                $graphics.image(Button.swap, 0, 0);
                $graphics.popMatrix();
            }
        };
        // Bind an event to be triggered when clicked
        editorButton.event(new Runnable() {
            public void run() {
                reset();
                setRunning(false);
                Game.switchScreen(Game.EDITOR);
            }
        });

        nextLevelButton = new Button(130, 720, 50, 50) {
            public void draw(final PGraphics $graphics) {
                $graphics.pushMatrix();
                $graphics.translate(this.x, this.y);
                if (this.clicked)
                    $graphics.fill(111, 214, 237);
                else
                    $graphics.fill(255, 255, 255);
                $graphics.noStroke();
                $graphics.rect(0, 0, this.w, this.h);
                $graphics.image(Button.levelup, 0, 0);
                $graphics.popMatrix();
            }
        };
        // Bind an event to be triggered when clicked
        nextLevelButton.event(new Runnable() {
            public void run() {
                hardReset();
                nextLevel();
            }
        });

        previousLevelButton = new Button(70, 720, 50, 50) {
            public void draw(final PGraphics $graphics) {
                $graphics.pushMatrix();
                $graphics.translate(this.x, this.y);
                if (this.clicked)
                    $graphics.fill(111, 214, 237);
                else
                    $graphics.fill(255, 255, 255);
                $graphics.noStroke();
                $graphics.rect(0, 0, this.w, this.h);
                $graphics.image(Button.leveldown, 0, 0);
                $graphics.popMatrix();
            }
        };
        // Bind an event to be triggered when clicked
        previousLevelButton.event(new Runnable() {
            public void run() {
                hardReset();
                previousLevel();
            }
        });

        execButton = new Button(190, 720, 135, 50) {
            public void draw(final PGraphics $graphics) {
                $graphics.pushMatrix();
                $graphics.translate(this.x, this.y);
                if (this.clicked)
                    $graphics.fill(89, 209, 75);
                else
                    $graphics.fill(255, 255, 255);
                $graphics.noStroke();
                $graphics.rect(0, 0, this.w, this.h);
                $graphics.image(Button.execute, 0, 0);
                $graphics.popMatrix();
            }
        };
        // Bind an event to be triggered when clicked
        execButton.event(new Runnable() {
            public void run() {
                toggleRunning();
            }
        });

        resetButton = new Button(335, 720, 135, 50) {
            public void draw(final PGraphics $graphics) {
                $graphics.pushMatrix();
                $graphics.translate(this.x, this.y);
                if (this.clicked)
                    $graphics.fill(240, 79, 79);
                else
                    $graphics.fill(255, 255, 255);
                $graphics.noStroke();
                $graphics.rect(0, 0, this.w, this.h);
                $graphics.image(Button.reset, 0, 0);
                $graphics.popMatrix();
            }
        };
        // Bind an event to be triggered when clicked
        resetButton.event(new Runnable() {
            public void run() {
                setRunning(false);
                reset();
            }
        });

        this.generateBoard(level);
    }

    public static Board getInstance() {
        return Game.board;
    }

    public Block[][] getBlocks() {
        return _blocks;
    }

    public void setRunning(boolean $running) {
        if (_running) {
            _running = $running;
            _reset = false;
        } else if (_reset) {
            _running = $running;
        }
    }

    public void toggleRunning() {
        setRunning(!_running);
    }

    public static Board getInstance(int $sx, int $sy) {
        if (_board == null)
            return (_board = new Board($sx, $sy));

        return _board;
    }

    public void setPlayer(Player $player) {
        _player = $player;
    }

    public void update(final float $dt) {
        // If the activity is being run execute each statement.
        StatementInstance instance = Game.editor.getStatement();

        if (_running && instance != null) {
            if (instance.executed()) {
                instance.reset();
                if (_running)
                    instance = Game.editor.nextStatement();
            } else {
                instance.eval();
            }
        } else if (!_reset && instance != null) {
            _reset = true;
            this.reset();
        }

        // Update the player.
        _player.update($dt);
    }

    public void draw(final PGraphics $graphics) {
        $graphics.fill(0xFF000000);
        $graphics.rect(0, 0, Game.WIDTH, Game.HEIGHT);

        $graphics.pushMatrix();
        $graphics.translate(0, 140);
        // Set up the graphics mode to correctly draw the blocks
        // and draw each block.
        $graphics.rectMode(PGraphics.CORNER);
        $graphics.stroke(0);
        
        if (null == _blocks) {
        	return;
        	// Graphics not yet initialized/loaded
        	// That's fin
        }
        
        synchronized (_blocks) {
            for (int y = 0; y < _size_y; y++) {
                for (int x = 0; x < _size_x; x++) {
                    _blocks[x][y].draw($graphics);
                }
            }
        }
        

        // Draw the player.
        _player.draw($graphics);

        // Draw the HUD text.
        $graphics.translate(0, 480);
        $graphics.textFont(Game.font);
        $graphics.fill(0xFFFFFFFF);
        $graphics.text("Treasure: " + _player.getTreasureCount(), 5, 20);
        $graphics.text("Food:     " + _player.getFoodCount(), 5, 45); //100, 22);
        $graphics.text("Score:    " + _player.getScore(), 5, 70); //175, 22);

        // Draw the button panel.
        $graphics.translate(0, 90);
        $graphics.rect(0, 0, Game.WIDTH, 70);
        $graphics.popMatrix();

        // Draw the buttons.
        nextLevelButton.draw($graphics);
        previousLevelButton.draw($graphics);
        editorButton.draw($graphics);
        execButton.draw($graphics);
        resetButton.draw($graphics);
    }

    public final PVector getDims() {
        return new PVector(_dims.x, _dims.y);
    }

    public void visitBlock(final Point $position) {

        _blocks[$position.x][$position.y].doAction(_player);

        if (_player.getFoodCount() == _foodcount) {
            hardReset();
            nextLevel();
        }
    }

    public void hardReset() {
        _foodcount = 0;
        _player.reset();
    }

    public void handleMotionEvent(MotionEvent $event) {
        int x = (int) $event.getX(), y = (int) $event.getY();
        switch ($event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            nextLevelButton.isClicked(x, y);
            previousLevelButton.isClicked(x, y);
            editorButton.isClicked(x, y);
            execButton.isClicked(x, y);
            resetButton.isClicked(x, y);
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            // When the mouse is released, all of the buttons should be
            // released as well
            nextLevelButton.release();
            previousLevelButton.release();
            editorButton.release();
            execButton.release();
            resetButton.release();
            break;
        }
    }

    public Player getPlayer() {
        return _player;
    }

    /**
     * Generates a new board. This will destroy the current board, if one is not present then that's
     * okay
     */
    public synchronized void generateBoard(int level) {
        _blocks = new Block[_size_x][_size_y];
        int[][] map = getLevel(level);

        for (int y = 0; y < _size_y; y++) {
            for (int x = 0; x < _size_x; x++) {
                Block.Type blockType = Block.Type.get(map[x][y]);
                _blocks[x][y] = BlockFactory.create(new PVector(x * Block.SIZE, y * Block.SIZE),
                        blockType);

                if (blockType == Block.Type.FOOD) {
                    _foodcount += 2;
                }
            }
        }

    }

    /**
     * @return Gets the 2D integer array for the level provided
     */
    private static int[][] getLevel(int level) {
        switch (level) {
        case 0:
        	Log.i(LEVEL,"level is 0");
            return LEVEL_0;
        case 1:
        	Log.i(LEVEL,"level is 1");
            return LEVEL_1;
        case 2:
        	Log.i(LEVEL,"level is 2");
            return LEVEL_2;
        case 3:
        	Log.i(LEVEL,"level is 3");
            return LEVEL_3;
        case 4:
        	Log.i(LEVEL,"level is 4");
            return LEVEL_4;
        case 5:
        	Log.i(LEVEL,"level is 5");
            return LEVEL_5;
        case 6:
        	Log.i(LEVEL,"level is 6");
            return LEVEL_6;
        case 7:
        	Log.i(LEVEL,"level is 7");
            return LEVEL_7;
        case 8:
        	Log.i(LEVEL,"level is 8");
            return LEVEL_8;
        case 9:
        	Log.i(LEVEL,"level is 9");
            return LEVEL_9;
        default:
            return generateRandomLevel(level);
        }
    }

    private synchronized static int[][] generateRandomLevel(int level) {
    	final int MAX_X = 10;
    	final int MAX_Y = 10;
    	final int MAX_TREASURE = 3;
    	final int MAX_FOOD = 8;
    	
    	java.util.Random myR = new java.util.Random(level);
    	
    	int ret[][] = new int[MAX_X][MAX_Y];
        for (int y = 0; y < MAX_Y; y++) {
            for (int x = 0; x < MAX_X; x++) {
            	ret[x][y] = Block.Type.EMPTY.value;
            }
        }

    	// MAX_TREASURE+MAX_FOOD must be less than MAX_X*MAX_Y !! or else crash
//    	if ((MAX_TREASURE + MAX_FOOD) >= (MAX_X * MAX_Y)) {
//    		System.err.println("Board::generateRandomLevel: Busted input");
//    		return ret;
//    	}

        
        for (int treasure = 0; treasure < MAX_TREASURE; treasure++) {
        	int rx,ry = 0;
        	do {
        		rx = (int)Math.floor(myR.nextDouble() * MAX_X);
        		ry = (int)Math.floor(myR.nextDouble() * MAX_Y);
        	} while (ret[rx][ry] != Block.Type.EMPTY.value);
        	ret[rx][ry] = Block.Type.TREASURE.value;
        }

        for (int food = 0; food < MAX_FOOD ; food++) {
        	int rx,ry = 0;
        	do {
        		rx = (int)Math.floor(myR.nextDouble() * MAX_X);
        		ry = (int)Math.floor(myR.nextDouble() * MAX_Y);
        	} while (ret[rx][ry] != Block.Type.EMPTY.value);
        	ret[rx][ry] = Block.Type.FOOD.value;
        }

        
        return ret;
    }

    public void nextLevel() {
        setRunning(false);
        reset();
        level++;
//        if (level > 9)
//            level = 9;
        //XXX random level enabled
        this.generateBoard(level);
    }

    public void previousLevel() {
        setRunning(false);
        reset();
        if (--level < 0)
            level = 0;
        this.generateBoard(level);
    }

    public void reset() {
        StatementInstance si = Game.editor.getStatement();
        if (si != null)
            Game.editor.getStatement().reset();
        Game.editor.reset();
        _player.reset();

        synchronized (_blocks) {
            for (int y = 0; y < 10; y++)
                for (int x = 0; x < 10; x++)
                    _blocks[y][x].reset();
        }
    }

    // ###########################################################################
    // The Static Levels
    //
    // @see Block.Type for more information on what the integer values mean.
    // ###########################################################################
    private static final int[][] LEVEL_0 = { { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_1 = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 2, 0, 0, 2, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 2, 0, 0, 2, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_2 = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 2, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 2, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_3 = { { 0, 1, 1, 1, 2, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 2, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_4 = { { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 2, 0, 1 },
            { 0, 2, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 1, 0, 0 } };

    private static final int[][] LEVEL_5 = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 2, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 2, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_6 = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 1, 1, 0 },
            { 0, 0, 0, 1, 0, 0, 1, 0, 1, 0 }, { 0, 0, 0, 0, 2, 2, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 2, 2, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_7 = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 2, 0, 1, 0, 2, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_8 = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private static final int[][] LEVEL_9 = { { 1, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 2, 1, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0, 2, 0 },
            { 0, 0, 1, 2, 1, 1, 0, 0, 1, 0 }, { 0, 0, 2, 0, 0, 0, 1, 0, 1, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

}
