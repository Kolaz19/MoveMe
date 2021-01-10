package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

public class Hero extends Character {
    public boolean willDie;
    public boolean willWin;
    protected Animation animationExplode;
    private static Texture idleTexture;
    private static Texture moveTexture;
    private static Texture explodeTexture;
    private static ArrayList<Texture> idleFaceTextures;
    private static Texture moveFaceTextureUp;
    private static Texture moveFaceTextureDown;
    private static Texture moveFaceTextureLeftRight;


    static {
        idleTexture = new Texture("charIdle.png");
        moveTexture = new Texture ("charMove.png");
        explodeTexture = new Texture("charExplode.png");
        idleFaceTextures = new ArrayList<>();
        idleFaceTextures.add(new Texture("CDefaultFace1.png"));
        idleFaceTextures.add(new Texture("CDefaultFace2.png"));
        idleFaceTextures.add(new Texture("CDefaultFace3.png"));
        idleFaceTextures.add(new Texture("CDefaultFace4.png"));
        idleFaceTextures.add(new Texture("CDefaultFace5.png"));
        idleFaceTextures.add(new Texture("CDefaultFace6.png"));
        idleFaceTextures.add(new Texture("CDefaultFace7.png"));
        idleFaceTextures.add(new Texture("CDefaultFace8.png"));
        moveFaceTextureUp = new Texture("CFaceMoveUp.png");
        moveFaceTextureDown = new Texture("CFaceMoveDown.png");
        moveFaceTextureLeftRight = new Texture("CFaceMoveRightLeft.png");
    }

    public Hero(int startingCellX, int startingCellY, int heightWidth) {
        super(startingCellX, startingCellY, heightWidth);
        willDie = false;
        willWin = false;
        animationIdle = new Animation(idleTexture,60,14,14);
        animationMove = new Animation(moveTexture,3,14,14);
        animationExplode = new Animation(explodeTexture,3,45,45);
        for (int k = 0; k < idleFaceTextures.size(); k++) {
            animationsFaceIdle.add(new Animation(idleFaceTextures.get(k),100,14,14));
        }
        animationFaceMoveUp = new Animation(moveFaceTextureUp,3,14,14);
        animationFaceMoveDown = new Animation(moveFaceTextureDown,3,14,14);
        animationFaceMoveLeftRight = new Animation(moveFaceTextureLeftRight,3,14,14);
    }



    public void playAnimation() {
        if (willDie && !isTargetSet()) {
            if (getScaling() != 3f) {
                animationExplode.play();
                currentFrame = animationExplode.getCurrentFrame();
                setScaling(3f);
            } else {
                if (animationExplode.isMidAnimation()) {
                    animationExplode.play();
                    currentFrame = animationExplode.getCurrentFrame();
                }
            }
            return;
        } else if (willWin && !isTargetSet()) {
            if (getScaling() > 0) {
              setScaling(getScaling()-0.02f);
              setFaceScaling(getFaceScaling()-0.02f);
              setRotation(getRotation()+10);
                animationIdle.play();
                currentFrame = animationIdle.getCurrentFrame();
            } else {
                setScaling(0);
            }
            return;
        }
        super.playAnimation();
    }

    public void checkFutureMapCollision(TiledMapTileLayer mapLayer) {
        //Check if player will hit tile with target property
        super.checkFutureMapCollision(mapLayer);
        int cellX =(int) getTargetX() / mapLayer.getTileWidth();
        int cellY = (int) getTargetY() / mapLayer.getTileHeight();
        TiledMapTileLayer.Cell cellToHit = mapLayer.getCell(cellX,cellY);
        if (cellToHit.getTile().getProperties().containsKey("target") && cellToHit.getTile().getProperties().get("target",Boolean.class).equals(true)) {
            willWin = true;
        }
    }

    public void reset(int startingCellX,int startingCellY) {
        int positionCellX = (startingCellX-1) * 16 + 1;
        int positionCellY = (startingCellY-1) * 16 + 1;
        animationExplode.reset();
        animationIdle.reset();
        animationMove.reset();
        setX(positionCellX);
        setY(positionCellY);
        setTargetX(positionCellX);
        setTargetY(positionCellY);
        setRotation(0);
        setScaling(6f);
        setFaceScaling(6f);
        isAppearing = true;
        willDie = false;
        willWin = false;
    }

}
