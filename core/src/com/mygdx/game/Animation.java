package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Animation {
    private TextureRegion[] ma_frames;
    private final int mv_maxFrames;
    private int mv_currentFrame;
    private int mv_durationInFrames;
    private int mv_frameCounter;
    private boolean mv_isMidAnimation;


    Animation(Texture ir_atlasPath,int iv_durationInFrames,int iv_maxFrames) {
        mv_isMidAnimation = false;
        mv_currentFrame = 0;
        mv_maxFrames = iv_maxFrames;
        ma_frames = new TextureRegion[mv_maxFrames];
        int lv_frameWidth = ir_atlasPath.getWidth()/mv_maxFrames;
        mv_durationInFrames = iv_durationInFrames;
        for (int i = 0; i < mv_maxFrames;i++) {
            ma_frames[i]=new TextureRegion(ir_atlasPath,i*lv_frameWidth,0,lv_frameWidth, ir_atlasPath.getHeight());
        }
    }

    public void play() {
        mv_isMidAnimation = true;
        mv_frameCounter++;
        if(mv_frameCounter % mv_durationInFrames == 0) {
            mv_currentFrame++;
            if (mv_currentFrame == mv_maxFrames) {
                mv_currentFrame = 0;
                mv_isMidAnimation = false;
            }
        }
    }

    public void setDurationInFrames(int iv_durationInFrames) {
        mv_durationInFrames = iv_durationInFrames;
        mv_frameCounter = 0;
    }

    public TextureRegion getCurrentFrame() {
        return ma_frames[mv_currentFrame];
    }

    public TextureRegion getSpecificFrame(int iv_frame) {
        if ((iv_frame < mv_maxFrames) || (iv_frame > -1)) {
            return ma_frames[iv_frame];
        } else {
            return ma_frames[0]; //return first frame if index is out of bounds
        }
    }

    //has to be checked before drawing for accurate result
    public boolean isMidAnimation () {
        return mv_isMidAnimation;
    }

}