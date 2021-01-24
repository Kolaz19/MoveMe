package com.mygdx.game;

public class StageScreen extends MenuScreen {
    public StageScreen(MyGdxGame maingame) {
        super(maingame);
    }

    @Override
    public void render(float delta) {
        basicScreenRenderSetup();
        spriteBatch.begin();
        basicBackgroundRendering();
        spriteBatch.end();
    }
}
