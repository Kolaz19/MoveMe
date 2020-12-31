package com.mygdx.game;

public class Enemy extends Character {
    public boolean willKill;

    public Enemy(float startPosX, float startPosY, int heightWidth) {
        super(startPosX, startPosY, heightWidth);
        willKill = false;
    }
    
    public void playAnimations() {
        if (willKill) {
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
