package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StageScreen extends MenuScreen {
    private static Texture backButtonTexture;
    private static Texture stageButtonTexture;
    private Animation backButton;
    private Coordinate cordsBack;
    private boolean isBackButtonSelected;
    private boolean isBackButtonClicked;
    private StageEntity[] stageButtons;

    static {
        backButtonTexture = new Texture("backButton.png");
        stageButtonTexture = new Texture("stageSelectButton.png");
    }

    public StageScreen(MyGdxGame maingame) {
        super(maingame);
        backButton = new Animation(backButtonTexture,100,50,50);
        cordsBack = new Coordinate(367,431);
        createStageNodes();
    }

    @Override
    public void render(float delta) {
        basicScreenRenderSetup();

        for (StageEntity button : stageButtons) {
            button.processButtonLogic();
        }


        assignBackButtonVariables();
        processBackButton();

        spriteBatch.begin();
        basicBackgroundRendering();
        for (StageEntity button : stageButtons) {
            spriteBatch.draw(button.getFrame(), button.cord.x, button.cord.y);
        }
        spriteBatch.draw(backButton.getSpecificFrame(getAnimationNumber(isBackButtonSelected)),cordsBack.x, cordsBack.y);
        spriteBatch.end();
    }

    private void processBackButton() {
        if (isBackButtonClicked) {
            mainGame.setScreen(new MainMenu(mainGame));
        }
    }

    private void createStageNodes() {
        stageButtons = new StageEntity[Savegame.getAmountOfLevels()];
        int drawX = 95;
        int drawY = 350;
        final int SPACING_X = 15;
        final int SPACING_Y = 18;
        final int AMOUNT_IN_ROW = 5;

        for (int k = 0; k < stageButtons.length; k++) {
            stageButtons[k] = new StageEntity(drawX, drawY, k + 1);
            if ((k + 1) % AMOUNT_IN_ROW == 0) {
                drawX = drawX - (stageButtons[k].getFrame().getRegionWidth() * (AMOUNT_IN_ROW - 1)) - (SPACING_X * (AMOUNT_IN_ROW - 1));
                drawY = drawY - stageButtons[k].getFrame().getRegionHeight() - SPACING_Y;
            } else {
                drawX = drawX + stageButtons[k].getFrame().getRegionWidth() + SPACING_X;
            }
        }
    }

    private void assignBackButtonVariables() {
        setBackButtonSelected();
        setBackButtonClicked();
    }

    private void setBackButtonClicked() {
        isBackButtonClicked = isBackButtonSelected && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    }

    private void setBackButtonSelected() {
            if(mouseCoordinates.x >= cordsBack.x && mouseCoordinates.x <= cordsBack.x + backButton.getSpecificFrame(1).getRegionWidth()
            && mouseCoordinates.y >= cordsBack.y && mouseCoordinates.y <= cordsBack.y + backButton.getSpecificFrame(1).getRegionHeight()) {
                isBackButtonSelected = true;
            } else {
                isBackButtonSelected = false;
            }
    }



    private class StageEntity {
        public MenuScreen.Coordinate cord;
        private Animation animation;
        private TextureRegion frame;
        private final int level;
        private boolean isLocked;
        private boolean isSelected;
        private boolean isClicked;

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

        public void processButtonLogic() {
            if (isLocked) {
                return;
            }
            setSelectedStatus();
            setClickedStatus();
            setCorrectFrame();
            if (isClicked) {
                moveToStage();
            }
        }

        private void moveToStage() {
            mainGame.chooseLevel(level);
        }

        private void setCorrectFrame() {
            if (isSelected) {
                frame = animation.getSpecificFrame(3);
            } else {
                frame = animation.getSpecificFrame(1);
            }
        }

        private void setClickedStatus() {
            isClicked = isSelected && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        }

        private void setSelectedStatus() {
            if (mouseCoordinates.x >= cord.x && mouseCoordinates.x <= cord.x + frame.getRegionWidth()
             && mouseCoordinates.y >= cord.y && mouseCoordinates.y <= cord.y + frame.getRegionHeight()) {
                isSelected =  true;
            } else {
                isSelected = false;
            }
        }

    }

}

