package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Animation {
    private TextureRegion[] frames;
    private final int amountOfFrames;
    private int currentKeyFrame;
    private int durationInFrames;
    private int frameCounter;
    private boolean isMidAnimation;


    Animation(Texture textureAtlas,int durationInFrames,int amountKeyFrames) {
        isMidAnimation = false;
        currentKeyFrame = 0;
        this.amountOfFrames = amountKeyFrames;
        frames = new TextureRegion[this.amountOfFrames];
        int lv_frameWidth = textureAtlas.getWidth()/ this.amountOfFrames;
        this.durationInFrames = durationInFrames;
        for (int k = 0; k < this.amountOfFrames; k++) {
            frames[k]=new TextureRegion(textureAtlas,k*lv_frameWidth,0,lv_frameWidth, textureAtlas.getHeight());
        }
    }

    public void play() {
        isMidAnimation = true;
        frameCounter++;
        if(frameCounter % durationInFrames == 0) {
            currentKeyFrame++;
            if (currentKeyFrame == amountOfFrames) {
                currentKeyFrame = 0;
                isMidAnimation = false;
            }
        }
    }

    public void setDuration(int frames) {
        durationInFrames = frames;
        frameCounter = 0;
    }

    public TextureRegion getCurrentFrame() {
        return frames[currentKeyFrame];
    }

    public TextureRegion getSpecificFrame(int frameNumber) {
            return frames[frameNumber-1];
    }

    //has to be checked before drawing for accurate result
    public boolean isMidAnimation () {
        return isMidAnimation;
    }

    public void reset() {
        currentKeyFrame = 0;
        isMidAnimation = false;
        frameCounter = 0;
    }

    public void dispose() {
        frames[0].getTexture().dispose();
    }

}