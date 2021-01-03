package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;


public class Animation {
    private TextureRegion[] frames;
    private final int amountOfFrames;
    private int currentKeyFrame;
    private int durationInFrames;
    private int frameCounter;
    private boolean isMidAnimation;


    Animation(Texture textureAtlas,int durationInFrames,int frameWidthHeight) {
        isMidAnimation = false;
        currentKeyFrame = 0;
        this.durationInFrames = durationInFrames;
        TextureRegion[][] frames2d = TextureRegion.split(textureAtlas,frameWidthHeight,frameWidthHeight);
        if (frames2d.length == 0) {
            frames = new TextureRegion[1];
            frames[0] = new TextureRegion(textureAtlas);
            amountOfFrames = 1;
            return;
        }
        //TODO get precise amount of frames -> possibly that not every row is the same length
        amountOfFrames = frames2d.length * frames2d[0].length;
        frames = new TextureRegion[amountOfFrames];
        int counter = 0;
        for (int k = 0; k < frames2d.length; k++) {
            for (int p = 0; p < frames2d[k].length; p++) {
                frames[counter] = frames2d[k][p];
                counter++;
            }
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