package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen extends ScreenAdapter {
    static Texture backgroundTexture;
    private OrthographicCamera orthographicCamera;
    private final int backgroundHeight;
    private final int backgroundWidth;
    private SpriteBatch spriteBatch;
    private Coordinate cordsHero, cordsEnemy;
    private final int borderThickness;
    private final int sizeOfCharTexture;

    static {
        backgroundTexture = new Texture("backgroundMenu.png");
    }

    public MenuScreen() {
        backgroundHeight = backgroundTexture.getHeight();
        backgroundWidth = backgroundTexture.getWidth();
        orthographicCamera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();

        borderThickness = 32;
        sizeOfCharTexture = 55;
        cordsHero = new Coordinate(borderThickness,borderThickness);
        cordsEnemy = new Coordinate(backgroundWidth - borderThickness - sizeOfCharTexture,backgroundHeight - borderThickness - sizeOfCharTexture);
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
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        orthographicCamera.update();

        moveCharInBackground(3,cordsHero);
        moveCharInBackground(3,cordsEnemy);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture,0,0);
        spriteBatch.draw(Hero.idleTexture, cordsHero.x, cordsHero.y,sizeOfCharTexture,sizeOfCharTexture);
        spriteBatch.draw(Enemy.idleTexture, cordsEnemy.x, cordsEnemy.y,sizeOfCharTexture,sizeOfCharTexture);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void moveCharInBackground(int pixelsPerFrames,Coordinate cords) {
        correctOutOfBounds(pixelsPerFrames,cords);
        moveTextureInBoundary(pixelsPerFrames,cords);
    }

    private void correctOutOfBounds (int pixelsToMove,Coordinate cords) {
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

    private void moveTextureInBoundary(int pixelsPerFrames,Coordinate cords) {
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

    private boolean willBeOutOfBoundsRight (Coordinate cords, int pixels) {
        if (cords.x + pixels > backgroundWidth - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }

    private boolean willBeOutOfBoundsLeft (Coordinate cords, int pixels) {
        if (cords.x - pixels < borderThickness) {
            return true;
        }
        return false;
    }

    private boolean willBeOutOfBoundsUp (Coordinate cords, int pixels) {
        if (cords.y + pixels > backgroundHeight - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }

    private boolean willBeOutOfBoundsDown (Coordinate cords, int pixels) {
        if (cords.y - pixels < borderThickness) {
            return true;
        }
        return false;
    }

    private boolean isRightBoundary (Coordinate cords) {
        if (cords.x == backgroundWidth - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }

    private boolean isBottomBoundary (Coordinate cords) {
        if (cords.y == borderThickness) {
            return true;
        }
        return false;
    }

    private boolean isLeftBoundary (Coordinate cords) {
        if (cords.x == borderThickness) {
            return true;
        }
        return false;
    }

    private boolean isUpperBoundary (Coordinate cords) {
        if (cords.y == backgroundHeight - borderThickness - sizeOfCharTexture) {
            return true;
        }
        return false;
    }


    private class Coordinate {
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
