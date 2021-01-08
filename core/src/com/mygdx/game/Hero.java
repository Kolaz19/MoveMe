package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

public class Hero extends Character {
    public boolean willDie;
    public boolean willWin;
    protected Animation animationExplode;
    private static Texture heroIdleTexture;
    private static Texture heroMoveTexture;
    private static Texture heroExplodeTexture;
    private static ArrayList<Texture> heroIdleFaceTextures;

    static {
        heroIdleTexture = new Texture("charIdle.png");
        heroMoveTexture = new Texture ("charMove.png");
        heroExplodeTexture = new Texture("charExplode.png");
        heroIdleFaceTextures = new ArrayList<>();
        heroIdleFaceTextures.add(new Texture("CDefaultFace1.png"));
        heroIdleFaceTextures.add(new Texture("CDefaultFace2.png"));
        heroIdleFaceTextures.add(new Texture("CDefaultFace3.png"));
        heroIdleFaceTextures.add(new Texture("CDefaultFace4.png"));
        heroIdleFaceTextures.add(new Texture("CDefaultFace5.png"));
        heroIdleFaceTextures.add(new Texture("CDefaultFace6.png"));
        heroIdleFaceTextures.add(new Texture("CDefaultFace7.png"));
        heroIdleFaceTextures.add(new Texture("CDefaultFace8.png"));
    }

    public Hero(int startingCellX, int startingCellY, int heightWidth) {
        super(startingCellX, startingCellY, heightWidth);
        willDie = false;
        willWin = false;
        animationIdle = new Animation(heroIdleTexture,60,15,15);
        animationMove = new Animation(heroMoveTexture,3,14,14);
        animationExplode = new Animation(heroExplodeTexture,3,45,45);
        for (int k = 0; k < heroIdleFaceTextures.size(); k++) {
            animationsFaceIdle.add(new Animation(heroIdleFaceTextures.get(k),100,15,15));
        }
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
        isAppearing = true;
        willDie = false;
        willWin = false;
    }

}
