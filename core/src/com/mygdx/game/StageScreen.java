package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StageScreen extends MenuScreen {
    private static Texture backButtonTexture;
    private static Texture stageButtonTexture;

    static {
        backButtonTexture = new Texture("backButton.png");
        stageButtonTexture = new Texture("stageSelectButton.png");
    }

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


    private class StageEntity {
        public MenuScreen.Coordinate cord;
        private Animation animation;
        private TextureRegion frame;
        private final int level;
        private boolean isLocked;

        public StageEntity(int x, int y, int level) {
            cord = new Coordinate(x ,y);
            this.level = level;
            animation = new Animation(stageButtonTexture,500,40,40);
            frame = animation.getSpecificFrame(1);
            if (Savegame.isLevelUnlocked(level)) {
                isLocked = false;
            } else {
                isLocked = true;
            }
        }

        public TextureRegion getFrame() {
            return frame;
        }
    }

}

