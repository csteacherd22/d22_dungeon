package d22.dungeon;


import d22.dungeon.api.Screen;
import d22.dungeon.blocks.Block;
import d22.dungeon.screens.Board;
import d22.dungeon.screens.Editor;

import processing.core.PFont;

import android.view.MotionEvent;

/**
 * 
 * @author d22 et al. 
 */
public class Game {

	
    public static final int BOARD = 1;
    public static final int EDITOR = 2;
    public static final int TUTORIAL = 3;
    public static final int SPLASH = 4;

    public static final int BOARD_SIZE = 10;
    public static final int WIDTH = BOARD_SIZE * Block.SIZE;
    public static final int HEIGHT = BOARD_SIZE * Block.SIZE + 300;

    public static Screen screen, splash;
    public static Board board;
    public static Editor editor;
    public static PFont font;

    public static void switchScreen(int s) {
        switch (s) {
        case EDITOR:
            screen = editor;
            break;
        case TUTORIAL:
        	//You should consider adding a tutorial screen
            break;
        default:
            screen = board;
            break;
        }

    }

    public static void nextLevel() {
        if (screen instanceof Board)
            board.nextLevel();
    }

    public static void previousLevel() {
        if (screen instanceof Board)
            board.previousLevel();
    }

    public static void handleMotionEvent(MotionEvent e) {
        if (screen != null)
            screen.handleMotionEvent(e);
    }
}
