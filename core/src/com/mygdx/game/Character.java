package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    private Animation mr_animationMove;
    private Animation mr_animationIdle;
    private Animation mr_animationExpl;
    private Rectangle  mr_collisionBox;
    private TextureRegion mr_currentFrame;
    private float mv_drawX;
    private float mv_drawY;
    private float mv_targetX;
    private float mv_targetY;
    private float mv_rotation;
    private float mv_scaling;

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
        mv_rotation = 0;
        mv_scaling = 1f;
    }

    public void setScaling(float iv_scaling) {
        mv_scaling = iv_scaling;
    }
    public float getScaling() {
        return mv_scaling;
    }

    public float getWidth() {
        return mr_collisionBox.getWidth();
    }
    public float getHeight() {
        return mr_collisionBox.getHeight();
    }
    public void setX(float iv_x) {
        mr_collisionBox.setX(iv_x);
        mv_drawX = iv_x;
    }
    public float getDrawX() {
        return mv_drawX;
    }
    public void setY(float iv_y) {
        mr_collisionBox.setY(iv_y);
        mv_drawY = iv_y;
    }
    public float getDrawY() {
        return mv_drawY;
    }

    public void setTargetX(float iv_targetX) {
        mv_targetX = iv_targetX;
    }

    public float getTargetX() {
        return mv_targetX;
    }

    public void setTargetY(float iv_targetY) {
        mv_targetY = iv_targetY;
    }

    public float getTargetY() {
        return mv_targetY;
    }

    public float getRotation() {
        return mv_rotation;
    }
    public void setRotation(float iv_rotation) {
        mv_rotation = iv_rotation;
    }

    public void addAnimationIdle(Texture ir_pathToAtlas, int iv_durationInFrames, int iv_maxFrames) {
        mr_animationIdle = new Animation(ir_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void addAnimationExplode(Texture ir_pathToAtlas, int iv_durationInFrames, int iv_maxFrames) {
        mr_animationExpl = new Animation(ir_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void addAnimationMove(Texture ir_pathToAtlas,int iv_durationInFrames,int iv_maxFrames) {
        mr_animationMove = new Animation(ir_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void playIdleAnimation() {
        mr_animationIdle.play();
        mr_currentFrame = mr_animationIdle.getCurrentFrame();
    }

    public void playMoveAnimation() {
        mr_animationMove.play();
        mr_currentFrame = mr_animationMove.getCurrentFrame();
    }

    public void playDeathAnimation() {
        mr_animationExpl.play();
        mr_currentFrame = mr_animationExpl.getCurrentFrame();
    }

    public boolean isMidAnimationMove() {
        return mr_animationMove.isMidAnimation();
    }

    public boolean isMidAnimationDeath() {
        return mr_animationExpl.isMidAnimation();
    }

    public TextureRegion getCurrentFrame() {
        return mr_currentFrame;
    }

    //Checks if target position will hit map boundary - reset target position
    public void checkFutureMapCollision(TiledMapTileLayer ir_mapLayer) {
        //first check if target is out of map
        if (getTargetX() < 0 || getTargetX() > ir_mapLayer.getTileWidth() * ir_mapLayer.getWidth() || getTargetY() < 0 || getTargetY() > ir_mapLayer.getTileHeight() * ir_mapLayer.getHeight()) {
            setTargetX(getDrawX());
            setTargetY(getDrawY());
            return;
        }
        //then check if target would hit blocked tile
        int lv_cellX =(int) getTargetX() / ir_mapLayer.getTileWidth();
        int lv_cellY = (int) getTargetY() / ir_mapLayer.getTileHeight();
        TiledMapTileLayer.Cell lr_cellToHit = ir_mapLayer.getCell(lv_cellX,lv_cellY);
        if (lr_cellToHit.getTile().getProperties().containsKey("blocked") && lr_cellToHit.getTile().getProperties().get("blocked",Boolean.class).equals(true)) {
            setTargetX(getDrawX());
            setTargetY(getDrawY());
        }
    }


    public void move(float iv_speed) {
        //No need to move if target and drawing coordinates are the same
        if (!isTargetSet()) {
            return;
        }
        //Snap to target coordinate if near enough
        if (getTargetX() > getDrawX() && getTargetX() - getDrawX() < iv_speed) {
            setX(getTargetX());
        } else if (getTargetX() < getDrawX() && getDrawX() - getTargetX() < iv_speed) {
            setX(getTargetX());
        } else if (getTargetY() > getDrawY() && getTargetY() - getDrawY() < iv_speed) {
            setY(getTargetY());
        } else if (getTargetY() < getDrawY() && getDrawY() - getTargetY() < iv_speed) {
            setY(getTargetY());
        }
        //Move with consistent speed
        if (getDrawX() < getTargetX()) {
            setX(getDrawX() + iv_speed);
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(270);
        } else if (getDrawX() > getTargetX()) {
            setX(getDrawX() - iv_speed);
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(90);
        } else if (getDrawY() < getTargetY()) {
            setY(getDrawY() + iv_speed);
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(0);
        } else if (getDrawY() > getTargetY()) {
            setY(getDrawY() - iv_speed);
            if (!isMidAnimationMove()) {
                playMoveAnimation();
            }
            setRotation(180);
        }
    }

    public void playAnimations() {
        if (isMidAnimationMove()) {
            playMoveAnimation();
        } else {
            playIdleAnimation();
            setRotation(0);
        }
    }

    public boolean isTargetSet () {
        if (getTargetX() != getDrawX() || getTargetY() != getDrawY()) {
            return true;
        } else {
            return false;
        }
    }

}

