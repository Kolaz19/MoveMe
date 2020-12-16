package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    public Animation mr_animationMove;
    public Animation mr_animationIdle;
    private Animation mr_animationExpl;
    private Rectangle  mr_collisionBox;
    private TextureRegion mr_currentFrame;
    private float mv_drawX;
    private float mv_drawY;
    public float mv_targetX;
    public float mv_targetY;
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

    public void changeScaling(float iv_scaling) {
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

    public void setY(float iv_y) {
        mr_collisionBox.setY(iv_y);
        mv_drawY = iv_y;
    }

    public float getRotation() {
        return mv_rotation;
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

    public float getDrawX() {
        return mv_drawX;
    }

    public float getDrawY() {
        return mv_drawY;
    }


    //Just get input and set target position, no other check
    public boolean calibrateTargetPosition() {
        //Check if char is still moving
        if((mv_targetY != mv_drawY) || (mv_targetX != mv_drawX)) {
            return false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            mv_targetY = getDrawY() + 16;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            mv_targetY = getDrawY() - 16;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mv_targetX = getDrawX() - 16;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mv_targetX = getDrawX() + 16;
        }
        return true;
    }
    //Checks if target position will hit map boundary - reset target position
    //TODO also play animation
    public void checkFutureMapCollision(TiledMapTileLayer ir_mapLayer) {
        //first check if target is out of map
        if (mv_targetX < 0 || mv_targetX > ir_mapLayer.getTileWidth() * ir_mapLayer.getWidth() || mv_targetY < 0 || mv_targetY > ir_mapLayer.getTileHeight() * ir_mapLayer.getHeight()) {
            mv_targetX = getDrawX();
            mv_targetY = getDrawY();
            return;
        }
        //then check if target would hit blocked tile
        int lv_cellX =(int) this.mv_targetX / ir_mapLayer.getTileWidth();
        int lv_cellY = (int) this.mv_targetY / ir_mapLayer.getTileHeight();
        TiledMapTileLayer.Cell lr_cellToHit = ir_mapLayer.getCell(lv_cellX,lv_cellY);
        if (lr_cellToHit.getTile().getProperties().containsKey("blocked") && lr_cellToHit.getTile().getProperties().get("blocked",Boolean.class).equals(true)) {
            mv_targetX = getDrawX();
            mv_targetY = getDrawY();
        }
    }



    //Has to be called after EVERY other check
    public boolean willDie(Enemy[] ia_enemies) {
        for (int lv_b = 0; lv_b < ia_enemies.length; lv_b++) {
            if (this.mv_targetX == ia_enemies[lv_b].mv_targetX && this.mv_targetY == ia_enemies[lv_b].mv_targetY) {
                //Increase size of enemy until position reached (devour animation) - then shrink it
                if (ia_enemies[lv_b].mv_targetX == ia_enemies[lv_b].getDrawX() && ia_enemies[lv_b].mv_targetY == ia_enemies[lv_b].getDrawY()) {
                    if (ia_enemies[lv_b].getScaling()>1f) {
                        ia_enemies[lv_b].changeScaling(ia_enemies[lv_b].getScaling() - 0.10f);
                    } else {
                        ia_enemies[lv_b].changeScaling(1f);
                    }

                } else {
                    ia_enemies[lv_b].changeScaling(ia_enemies[lv_b].getScaling()+0.05f);
                }
                return true;
            }
        }
        return false;
    }

    public void move(float iv_speed) {
        //No need to move if target and drawing coordinates are the same
        if (mv_targetX == getDrawX() && mv_targetY == getDrawY()) {
            return;
        }
        //Snap to target coordinate if near enough
        if (mv_targetX > getDrawX() && mv_targetX - getDrawX() < iv_speed) {
            setX(mv_targetX);
        } else if (mv_targetX < getDrawX() && getDrawX() - mv_targetX < iv_speed) {
            setX(mv_targetX);
        } else if (mv_targetY > getDrawY() && mv_targetY - getDrawY() < iv_speed) {
            setY(mv_targetY);
        } else if (mv_targetY < getDrawY() && getDrawY() - mv_targetY < iv_speed) {
            setY(mv_targetY);
        }
        //Move with consistent speed
        if (getDrawX() < mv_targetX) {
            this.setX(getDrawX() + iv_speed);
            if (!this.isMidAnimationMove()) {
                this.playMoveAnimation();
            }
            mv_rotation = 270;
        } else if (getDrawX() > mv_targetX) {
            this.setX(getDrawX() - iv_speed);
            if (!this.isMidAnimationMove()) {
                this.playMoveAnimation();
            }
            mv_rotation = 90;
        } else if (getDrawY() < mv_targetY) {
            this.setY(getDrawY() + iv_speed);
            if (!this.isMidAnimationMove()) {
                this.playMoveAnimation();
            }
            mv_rotation = 0;
        } else if (getDrawY() > mv_targetY) {
            this.setY(getDrawY() - iv_speed);
            if (!this.isMidAnimationMove()) {
                this.playMoveAnimation();
            }
            mv_rotation = 180;
        }
    }

    public void playAnimations(boolean iv_deathCondition, boolean iv_winCondition) {
        if (iv_deathCondition && this.mv_targetY == this.getDrawY() && this.mv_targetX == this.getDrawX()) {
            if (this.getScaling() != 3f) {
                playDeathAnimation();
                this.changeScaling(3f);
                return;
            } else {
                if (this.isMidAnimationDeath()) {
                    this.playDeathAnimation();
                    return;
                }
            }
        }
        //This is after char was crushed
        if (getScaling() == 3f) {
            return;
        }
        if (this.isMidAnimationMove()) {
            this.playMoveAnimation();
        } else {
            playIdleAnimation();
            mv_rotation = 0;
        }
    }

}


//TODO extract hero stuff in hero class and let Character be the parent class