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
        mv_targetX = iv_posX;
        mv_targetY = iv_posY;
    }

    public void setX(float iv_x) {
        mr_collisionBox.setX(iv_x);
        mv_drawX = iv_x;
    }

    public void setY(float iv_y) {
        mr_collisionBox.setY(iv_y);
        mv_drawY = iv_y;
    }

    public void addAnimationIdle(String iv_pathToAtlas,int iv_durationInFrames,int iv_maxFrames) {
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
        //Check if char is still moving
        if((mv_targetY != mv_drawY) || (mv_targetX != mv_drawX)) {
            return;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            mv_targetY = mv_drawY + mr_collisionBox.getHeight();
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            mv_targetY = mv_drawY - mr_collisionBox.getHeight();
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mv_targetX = mv_drawX - mr_collisionBox.getWidth();
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mv_targetX = mv_drawX + mr_collisionBox.getWidth();
        }
    }
    //Checks if target position will hit map boundary - reset target position
    //TODO also play animation
    public void checkFutureMapCollision(TiledMapTileLayer ir_mapLayer) {
        int lv_cellX =(int) this.mv_targetX / ir_mapLayer.getTileWidth();
        int lv_cellY = (int) this.mv_targetY / ir_mapLayer.getTileHeight();
        TiledMapTileLayer.Cell lr_cellToHit = ir_mapLayer.getCell(lv_cellX,lv_cellY);
        if (lr_cellToHit.getTile().getProperties().containsKey("blocked") && lr_cellToHit.getTile().getProperties().get("blocked",Boolean.class).equals(true)) {
            mv_targetX = 0;
            mv_targetY = 0;
        }
    }

    public void move(float iv_speed) {
        if (getDrawX() < mv_targetX) {
            this.setX(getDrawX() + iv_speed);
        } else if (getDrawX() > mv_targetX) {
            this.setX(getDrawX() - iv_speed);
        } else if (getDrawY() < mv_targetY) {
            this.setY(getDrawY() + iv_speed);
        } else if (getDrawY() > mv_targetY) {
            this.setY(getDrawY() - iv_speed);
        }
    }


}