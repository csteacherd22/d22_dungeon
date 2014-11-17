package d22.dungeon;


import d22.dungeon.blocks.*;
import d22.dungeon.screens.*;
import d22.dungeon.statements.IfStatement;
import d22.dungeon.statements.MoveStatement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

import processing.core.*;

/**
 * 
 * @author d22
 * @author et al. 
 */
public class D22DungeonDesigners extends PApplet {
    private float _last_time, _dt;
    private BitmapFactory.Options _options;

    public void setup() {
        // Configure the bitmap factory sizing...
        _options = new BitmapFactory.Options();
        _options.inScaled = false;

        // Build the board and player.
        Game.board = new Board(Game.BOARD_SIZE, Game.BOARD_SIZE);
        Game.editor = new Editor(new PVector(Game.WIDTH, Game.HEIGHT));
        Game.screen = Game.splash = new SplashScreen();
        
        //XXX Debug
//        String[] fontList = PFont.list();
//        println(fontList);
        
        Game.font = createFont("Monospaced",16);

        // Prep time calculations.
        _last_time = millis();

        SplashScreen.logo = loadImage(d22.dungeon.R.drawable.logo);
        SplashScreen.font = createDefaultFont(24);

        Block.img = loadImage(d22.dungeon.R.drawable.grass);
        FoodBlock.img = loadImage(d22.dungeon.R.drawable.food);
        TreasureBlock.img = loadImage(d22.dungeon.R.drawable.treasure);
        IfStatement.image = loadImage(d22.dungeon.R.drawable.ifbtn);
        MoveStatement.image = loadImage(d22.dungeon.R.drawable.dobtn);

        Player.imgs = new PImage[5];
        Player.imgs[Player.Movement.UP.dir] = loadImage(d22.dungeon.R.drawable.up);
        Player.imgs[Player.Movement.DOWN.dir] = loadImage(d22.dungeon.R.drawable.down);
        Player.imgs[Player.Movement.LEFT.dir] = loadImage(d22.dungeon.R.drawable.left);
        Player.imgs[Player.Movement.RIGHT.dir] = loadImage(d22.dungeon.R.drawable.right);

        Button.swap = loadImage(d22.dungeon.R.drawable.switchbtn);
        Button.execute = loadImage(d22.dungeon.R.drawable.execute);
        Button.levelup = loadImage(d22.dungeon.R.drawable.nextbtn);
        Button.leveldown = loadImage(d22.dungeon.R.drawable.lastbtn);
        Button.reset = loadImage(d22.dungeon.R.drawable.reset);
        Button.play = loadImage(d22.dungeon.R.drawable.startbtn);
        Button.tutorial = loadImage(d22.dungeon.R.drawable.tutorialbtn);
    }

    public int sketchWidth() {
        return Game.WIDTH;
    }

    public int sketchHeight() {
        return Game.HEIGHT;
    }
    
    public void draw() {
        // Calculate the time delta.
        float temp = millis();
        _dt = (temp - _last_time) / 1000;
        _last_time = temp;

        // Update the core screens.
        Game.board.update(_dt);
        Game.editor.update(_dt);

        // Draw only the current selected screen.
        Game.screen.draw(g);
    }

    public boolean surfaceTouchEvent(MotionEvent $event) {
        Game.handleMotionEvent($event);
        return super.surfaceTouchEvent($event);
    }

    public PImage loadImage(final int $resourceID) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), $resourceID, _options);
        return new PImage(image);
    }
}
