package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class EndScreen extends MenuScreen {
    private static Texture endTextTexture;
    private static Texture smileyTexture;
    private Animation endText;
    private Animation smiley;
    private Animation exitButton;
    private Coordinate cordsExit;
    private int currentTextFrame;
    private int currentExitFrame;
    private int frameCounter;
    private boolean isExitButtonSelected;
    private boolean isExitButtonClicked;


    static {
        endTextTexture = new Texture("endingText.png");
        smileyTexture = new Texture("endHero.png");
    }

    public EndScreen(MyGdxGame maingame) {
        super(maingame);
        endText = new Animation(endTextTexture,500,346, endTextTexture.getHeight());
        smiley = new Animation(smileyTexture,50,88, smileyTexture.getHeight());
        currentTextFrame = 0;
        frameCounter = 0;
        exitButton = new Animation(MainMenu.exitButtonTexture,500,50,50);
        cordsExit = new MenuScreen.Coordinate(367,431);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        basicScreenRenderSetup();

        smiley.play();
        setEndTextFrame();
        setExitButtonSelected();
        setExitButtonClicked();
        setCorrectExitButtonFrame();
        if (currentTextFrame == 3) {
            processExitCondition();
        }


        spriteBatch.begin();
        basicBackgroundRendering();
        spriteBatch.draw(smiley.getCurrentFrame(),backgroundWidth/2 - smiley.getCurrentFrame().getRegionWidth()/2,100);
        if (!(currentTextFrame==0)) {
            spriteBatch.draw(endText.getSpecificFrame(currentTextFrame), backgroundWidth / 2 - endText.getCurrentFrame().getRegionWidth() / 2 - 15, 230, backgroundWidth / 2, 230 - endText.getCurrentFrame().getRegionHeight() / 2, endText.getCurrentFrame().getRegionWidth(), endText.getCurrentFrame().getRegionHeight(), 0.7f, 0.7f, 0);
        }
        if (currentTextFrame == 3) {
            spriteBatch.draw(exitButton.getSpecificFrame(currentExitFrame),cordsExit.x,cordsExit.y);
        }
        spriteBatch.end();
    }

    private void setEndTextFrame() {
        if (frameCounter > 350) {
            return;
        }

        if (frameCounter == 150) {
            currentTextFrame++;
        } else if (frameCounter == 250) {
            currentTextFrame++;
        } else if (frameCounter == 350) {
            currentTextFrame++;
        }
        frameCounter++;
    }

    private void setExitButtonSelected() {
        if(mouseCoordinates.x >= cordsExit.x && mouseCoordinates.x <= cordsExit.x + exitButton.getSpecificFrame(1).getRegionWidth()
           && mouseCoordinates.y >= cordsExit.y && mouseCoordinates.y <= cordsExit.y + exitButton.getSpecificFrame(1).getRegionHeight()) {
            isExitButtonSelected = true;
        } else {
            isExitButtonSelected = false;
        }
    }

    private void setCorrectExitButtonFrame() {
        if (isExitButtonSelected) {
            currentExitFrame = 2;
        } else {
            currentExitFrame = 1;
        }
    }

    private void setExitButtonClicked() {
        if (isExitButtonSelected && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            isExitButtonClicked = true;
        } else {
            isExitButtonClicked = false;
        }
    }

    private void processExitCondition() {
        if (isExitButtonClicked) {
            mainGame.setScreen(new MainMenu(mainGame));
        }
    }

}
