package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;


public class MainMenu extends MenuScreen {
    static Texture playButtonTexture;
    static Texture stageButtonTexture;
    static Texture exitButtonTexture;
    private Animation playButton;
    private Animation stageSelectButton;
    private Animation exitButton;
    protected MainMenu.Coordinate cordsExit, cordsPlay, cordsStage;
    private boolean isPlayButtonSelected, isStageButtonSelected, isExitButtonSelected;
    private MenuScreen.BUTTON buttonCurrentlyClicked;

    static {
        playButtonTexture = new Texture("playButton.png");
        stageButtonTexture = new Texture("stageButton.png");
        exitButtonTexture = new Texture("exitButton.png");
    }


    public MainMenu(MyGdxGame maingame) {
        super(maingame);
        playButton = new Animation(playButtonTexture,1,300,100);
        stageSelectButton = new Animation(stageButtonTexture,1,300,100);
        exitButton = new Animation(exitButtonTexture,1,50,50);

        cordsPlay = new MenuScreen.Coordinate((backgroundWidth / 2) - (playButton.getSpecificFrame(1).getRegionWidth() / 2),270);
        cordsStage = new MenuScreen.Coordinate((backgroundWidth / 2) - (stageSelectButton.getSpecificFrame(1).getRegionWidth() / 2),120);
        cordsExit = new MenuScreen.Coordinate(367,431);
    }

    public void render(float delta) {
        basicScreenRenderSetup();

        assignButtonVariables();
        processButtonLogic();

        spriteBatch.begin();
        basicBackgroundRendering();
        spriteBatch.draw(playButton.getSpecificFrame(getAnimationNumber(isPlayButtonSelected)),cordsPlay.x, cordsPlay.y);
        spriteBatch.draw(stageSelectButton.getSpecificFrame(getAnimationNumber(isStageButtonSelected)),cordsStage.x, cordsStage.y);
        spriteBatch.draw(exitButton.getSpecificFrame(getAnimationNumber(isExitButtonSelected)),cordsExit.x, cordsExit.y);
        spriteBatch.end();
    }

    private void processButtonLogic() {
        switch (buttonCurrentlyClicked) {
            case PLAY: mainGame.chooseLevel(Savegame.getCurrentLevel());
                break;
            case STAGE: mainGame.setScreen(new StageScreen(mainGame));
                break;
            case EXIT:
                break;
        }
    }

    void assignButtonVariables() {
        chooseSelectedButton();
        //chooseClickedButton depends on chooseSelectedButton above
        chooseClickedButton();
    }

    private void chooseClickedButton() {
        buttonCurrentlyClicked = MenuScreen.BUTTON.NOSELECT;
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            return;
        }
        if (isPlayButtonSelected)  {
            buttonCurrentlyClicked = MenuScreen.BUTTON.PLAY;
        } else if (isStageButtonSelected) {
            buttonCurrentlyClicked = MenuScreen.BUTTON.STAGE;
        } else if (isExitButtonSelected) {
            buttonCurrentlyClicked = MenuScreen.BUTTON.EXIT;
        }
    }

    private void chooseSelectedButton () {
        isPlayButtonSelected = false;
        isStageButtonSelected = false;
        isExitButtonSelected = false;
        if (isPlayButtonSelected()) {
            isPlayButtonSelected = true;
        } else if (isStageButtonSelected()) {
            isStageButtonSelected = true;
        } else if (isExitButtonSelected()) {
            isExitButtonSelected = true;
        }
    }

    private boolean isPlayButtonSelected () {
        if(mouseCoordinates.x >= cordsPlay.x && mouseCoordinates.x <= cordsPlay.x + playButton.getSpecificFrame(1).getRegionWidth()
                && mouseCoordinates.y >= cordsPlay.y && mouseCoordinates.y <= cordsPlay.y + playButton.getSpecificFrame(1).getRegionHeight()) {
            return true;
        }
        return false;
    }

    private boolean isStageButtonSelected() {
        if(mouseCoordinates.x >= cordsStage.x && mouseCoordinates.x <= cordsStage.x + stageSelectButton.getSpecificFrame(1).getRegionWidth()
                && mouseCoordinates.y >= cordsStage.y && mouseCoordinates.y <= cordsStage.y + stageSelectButton.getSpecificFrame(1).getRegionHeight()) {
            return true;
        }
        return false;
    }

    private boolean isExitButtonSelected() {
        if(mouseCoordinates.x >= cordsExit.x && mouseCoordinates.x <= cordsExit.x + exitButton.getSpecificFrame(1).getRegionWidth()
                && mouseCoordinates.y >= cordsExit.y && mouseCoordinates.y <= cordsExit.y + exitButton.getSpecificFrame(1).getRegionHeight()) {
            return true;
        }
        return false;
    }
}


