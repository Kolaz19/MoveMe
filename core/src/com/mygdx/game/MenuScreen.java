package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class MenuScreen extends ScreenAdapter {

    static Texture backgroundTexture;

    protected OrthographicCamera orthographicCamera;
    protected final int backgroundHeight;
    protected final int backgroundWidth;
    protected SpriteBatch spriteBatch;
    protected MenuScreen.Coordinate cordsHero, cordsEnemy;
    protected final int borderThickness;
    protected final int sizeOfCharTexture;
    protected Vector3 mouseCoordinates;
    protected MyGdxGame mainGame;

    static {
        backgroundTexture = new Texture("backgroundMenu.png");
    }

    protected enum BUTTON {
        EXIT,
        PLAY,
        STAGE,
        NOSELECT
    }

    public MenuScreen(MyGdxGame maingame) {
        this.mainGame = maingame;
        backgroundHeight = backgroundTexture.getHeight();
        backgroundWidth = backgroundTexture.getWidth();
        orthographicCamera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();

        borderThickness = 32;
        sizeOfCharTexture = 55;



        cordsHero = new MenuScreen.Coordinate(borderThickness,borderThickness);
        cordsEnemy = new MenuScreen.Coordinate(backgroundWidth - borderThickness - sizeOfCharTexture,backgroundHeight - borderThickness - sizeOfCharTexture);
        mouseCoordinates = new Vector3(0,0,0);
        mouseCoordinates.z = 0;
    }

    @Override
    public void show() {
        orthographicCamera.viewportHeight = backgroundHeight;
        orthographicCamera.viewportWidth = backgroundWidth;
        orthographicCamera.position.x = (int) (backgroundWidth / 2);
        orthographicCamera.position.y = (int) (backgroundHeight / 2);
        Gdx.graphics.setWindowedMode(backgroundWidth,backgroundHeight);
    }

    @Override
    public void render(float delta) {

    }

    protected void basicBackgroundRendering() {
        spriteBatch.draw(backgroundTexture,0,0);
        spriteBatch.draw(Hero.idleTexture, cordsHero.x, cordsHero.y,sizeOfCharTexture,sizeOfCharTexture);
        spriteBatch.draw(Enemy.idleTexture, cordsEnemy.x, cordsEnemy.y,sizeOfCharTexture,sizeOfCharTexture);
    }

    protected void basicScreenRenderSetup() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        orthographicCamera.update();
        moveCharInBackground(3,cordsHero);
        moveCharInBackground(3,cordsEnemy);
        setMouseCoordinates();
    }





    protected int getAnimationNumber(boolean isButtonSelected) {
        if (isButtonSelected) {
            return 2;
        } else {
            return 1;
        }
    }

    protected void setMouseCoordinates () {
        mouseCoordinates.x = Gdx.input.getX();
        mouseCoordinates.y = Gdx.input.getY();
        orthographicCamera.unproject(mouseCoordinates);
    }

    protected void moveCharInBackground(int pixelsPerFrames, MenuScreen.Coordinate cords) {
        correctOutOfBounds(pixelsPerFrames,cords);
        moveTextureInBoundary(pixelsPerFrames,cords);
    }

    protected void correctOutOfBounds (int pixelsToMove, MenuScreen.Coordinate cords) {
        //Snap to boundary coordinates if texture would move out
        if (willBeOutOfBoundsDown(cords, pixelsToMove) && isRightBoundary(cords)) {
            cords.y = borderThickness;
        } else if (willBeOutOfBoundsUp(cords,pixelsToMove) && isLeftBoundary(cords)){
            cords.y = backgroundHeight - borderThickness - sizeOfCharTexture;
        } else if (willBeOutOfBoundsLeft(cords,pixelsToMove) && isBottomBoundary(cords)) {
            cords.x = borderThickness;
        } else if (willBeOutOfBoundsRight(cords,pixelsToMove) && isUpperBoundary(cords)) {
            cords.x = backgroundWidth - borderThickness - sizeOfCharTexture;
        }
    }

    protected void moveTextureInBoundary(int pixelsPerFrames, MenuScreen.Coordinate cords) {
        if (isRightBoundary(cords) && !isBottomBoundary(cords)) {
            //move down
            cords.addToY(pixelsPerFrames * -1);
        } else if (isBottomBoundary(cords) && !isLeftBoundary(cords)) {
            //move left
            cords.addToX(pixelsPerFrames * -1);
        } else if (isLeftBoundary(cords) && !isUpperBoundary(cords)) {
            //move up
            cords.addToY(pixelsPerFrames);
        } else if (isUpperBoundary(cords) && !isRightBoundary(cords)) {
            //move right
            cords.addToX(pixelsPerFrames);
        }
    }

    protected boolean willBeOutOfBoundsRight (MenuScreen.Coordinate cords, int pixels) {
        if (cords.x + pixels > backgroundWidth - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }

    protected boolean willBeOutOfBoundsLeft (MenuScreen.Coordinate cords, int pixels) {
        if (cords.x - pixels < borderThickness) {
            return true;
        }
        return false;
    }

    protected boolean willBeOutOfBoundsUp (MenuScreen.Coordinate cords, int pixels) {
        if (cords.y + pixels > backgroundHeight - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }

    protected boolean willBeOutOfBoundsDown (MenuScreen.Coordinate cords, int pixels) {
        if (cords.y - pixels < borderThickness) {
            return true;
        }
        return false;
    }

    protected boolean isRightBoundary (MenuScreen.Coordinate cords) {
        if (cords.x == backgroundWidth - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }

    protected boolean isBottomBoundary (MenuScreen.Coordinate cords) {
        if (cords.y == borderThickness) {
            return true;
        }
        return false;
    }

    protected boolean isLeftBoundary (MenuScreen.Coordinate cords) {
        if (cords.x == borderThickness) {
            return true;
        }
        return false;
    }

    protected boolean isUpperBoundary (MenuScreen.Coordinate cords) {
        if (cords.y == backgroundHeight - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }


    protected class Coordinate {
        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setCoordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void addToX(int amount) {
            this.x += amount;
        }

        public void addToY(int amount) {
            this.y += amount;
        }
    }

}
