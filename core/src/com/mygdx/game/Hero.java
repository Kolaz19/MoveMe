package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Hero extends Character {
    public boolean willDie;
    public boolean willWin;
    protected Animation animationExplode;

    public Hero(int startingCellX, int startingCellY, int heightWidth) {
        super(startingCellX, startingCellY, heightWidth);
        willDie = false;
        willWin = false;
    }

    public void addAnimationExplode(Texture textureAtlas, int durationInFrames, int frameWidth, int frameHeight) {
        animationExplode = new Animation(textureAtlas,durationInFrames,frameWidth,frameHeight);
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
