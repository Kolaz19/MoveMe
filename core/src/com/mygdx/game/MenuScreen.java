package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen extends ScreenAdapter {
    static Texture backgroundTexture;
    private OrthographicCamera orthographicCamera;
    private final int backgroundHeight;
    private final int backgroundWidth;
    private SpriteBatch spriteBatch;

    static {
        backgroundTexture = new Texture("backgroundMenu.png");
    }

    public MenuScreen() {
        backgroundHeight = backgroundTexture.getHeight();
        backgroundWidth = backgroundTexture.getWidth();
        orthographicCamera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();
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
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        orthographicCamera.update();

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture,0,0);
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
    }

}
