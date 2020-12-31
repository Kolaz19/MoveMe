package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Hero extends Character {
    public boolean willDie;
    public boolean willWin;
    private Animation animationExplode;

    public Hero(float startPosX, float startPosY, int heightWidth) {
        super(startPosX, startPosY, heightWidth);
        willDie = false;
        willWin = false;
    }

    public void addAnimationExplode(Texture textureAtlas, int durationInFrames, int amountOfFrames) {
        animationExplode = new Animation(textureAtlas,durationInFrames,amountOfFrames);
    }

    public void playDeathAnimation() {
        animationExplode.play();
        currentFrame = animationExplode.getCurrentFrame();
    }

    public boolean isMidAnimationDeath() {
        return animationExplode.isMidAnimation();
    }


    public void playAnimations () {
        if (willDie && !isTargetSet()) {
            if (getScaling() != 3f) {
                playDeathAnimation();
                setScaling(3f);
            } else {
                if (isMidAnimationDeath()) {
                    playDeathAnimation();
                }
            }
            return;
        } else if (willWin && !isTargetSet()) {
            if (getScaling() > 0) {
              setScaling(getScaling()-0.02f);
              setRotation(getRotation()+10);
              playIdleAnimation();
            } else {
                setScaling(0);
            }
            return;
        }
        super.playAnimations();
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

    public void reset(float startPosX,float startPosY) {
        animationExplode.reset();
        animationAppear.reset();
        animationIdle.reset();
        animationMove.reset();
        setX(startPosX);
        setY(startPosY);
        setTargetX(startPosX);
        setTargetY(startPosY);
        setRotation(0);
        setScaling(1f);
        isAppearing = true;
        willDie = false;
        willWin = false;
    }

}
