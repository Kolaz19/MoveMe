package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Enemy extends Character {
    public Enemy(float iv_posX, float iv_posY, int iv_heightWidth) {
        super(iv_posX, iv_posY, iv_heightWidth);
    }

    public void calibrateTargetPosition() {
        //Check if char is still moving
        if((mv_targetY != getDrawY()) || (mv_targetX != getDrawX())) {
            return;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            mv_targetY = getDrawY() - 16;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            mv_targetY = getDrawY() + 16;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mv_targetX = getDrawX() + 16;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mv_targetX = getDrawX() - 16;
        }
    }
}
