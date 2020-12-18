package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Enemy extends Character {
    public boolean mv_willKill;
    public Enemy(float iv_posX, float iv_posY, int iv_heightWidth) {
        super(iv_posX, iv_posY, iv_heightWidth);
        mv_willKill = false;
    }

    public void calibrateTargetPosition() {

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            setTargetY(getDrawY() - 16);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            setTargetY(getDrawY() + 16);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            setTargetX(getDrawX() + 16);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            setTargetX(getDrawX() - 16);
        }
    }

    public void playAnimations() {
        if (mv_willKill) {
            if (getTargetX() == getDrawX() && getTargetY() == getDrawY()) {
                if (getScaling() > 1f) {
                    setScaling(getScaling() - 0.10f);
                } else {
                    setScaling(1f);
                }
            } else {
                setScaling(getScaling() + 0.04f);
            }
        }
        super.playAnimations();
    }
}
