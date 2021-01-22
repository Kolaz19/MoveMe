package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen extends ScreenAdapter {
    static Texture backgroundTexture;
    static Texture playButtonTexture;
    static Texture stageButtonTexture;
    static Texture exitButtonTexture;
    private OrthographicCamera orthographicCamera;
    private final int backgroundHeight;
    private final int backgroundWidth;
    private SpriteBatch spriteBatch;
    private Coordinate cordsHero, cordsEnemy, cordsExit, cordsPlay, cordsStage;
    private final int borderThickness;
    private final int sizeOfCharTexture;
    private Animation playButton;
    private Animation stageButton;
    private Animation exitButton;
    private boolean isPlayButtonSelected, isStageButtonSelected, isExitButtonSelected;
    private BUTTON buttonCurrentlyClicked;
    private Vector3 mouseCoordinates;
    private MyGdxGame mainGame;

    private enum BUTTON {
        EXIT,
        PLAY,
        STAGE,
        NOSELECT
    }


    static {
        backgroundTexture = new Texture("backgroundMenu.png");
        playButtonTexture = new Texture("playButton.png");
        stageButtonTexture = new Texture("stageButton.png");
        exitButtonTexture = new Texture("exitButton.png");
    }

    public MenuScreen(MyGdxGame maingame) {
        this.mainGame = maingame;
        backgroundHeight = backgroundTexture.getHeight();
        backgroundWidth = backgroundTexture.getWidth();
        orthographicCamera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();

        borderThickness = 32;
        sizeOfCharTexture = 55;

        playButton = new Animation(playButtonTexture,1,300,100);
        stageButton = new Animation(stageButtonTexture,1,300,100);
        exitButton = new Animation(exitButtonTexture,1,50,50);

        cordsHero = new Coordinate(borderThickness,borderThickness);
        cordsEnemy = new Coordinate(backgroundWidth - borderThickness - sizeOfCharTexture,backgroundHeight - borderThickness - sizeOfCharTexture);
        cordsPlay = new Coordinate((backgroundWidth / 2) - (playButton.getSpecificFrame(1).getRegionWidth() / 2),270);
        cordsStage = new Coordinate((backgroundWidth / 2) - (stageButton.getSpecificFrame(1).getRegionWidth() / 2),120);
        cordsExit = new Coordinate(367,431);

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

        buttonCurrentlyClicked = BUTTON.NOSELECT;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        orthographicCamera.update();

        moveCharInBackground(3,cordsHero);
        moveCharInBackground(3,cordsEnemy);
        assignButtonVariables();

        processButtonLogic();

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture,0,0);
        spriteBatch.draw(Hero.idleTexture, cordsHero.x, cordsHero.y,sizeOfCharTexture,sizeOfCharTexture);
        spriteBatch.draw(Enemy.idleTexture, cordsEnemy.x, cordsEnemy.y,sizeOfCharTexture,sizeOfCharTexture);
        spriteBatch.draw(playButton.getSpecificFrame(getAnimationNumber(isPlayButtonSelected)),cordsPlay.x, cordsPlay.y);
        spriteBatch.draw(stageButton.getSpecificFrame(getAnimationNumber(isStageButtonSelected)),cordsStage.x, cordsStage.y);
        spriteBatch.draw(exitButton.getSpecificFrame(getAnimationNumber(isExitButtonSelected)),cordsExit.x, cordsExit.y);
        spriteBatch.end();
    }

    private void processButtonLogic() {
        switch (buttonCurrentlyClicked) {
            case PLAY: mainGame.chooseLevel(Savegame.getNextLevel());
                break;
        }
    }

    private int getAnimationNumber(boolean isButtonSelected) {
        if (isButtonSelected) {
            return 2;
        } else {
            return 1;
        }
    }

    private void assignButtonVariables() {
        setMouseCoordinates();
        chooseSelectedButton();
        //chooseClickedButton depends on chooseSelectedButton above
        chooseClickedButton();
    }

    private void chooseClickedButton() {
        buttonCurrentlyClicked = BUTTON.NOSELECT;
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            return;
        }
        if (isPlayButtonSelected)  {
            buttonCurrentlyClicked = BUTTON.PLAY;
        } else if (isStageButtonSelected) {
            buttonCurrentlyClicked = BUTTON.STAGE;
        } else if (isExitButtonSelected) {
            buttonCurrentlyClicked = BUTTON.EXIT;
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
        if(mouseCoordinates.x >= cordsStage.x && mouseCoordinates.x <= cordsStage.x + stageButton.getSpecificFrame(1).getRegionWidth()
        && mouseCoordinates.y >= cordsStage.y && mouseCoordinates.y <= cordsStage.y + stageButton.getSpecificFrame(1).getRegionHeight()) {
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

    private void setMouseCoordinates () {
        mouseCoordinates.x = Gdx.input.getX();
        mouseCoordinates.y = Gdx.input.getY();
        orthographicCamera.unproject(mouseCoordinates);
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
