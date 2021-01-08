package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Character {
    protected Animation animationMove;
    protected Animation animationIdle;
    protected ArrayList<Animation> animationsFaceIdle;
    private Rectangle collisionBox;
    protected TextureRegion currentFrame;
    protected  TextureRegion currentFace;
    private float drawX;
    private float drawY;
    private float targetX;
    private float targetY;
    private float rotationDegree;
    private float drawScaling;
    public boolean isAppearing;


    public Character(int startingCellX, int startingCellY, int heightWidth) {
        int positionCellX = (startingCellX-1) * 16 + 1;
        int positionCellY = (startingCellY-1) * 16 + 1;
        collisionBox = new Rectangle();
        collisionBox.setHeight(heightWidth);
        collisionBox.setWidth(heightWidth);
        collisionBox.setX(positionCellX);
        collisionBox.setY(positionCellY);
        drawX = positionCellX;
        drawY = positionCellY;
        targetX = positionCellX;
        targetY = positionCellY;
        rotationDegree = 0;
        drawScaling = 6f;
        isAppearing = true;
        animationsFaceIdle = new ArrayList<>();
    }

    public void setScaling(float scaling) {
        drawScaling = scaling;
    }
    public float getScaling() {
        return drawScaling;
    }
    public float getWidth() {
        return collisionBox.getWidth();
    }
    public float getHeight() {
        return collisionBox.getHeight();
    }
    public void setX(float posX) {
        collisionBox.setX(posX);
        drawX = posX;
    }
    public float getDrawX() {
        return drawX;
    }
    public void setY(float posY) {
        collisionBox.setY(posY);
        drawY = posY;
    }
    public float getDrawY() {
        return drawY;
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public float getTargetX() {
        return targetX;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getRotation() {
        return rotationDegree;
    }
    public void setRotation(float rotationInDegrees) {
        rotationDegree = rotationInDegrees;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }
    public TextureRegion getCurrentFace() {
        return currentFace;
    }


    //Get input and set target position - Negative importValue means move in reverse direction
    public void calibrateTargetPosition(int pixelsToMove) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            setTargetY(getDrawY() + pixelsToMove);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            setTargetY(getDrawY() - pixelsToMove);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            setTargetX(getDrawX() - pixelsToMove);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            setTargetX(getDrawX() + pixelsToMove);
        }
    }

    //Checks if target position will hit map boundary - reset target position
    public void checkFutureMapCollision(TiledMapTileLayer mapLayer) {
        //first check if target is out of map
        if (getTargetX() < 0 || getTargetX() > mapLayer.getTileWidth() * mapLayer.getWidth() || getTargetY() < 0 || getTargetY() > mapLayer.getTileHeight() * mapLayer.getHeight()) {
            setTargetX(getDrawX());
            setTargetY(getDrawY());
            return;
        }
        //then check if target would hit blocked tile
        int cellX =(int) getTargetX() / mapLayer.getTileWidth();
        int cellY = (int) getTargetY() / mapLayer.getTileHeight();
        TiledMapTileLayer.Cell cellToHit = mapLayer.getCell(cellX,cellY);
        if (cellToHit.getTile().getProperties().containsKey("blocked") && cellToHit.getTile().getProperties().get("blocked",Boolean.class).equals(true)) {
            setTargetX(getDrawX());
            setTargetY(getDrawY());
        }
    }


    public void move(float pixelsPerFrame) {
        //No need to move if target and drawing coordinates are the same
        if (!isTargetSet()) {
            return;
        }
        //Snap to target coordinate if near enough
        if (getTargetX() > getDrawX() && getTargetX() - getDrawX() < pixelsPerFrame) {
            setX(getTargetX());
        } else if (getTargetX() < getDrawX() && getDrawX() - getTargetX() < pixelsPerFrame) {
            setX(getTargetX());
        } else if (getTargetY() > getDrawY() && getTargetY() - getDrawY() < pixelsPerFrame) {
            setY(getTargetY());
        } else if (getTargetY() < getDrawY() && getDrawY() - getTargetY() < pixelsPerFrame) {
            setY(getTargetY());
        }
        //Move with consistent speed
        if (getDrawX() < getTargetX()) {
            setX(getDrawX() + pixelsPerFrame);
            if (!animationMove.isMidAnimation()) {
                animationMove.play();
                currentFrame = animationMove.getCurrentFrame();
            }
            setRotation(270);
        } else if (getDrawX() > getTargetX()) {
            setX(getDrawX() - pixelsPerFrame);
            if (!animationMove.isMidAnimation()) {
                animationMove.play();
                currentFrame = animationMove.getCurrentFrame();
            }
            setRotation(90);
        } else if (getDrawY() < getTargetY()) {
            setY(getDrawY() + pixelsPerFrame);
            if (!animationMove.isMidAnimation()) {
                animationMove.play();
                currentFrame = animationMove.getCurrentFrame();
            }
            setRotation(0);
        } else if (getDrawY() > getTargetY()) {
            setY(getDrawY() - pixelsPerFrame);
            if (!animationMove.isMidAnimation()) {
                animationMove.play();
                currentFrame = animationMove.getCurrentFrame();
            }
            setRotation(180);
        }
    }

    public void playFaceAnimation() {
        if (isTargetSet()) {
            return;
        }
        //Check if one animation is still running -> play it till end
        for (int k = 1; k < animationsFaceIdle.size(); k++) {
            if (animationsFaceIdle.get(k).isMidAnimation()) {
                animationsFaceIdle.get(k).play();
                currentFace = animationsFaceIdle.get(k).getCurrentFrame();
                return;
            }
        }

        Random randomGenerator = new Random();
        //Play other face animation with 1/300 chance per frame (framerate 60)
        if (randomGenerator.nextInt(300 + 1) == 300) {
            //Choose one of the other face animations with random number
            int randomFaceNumber = randomGenerator.nextInt(animationsFaceIdle.size());
            animationsFaceIdle.get(randomFaceNumber).play();
            currentFace = animationsFaceIdle.get(randomFaceNumber).getCurrentFrame();
        } else {
            animationsFaceIdle.get(0).play();
            currentFace = animationsFaceIdle.get(0).getCurrentFrame();
        }
    }

    public void playAnimation() {
        if (isAppearing) {
            if (getScaling() > 1f) {
                setScaling(getScaling() - 0.05f);
            } else {
                setScaling(1f);
                isAppearing = false;
            }
        }

        if (animationMove.isMidAnimation()) {
            animationMove.play();
            currentFrame = animationMove.getCurrentFrame();
        } else {
            animationIdle.play();
            currentFrame = animationIdle.getCurrentFrame();
            setRotation(0);
        }
    }

    public boolean isTargetSet () {
        return getTargetX() != getDrawX() || getTargetY() != getDrawY();
    }

}

