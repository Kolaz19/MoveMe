package com.mygdx.game;

public class EndScreen extends MenuScreen {
    public EndScreen(MyGdxGame maingame) {
        super(maingame);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        basicScreenRenderSetup();
        spriteBatch.begin();
        basicScreenRenderSetup();
        spriteBatch.end();
    }


}
