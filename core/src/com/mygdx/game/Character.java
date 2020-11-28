package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Character {
    public Animation mr_animationHorizontal;
    public Animation mr_animationVertically;
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
        mr_collisionBox.setY(iv_y + mr_collisionBox.getY() - mv_drawY);
        mv_drawX = iv_y;
    }

    public void playIdleAnimation() {

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