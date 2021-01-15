package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Enemy extends Character {
    public boolean willKill;
    public static Texture idleTexture;
    private static Texture moveTexture;
    private static ArrayList<Texture> idleFaceTextures;
    private static Texture moveFaceTextureUp;
    private static Texture moveFaceTextureDown;
    private static Texture moveFaceTextureLeftRight;

    static {
        idleTexture = new Texture("enemyIdle.png");
        moveTexture = new Texture ("enemyMove.png");
        idleFaceTextures = new ArrayList<>();
        idleFaceTextures.add(new Texture("EDefaultFace1.png"));
        idleFaceTextures.add(new Texture("EDefaultFace2.png"));
        idleFaceTextures.add(new Texture("EDefaultFace3.png"));
        idleFaceTextures.add(new Texture("EDefaultFace4.png"));
        idleFaceTextures.add(new Texture("EDefaultFace5.png"));
        idleFaceTextures.add(new Texture("EDefaultFace6.png"));
        idleFaceTextures.add(new Texture("EDefaultFace7.png"));
        idleFaceTextures.add(new Texture("EDefaultFace8.png"));
        moveFaceTextureUp = new Texture("EFaceMoveUp.png");
        moveFaceTextureDown = new Texture("EFaceMoveDown.png");
        moveFaceTextureLeftRight = new Texture("EFaceMoveRightLeft.png");
    }

    public Enemy(int startingCellX, int startingCellY, int heightWidth) {
        super(startingCellX, startingCellY, heightWidth);
        willKill = false;
        animationIdle = new Animation(idleTexture, 60, 14,14);
        animationMove = new Animation(moveTexture, 3, 14,14);
        for (int k = 0; k < idleFaceTextures.size(); k++) {
            animationsFaceIdle.add(new Animation(idleFaceTextures.get(k),100,14,14));
        }
        animationFaceMoveUp = new Animation(moveFaceTextureUp,3,14,14);
        animationFaceMoveDown = new Animation(moveFaceTextureDown,3,14,14);
        animationFaceMoveLeftRight = new Animation(moveFaceTextureLeftRight,3,14,14);
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
