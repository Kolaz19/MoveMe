package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.graalvm.compiler.phases.common.NodeCounterPhase;

public class StageScreen extends MenuScreen {
    private static Texture backButtonTexture;
    private static Texture stageButtonTexture;
    private StageEntity[] stageButtons;

    static {
        backButtonTexture = new Texture("backButton.png");
        stageButtonTexture = new Texture("stageSelectButton.png");
    }

    public StageScreen(MyGdxGame maingame) {
        super(maingame);
        stageButtons = new StageEntity[Savegame.getAmountOfLevels()];

        int drawX = 95;
        int drawY = 350;
        int spacingX = 15;
        int spacingY = 18;
        int amountInARow = 5;
        for (int k = 0; k < stageButtons.length; k++) {
                stageButtons[k] = new StageEntity(drawX, drawY, k + 1);
                if ((k + 1) % amountInARow == 0) {
                    drawX = drawX - (stageButtons[k].getFrame().getRegionWidth() * (amountInARow - 1)) - (spacingX * (amountInARow - 1));
                    drawY = drawY - stageButtons[k].getFrame().getRegionHeight() - spacingY;
                } else {
                    drawX = drawX + stageButtons[k].getFrame().getRegionWidth() + spacingX;
                }
        }

    }

    @Override
    public void render(float delta) {
        basicScreenRenderSetup();

        for (StageEntity button : stageButtons) {
            button.setCorrectFrame();
        }

        spriteBatch.begin();
        basicBackgroundRendering();
        for (StageEntity button : stageButtons) {
            spriteBatch.draw(button.getFrame(), button.cord.x, button.cord.y);
        }
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
            if (Savegame.isLevelUnlocked(level)) {
                isLocked = false;
                frame = animation.getSpecificFrame(3);
            } else {
                isLocked = true;
                frame = animation.getSpecificFrame(2);
            }
        }

        public TextureRegion getFrame() {
            return frame;
        }

        public void setCorrectFrame() {
            if (isLocked) {
                return;
            }
            if (isButtonSelected()) {
                frame = animation.getSpecificFrame(3);
            } else {
                frame = animation.getSpecificFrame(1);
            }
        }

        private boolean isButtonSelected() {
            if (mouseCoordinates.x >= cord.x && mouseCoordinates.x <= cord.x + frame.getRegionWidth()
             && mouseCoordinates.y >= cord.y && mouseCoordinates.y <= cord.y + frame.getRegionHeight()) {
                return true;
            } else {
                return false;
            }
        }

    }

}

