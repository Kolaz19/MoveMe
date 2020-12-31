package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Hero extends Character {
    public boolean mv_willDie;
    public boolean mv_willWin;
    private Animation mr_animationExpl;

    public Hero(float iv_posX, float iv_posY, int iv_heightWidth) {
        super(iv_posX, iv_posY, iv_heightWidth);
        mv_willDie = false;
        mv_willWin = false;
    }

    public void addAnimationExplode(Texture ir_pathToAtlas, int iv_durationInFrames, int iv_maxFrames) {
        mr_animationExpl = new Animation(ir_pathToAtlas,iv_durationInFrames,iv_maxFrames);
    }

    public void playDeathAnimation() {
        mr_animationExpl.play();
        currentFrame = mr_animationExpl.getCurrentFrame();
    }

    public boolean isMidAnimationDeath() {
        return mr_animationExpl.isMidAnimation();
    }


    public void playAnimations () {
        if (mv_willDie && !isTargetSet()) {
            if (getScaling() != 3f) {
                playDeathAnimation();
                setScaling(3f);
            } else {
                if (isMidAnimationDeath()) {
                    playDeathAnimation();
                }
            }
            return;
        } else if (mv_willWin && !isTargetSet()) {
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
        int lv_cellX =(int) getTargetX() / mapLayer.getTileWidth();
        int lv_cellY = (int) getTargetY() / mapLayer.getTileHeight();
        TiledMapTileLayer.Cell lr_cellToHit = mapLayer.getCell(lv_cellX,lv_cellY);
        if (lr_cellToHit.getTile().getProperties().containsKey("target") && lr_cellToHit.getTile().getProperties().get("target",Boolean.class).equals(true)) {
            mv_willWin = true;
        }
    }

    public void reset(float iv_drawX,float iv_drawY) {
        mr_animationExpl.reset();
        animationAppear.reset();
        animationIdle.reset();
        animationMove.reset();
        setX(iv_drawX);
        setY(iv_drawY);
        setTargetX(iv_drawX);
        setTargetY(iv_drawY);
        setRotation(0);
        setScaling(1f);
        isAppearing = true;
        mv_willDie = false;
        mv_willWin = false;
    }

}
