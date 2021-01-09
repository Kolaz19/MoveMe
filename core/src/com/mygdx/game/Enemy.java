package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Enemy extends Character {
    public boolean willKill;
    private static Texture idleTexture;
    private static Texture moveTexture;

    static {
        idleTexture = new Texture("enemyIdle.png");
        moveTexture = new Texture ("enemyMove.png");
    }

    public Enemy(int startingCellX, int startingCellY, int heightWidth) {
        super(startingCellX, startingCellY, heightWidth);
        willKill = false;
        animationIdle = new Animation(idleTexture, 60, 14,14);
        animationMove = new Animation(moveTexture, 3, 14,14);
    }
    
    public void playAnimation() {
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
        super.playAnimation();
    }
}
