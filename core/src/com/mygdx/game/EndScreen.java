package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class EndScreen extends MenuScreen {
    private static Texture endTextTexture;
    private static Texture smileyTexture;
    private Animation endText;
    private Animation smiley;
    private int currentTextFrame;
    private int frameCounter;


    static {
        endTextTexture = new Texture("endingText.png");
        smileyTexture = new Texture("endHero.png");
    }

    public EndScreen(MyGdxGame maingame) {
        super(maingame);
        endText = new Animation(endTextTexture,500,346, endTextTexture.getHeight());
        smiley = new Animation(smileyTexture,50,88, smileyTexture.getHeight());
        currentTextFrame = 0;
        frameCounter = 0;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        basicScreenRenderSetup();

        smiley.play();
        setEndTextFrame();

        spriteBatch.begin();
        basicBackgroundRendering();
        spriteBatch.draw(smiley.getCurrentFrame(),backgroundWidth/2 - smiley.getCurrentFrame().getRegionWidth()/2,100);
        if (!(currentTextFrame==0)) {
            spriteBatch.draw(endText.getSpecificFrame(currentTextFrame), backgroundWidth / 2 - endText.getCurrentFrame().getRegionWidth() / 2 - 15, 230, backgroundWidth / 2, 230 - endText.getCurrentFrame().getRegionHeight() / 2, endText.getCurrentFrame().getRegionWidth(), endText.getCurrentFrame().getRegionHeight(), 0.7f, 0.7f, 0);
        }
        spriteBatch.end();
    }

    private void setEndTextFrame() {
        if (frameCounter > 350) {
            return;
        }

        if (frameCounter == 150) {
            currentTextFrame++;
        } else if (frameCounter == 250) {
            currentTextFrame++;
        } else if (frameCounter == 350) {
            currentTextFrame++;
        }
        frameCounter++;
    }


}
