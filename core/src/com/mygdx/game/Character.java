package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    public Animation mr_animationMove;
    public Animation mr_animationIdle;
    private Rectangle  mr_collisionBox;
    private TextureRegion mr_currentFrame;
    private float mv_drawX;
    private float mv_drawY;
    private float mv_targetX;
    private float mv_targetY;

    public Character(float iv_posX, float iv_posY, int iv_heightWidth) {
        mr_collisionBox = new Rectangle();
        mr_collisionBox.setHeight(iv_heightWidth);
        mr_collisionBox.setWidth(iv_heightWidth);
        mr_collisionBox.setX(iv_posX);
        mr_collisionBox.setY(iv_posY);
        mv_drawX = iv_posX;
        mv_drawY = iv_posY;
    }

    public void setX(float iv_x) {
        mr_collisionBox.setX(iv_x);
        mv_drawX = iv_x;
    }

    public void setY(float iv_y) {
        mr_collisionBox.setY(iv_y);
        mv_drawX = iv_y;
    }

    public void addIdleAnimation(String iv_pathToAtlas,int iv_durationInFrames,int iv_maxFrames) {
        mr_animationIdle = new Animation(iv_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void addAnimationMove(String iv_pathToAtlas,int iv_durationInFrames,int iv_maxFrames) {
        mr_animationMove = new Animation(iv_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void playIdleAnimation() {
        mr_animationIdle.play();
        mr_currentFrame = mr_animationIdle.getCurrentFrame();
    }

    public void playMoveAnimation() {
        mr_animationMove.play();
        mr_currentFrame = mr_animationMove.getCurrentFrame();
    }

    public boolean isMidAnimationMove() {
        return mr_animationMove.isMidAnimation();
    }

    public TextureRegion getCurrentFrame() {
        return mr_currentFrame;
    }

    public float getDrawX() {
        return mv_drawX;
    }

    public float getDrawY() {
        return mv_drawY;
    }
    //Just get input and set target position, no other check
    public void calibrateTargetPosition() {
        if (Gdx.input.isButtonPressed(Input.Keys.W) || Gdx.input.isButtonPressed(Input.Keys.UP)) {
            mv_targetY = mv_drawY + mr_collisionBox.getHeight();
        } else if (Gdx.input.isButtonPressed(Input.Keys.S) || Gdx.input.isButtonPressed(Input.Keys.DOWN)) {
            mv_targetY = mv_drawY - mr_collisionBox.getHeight();
        } else if (Gdx.input.isButtonPressed(Input.Keys.A) || Gdx.input.isButtonPressed(Input.Keys.LEFT)) {
            mv_targetX = mv_drawX + mr_collisionBox.getWidth();
        } else if (Gdx.input.isButtonPressed(Input.Keys.D) || Gdx.input.isButtonPressed(Input.Keys.RIGHT)) {
            mv_targetX = mv_drawX - mr_collisionBox.getWidth();
        }
    }
    //Checks if target position will hit map boundary - reset target position
    //TODO also play animation
    public void checkFutureMapCollision(TiledMapTileLayer ir_mapLayer) {

    }


}