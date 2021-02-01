package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class LevelBasic extends ScreenAdapter {

    private MyGdxGame mainGame;
    private TiledMap map;
    private TiledMapTileLayer mapLayer;
    private Enemy[] enemies;
    private Hero hero;
    private int currentLevel;
    private final int mapHeight;
    private final int mapWidth;
    private float lineCoordinates[][];
    private boolean acceptInputs;
    private boolean inputRegistered;
    private Animation winAnimation;
    private Animation looseAnimation;
    private Animation endingTextAnimation;
    private float textSizeMultiplier;
    private boolean isLevelOver;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera orthographicCamera;
    private static Texture winTexture;
    private static Texture looseTexture;

    static {
        winTexture = new Texture("winText.png");
        looseTexture = new Texture("looseText.png");
    }

    LevelBasic(MyGdxGame mainGame,int levelNumber,Hero hero,Enemy[] enemies,String fileNameMap,String mapLayerName) {
        this.mainGame = mainGame;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(Color.BLACK);
        orthographicCamera = new OrthographicCamera();
        currentLevel = levelNumber;
        this.winAnimation = new Animation(winTexture,50,80,32);
        this.looseAnimation = new Animation(looseTexture,50,80,32);
        textSizeMultiplier = 0;
        acceptInputs = false;
        this.enemies = enemies;
        this.hero = hero;
        isLevelOver = false;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        //Manage Map & map animation
        map = MapManager.mapLoader.load(fileNameMap);
        MapManager.replaceTilesAnimated(map,"BasicTileset","animation","target",0.5f,mapLayerName,"animation","target");
        MapManager.replaceTilesAnimated(map,"BasicTileset","animation","wall",0.5f,mapLayerName,"animation","wall");
        //Map height&width
        mapLayer = (TiledMapTileLayer) map.getLayers().get(mapLayerName);
        mapHeight = mapLayer.getHeight() * mapLayer.getTileHeight();
        mapWidth = mapLayer.getWidth() * mapLayer.getTileWidth();
        //Add lines to render on screen
        addLinesToRender();
    }

    public void show() {
        int borderSpacing = 23;
        //Set up Camera
        orthographicCamera.viewportHeight = mapHeight -borderSpacing;
        orthographicCamera.viewportWidth = mapWidth-borderSpacing;
        orthographicCamera.position.x = (int) (mapWidth / 2);
        orthographicCamera.position.y = (int) (mapHeight / 2);
        //Set up window size to match map size
        int windowTargetHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        //0.8 because taskbar does take also some room
        windowTargetHeight = (int) (windowTargetHeight * 0.8f);
        int windowTargetWidth = (mapWidth-borderSpacing) * windowTargetHeight / (mapHeight-borderSpacing);
        Gdx.graphics.setWindowedMode(windowTargetWidth,windowTargetHeight);
    }

    public void render(float delta) {
        //Update Animation of TileMap
        AnimatedTiledMapTile.updateAnimationBaseTime();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Camera
        tiledMapRenderer.setView(orthographicCamera);
        orthographicCamera.update();
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
        //Check if inputs should be allowed
        inputRegistered = false;
        if (hero.isTargetSet() || hero.willDie || hero.willWin || hero.isAppearing) {
            acceptInputs = false;
        } else {
            acceptInputs = true;
        }
        processCharacterMovement();
        processEnemyMovement();
        //Check collision between char and enemies
        if ((acceptInputs) && (inputRegistered)) {
            checkFutureCharCollision();
        }
        //Move char / enemies
        for (int k = 0; k < enemies.length; k++) {
            enemies[k].move(0.5f);
        }
        hero.move(0.5f);

        //Render map
        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(mapLayer.getName()));
        tiledMapRenderer.getBatch().end();

        //Render lines/grid
        shapeRenderer.begin();
        for (int k = 0; k < lineCoordinates.length; k++) {
            shapeRenderer.line(lineCoordinates[k][0], lineCoordinates[k][1], lineCoordinates[k][2], lineCoordinates[k][3]);
        }
        shapeRenderer.end();
        //Render characters
        spriteBatch.begin();
        spriteBatch.draw(hero.getCurrentFrame(),hero.getDrawX(),hero.getDrawY(),hero.getWidth()/2,hero.getHeight()/2,hero.getWidth(),hero.getHeight(),hero.getScaling(),hero.getScaling(),hero.getRotation());
        spriteBatch.draw(hero.getCurrentFace(),hero.getDrawX(),hero.getDrawY(),hero.getWidth()/2,hero.getHeight()/2,hero.getWidth(),hero.getHeight(),hero.getFaceScaling(),hero.getFaceScaling(),0);
        for (int k = 0; k < enemies.length; k++) {
            spriteBatch.draw(enemies[k].getCurrentFrame(), enemies[k].getDrawX(), enemies[k].getDrawY(), enemies[k].getWidth()/2, enemies[k].getHeight()/2, enemies[k].getWidth(), enemies[k].getHeight(), enemies[k].getScaling(), enemies[k].getScaling(), enemies[k].getRotation());
            spriteBatch.draw(enemies[k].getCurrentFace(), enemies[k].getDrawX(), enemies[k].getDrawY(), enemies[k].getWidth()/2, enemies[k].getHeight()/2, enemies[k].getWidth(), enemies[k].getHeight(), enemies[k].getScaling(), enemies[k].getFaceScaling(), 0);
        }
        if (isLevelOver) {
            spriteBatch.draw(endingTextAnimation.getCurrentFrame(), mapWidth /2f - endingTextAnimation.getCurrentFrame().getRegionWidth()/2f +1, mapHeight /2f - endingTextAnimation.getCurrentFrame().getRegionHeight()/2f, endingTextAnimation.getCurrentFrame().getRegionWidth()/2f, endingTextAnimation.getCurrentFrame().getRegionHeight()/2f, endingTextAnimation.getCurrentFrame().getRegionWidth(), endingTextAnimation.getCurrentFrame().getRegionHeight(), textSizeMultiplier, textSizeMultiplier,0);
        }
        spriteBatch.end();
        //Check if win/loose text can be displayed and player can process to next level
        try {
            checkEndingCondition();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processCharacterMovement () {
        //Logic char
        hero.playAnimation();
        hero.playFaceAnimation();
        if (acceptInputs) {
            hero.calibrateTargetPosition(16);
            if (hero.isTargetSet()) {
                hero.checkFutureMapCollision(mapLayer);
                inputRegistered = true;
            }
        }
    }

    public void processEnemyMovement() {
        //Logic enemies
        for (int k = 0; k < enemies.length; k++) {
            enemies[k].playAnimation();
            enemies[k].playFaceAnimation();
            if (acceptInputs) {
                enemies[k].calibrateTargetPosition(-16);
                if (enemies[k].isTargetSet()) {
                    enemies[k].checkFutureMapCollision(mapLayer);
                }
            }
        }
    }

    public void checkEndingCondition() throws IOException {
        if (hero.willWin) {
            endingTextAnimation = winAnimation;
        } else if (hero.willDie && !hero.isTargetSet()) {
            endingTextAnimation = looseAnimation;
        } else {
            return;
        }
        isLevelOver = true;
        endingTextAnimation.play();

        if (textSizeMultiplier * endingTextAnimation.getCurrentFrame().getRegionWidth() < mapWidth-32) {
            textSizeMultiplier += 0.01;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            if (hero.willDie) {
                mainGame.chooseLevel(this.currentLevel);
            } else {
                Savegame.writeSavestate(currentLevel);
                mainGame.chooseLevel(currentLevel + 1);
            }
        }
    }

    public void dispose() {
        map.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        tiledMapRenderer.dispose();
    }

    public void hide() {
        dispose();
    }

    private void addLinesToRender() {
        //Amount of lines on screen/map
        int amountHorizontalLines =  mapHeight / mapLayer.getTileHeight() - 1;
        int amountVerticalLines =  mapWidth / mapLayer.getTileWidth() - 1;
        //Set coordinates for the lines
        lineCoordinates = new float[amountHorizontalLines+amountVerticalLines][4];
        //Set coordinates for horizontal lines
        for(int k = 0; k < amountHorizontalLines;k++) {
            lineCoordinates[k][0] = mapLayer.getTileHeight();                                                   //x
            lineCoordinates[k][1] = (k + 1) * mapLayer.getTileHeight();                                         //y
            lineCoordinates[k][2] = mapLayer.getWidth() * mapLayer.getTileWidth() - mapLayer.getTileHeight();   //x2
            lineCoordinates[k][3] = (k + 1) * mapLayer.getTileHeight();                                         //y2
        }
        //Set coordinates for vertical lines
        for(int k = amountHorizontalLines; k < amountHorizontalLines + amountVerticalLines; k++) {
            lineCoordinates[k][0] = (k + 1 - amountHorizontalLines) * mapLayer.getTileWidth();
            lineCoordinates[k][1] = mapLayer.getTileHeight();
            lineCoordinates[k][2] = (k + 1 - amountHorizontalLines) * mapLayer.getTileWidth();
            lineCoordinates[k][3] = mapLayer.getHeight() * mapLayer.getTileHeight() - mapLayer.getTileHeight();
        }
    }


    public void checkFutureCharCollision() {
        ArrayList<Enemy> enemiesToCheckAgain = new ArrayList<>();
        for (int k = 0;k < enemies.length; k++) {
            //Check if char will collide with enemy
            if (hero.getTargetX() == enemies[k].getDrawX() && hero.getTargetY() == enemies[k].getDrawY()) {
                hero.setTargetX(hero.getDrawX());
                hero.setTargetY(hero.getDrawY());
                //Skip the other checks, because then noone should move
                break;
            }
            //Check if enemy will collide with char
            if (enemies[k].getTargetX() == hero.getDrawX() && enemies[k].getTargetY() == hero.getDrawY()) {
                enemies[k].setTargetX(enemies[k].getDrawX());
                enemies[k].setTargetY(enemies[k].getDrawY());
            }
            //Check if enemy will collide with another enemy that hasn`t target set
            for (int a = 0; a < enemies.length; a++) {
                if (k != a && (enemies[k].getTargetX() == enemies[a].getTargetX() && enemies[k].getTargetY() == enemies[a].getTargetY())) {
                    enemies[k].setTargetX(enemies[k].getDrawX());
                    enemies[k].setTargetY(enemies[k].getDrawY());
                    break;
                }
            }
            if (enemies[k].isTargetSet()) {
                enemiesToCheckAgain.add(enemies[k]);
            }
            //Check if character will die
            if (hero.getTargetX() == enemies[k].getTargetX() && hero.getTargetY() == enemies[k].getTargetY()) {
                hero.willDie = true;
                enemies[k].willKill = true;
            }
        }
        /*Enemies could be in a row -> Check again if target was set and needs correction
        After every loop and every correction the next enemy could also need correction*/
        int amountOfChecks = enemiesToCheckAgain.size();
        Iterator<Enemy> enemiesToCheckIter =  enemiesToCheckAgain.iterator();
        while (amountOfChecks != 0) {
            while (enemiesToCheckIter.hasNext()) {
                Enemy enemyToCheck = enemiesToCheckIter.next();
                for (Enemy enemyToCheckAgainst : enemies) {
                    if (enemyToCheck != enemyToCheckAgainst && (enemyToCheck.getTargetX() == enemyToCheckAgainst.getTargetX() && enemyToCheck.getTargetY() == enemyToCheckAgainst.getTargetY())) {
                        enemyToCheck.setTargetX(enemyToCheck.getDrawX());
                        enemyToCheck.setTargetY(enemyToCheck.getDrawY());
                        enemiesToCheckIter.remove();
                        break;
                    }
                }
            }
            amountOfChecks--;
        }



        //If character does not move, enemies should also not move
        //Reset winCondition in Hero, when character will not move to tile
        if (!hero.isTargetSet()) {
            hero.willWin = false;
            for (int k = 0; k < enemies.length; k++) {
                enemies[k].setTargetY(enemies[k].getDrawY());
                enemies[k].setTargetX(enemies[k].getDrawX());
            }
        }
    }

}
