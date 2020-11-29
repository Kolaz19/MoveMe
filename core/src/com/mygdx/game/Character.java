package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    public Animation mr_animationHorizontal;
    public Animation mr_animationVertically;
    public Animation mr_animationIdle;
    private Rectangle  mr_collisionBox;
    private TextureRegion mr_currentFrame;
    private float mv_drawX;
    private float mv_drawY;

    public Character(int iv_posX, int iv_posY, int iv_heightWidth) {
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

    public void addVerticalAnimation(String iv_pathToAtlas,int iv_durationInFrames,int iv_maxFrames) {
        mr_animationVertically = new Animation(iv_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void addHorizontalAnimation(String iv_pathToAtlas,int iv_durationInFrames,int iv_maxFrames) {
        mr_animationHorizontal = new Animation(iv_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void playIdleAnimation() {
        mr_animationIdle.play();
        mr_currentFrame = mr_animationIdle.getCurrentFrame();
    }

    public void playVerticalAnimation() {
        mr_animationVertically.play();
        mr_currentFrame = mr_animationIdle.getCurrentFrame();
    }

    public boolean isMidAnimationVertical() {
        return mr_animationVertically.isMidAnimation();
    }

    public boolean isMidAnimationHorizontal() {
        return mr_animationHorizontal.isMidAnimation();
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




}