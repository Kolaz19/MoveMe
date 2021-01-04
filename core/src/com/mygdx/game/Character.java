package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    protected Animation animationMove;
    protected Animation animationIdle;
    protected Animation animationAppear;
    private Rectangle collisionBox;
    protected TextureRegion currentFrame;
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
        drawScaling = 1f;
        isAppearing = true;
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

    public void addAnimationIdle(Texture textureAtlas, int durationInFrames, int frameWidth, int frameHeight) {
        animationIdle = new Animation(textureAtlas,durationInFrames,frameWidth,frameHeight);
    }

    public void addAnimationMove(Texture textureAtlas, int durationInFrames, int frameWidth, int frameHeight) {
        animationMove = new Animation(textureAtlas,durationInFrames,frameWidth,frameHeight);
    }

    public void addAnimationAppear(Texture textureAtlas, int durationInFrames, int frameWidth, int frameHeight) {
        animationAppear = new Animation(textureAtlas,durationInFrames,frameWidth,frameHeight);
    }

    public void playIdleAnimation() {
        animationIdle.play();
        currentFrame = animationIdle.getCurrentFrame();
    }

    public void playMoveAnimation() {
        animationMove.play();
        currentFrame = animationMove.getCurrentFrame();
    }

    public void playAppearAnimation() {
        animationAppear.play();
        currentFrame = animationAppear.getCurrentFrame();
    }



    public boolean isMidAnimationMove() {
        return animationMove.isMidAnimation();
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
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
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(270);
        } else if (getDrawX() > getTargetX()) {
            setX(getDrawX() - pixelsPerFrame);
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(90);
        } else if (getDrawY() < getTargetY()) {
            setY(getDrawY() + pixelsPerFrame);
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(0);
        } else if (getDrawY() > getTargetY()) {
            setY(getDrawY() - pixelsPerFrame);
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(180);
        }
    }

    public void playAnimations() {
        if (isAppearing) {
            playAppearAnimation();
            if (!animationAppear.isMidAnimation()) {
                isAppearing = false;
                playIdleAnimation();
            }
            return;
        }


        if (isMidAnimationMove()) {
            playMoveAnimation();
        } else {
            playIdleAnimation();
            setRotation(0);
        }
    }

    public boolean isTargetSet () {
        return getTargetX() != getDrawX() || getTargetY() != getDrawY();
    }

}

