package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Hero extends Character {
    public Hero(float iv_posX, float iv_posY, int iv_heightWidth) {
        super(iv_posX, iv_posY, iv_heightWidth);
    }

    //Just get input and set target position, no other check
    public boolean calibrateTargetPosition() {
        //Check if char is still moving
        if(getTargetY() != getDrawY() || (getTargetX() != getDrawX())) {
            return false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            setTargetY(getDrawY() + 16);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            setTargetY(getDrawY() - 16);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            setTargetX(getDrawX() - 16);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            setTargetX(getDrawX() + 16);
        }
        return true;
    }

    public void playAnimations (boolean iv_deathCondition, boolean iv_winCondition) {
        if (iv_deathCondition && getTargetY() == getDrawY() && getTargetX() == getDrawX()) {
            if (getScaling() != 3f) {
                playDeathAnimation();
                setScaling(3f);
                return;
            } else {
                if (isMidAnimationDeath()) {
                    playDeathAnimation();
                    return;
                }
            }
        }
        //This is after char was crushed
        if (getScaling() == 3f) {
            return;
        }
        playAnimations();
    }

}
