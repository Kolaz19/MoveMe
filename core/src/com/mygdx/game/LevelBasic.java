package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

import java.io.IOException;


public class LevelBasic extends ScreenAdapter {

    MyGdxGame mainGame;
    private TiledMap map;
    private TiledMapTileLayer mapLayer;
    private Enemy[] enemies;
    private Hero hero;
    private int currentLevel;
    private int mapHeight;
    private int mapWidth;
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


    LevelBasic(MyGdxGame mainGame,int iv_levelNumber,Hero hero,Enemy[] ia_enemies,String iv_pathToMap,String iv_mapLayerName,Animation ir_winAnimation, Animation ir_looseAnimation) {
        this.mainGame = mainGame;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(Color.BLACK);
        orthographicCamera = new OrthographicCamera();
        currentLevel = iv_levelNumber;
        winAnimation = ir_winAnimation;
        looseAnimation = ir_looseAnimation;
        textSizeMultiplier = 0;
        acceptInputs = false;
        enemies = ia_enemies;
        this.hero = hero;
        isLevelOver = false;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        //Manage Map & map animation
        map = MapManager.mr_mapLoader.load(iv_pathToMap);
        MapManager.replaceTilesAnimated(map,"BasicTileset","animation","target",0.5f,iv_mapLayerName,"animation","target",16);
        //Map height&width
        mapLayer = (TiledMapTileLayer) map.getLayers().get(iv_mapLayerName);
        mapHeight = mapLayer.getHeight() * mapLayer.getTileHeight();
        mapWidth = mapLayer.getWidth() * mapLayer.getTileWidth();
        //Add lines to render on screen
        addLinesToRender();
    }

    public void show() {
        //Set up Camera
        orthographicCamera.viewportHeight = mapHeight;
        orthographicCamera.viewportWidth = mapWidth;
        orthographicCamera.position.x = mapWidth / 2;
        orthographicCamera.position.y = mapHeight / 2;
        //Set up window size to match map size
        int lv_destinationHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        //0.8 because taskbar does take also some room
        lv_destinationHeight = (int) (lv_destinationHeight * 0.8f);
        int lv_destinationWidth = mapWidth * lv_destinationHeight / mapHeight;
        Gdx.graphics.setWindowedMode(lv_destinationWidth,lv_destinationHeight);
    }

    public void render(float iv_delta) {
        //Camera
        tiledMapRenderer.setView(orthographicCamera);

        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        shapeRenderer.setProjectionMatrix(orthographicCamera.combined);
        orthographicCamera.update();
        //Update Animation of TileMap
        AnimatedTiledMapTile.updateAnimationBaseTime();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Check if inputs should be allowed
        inputRegistered = false;
        if (hero.isTargetSet() || hero.mv_willDie || hero.mv_willWin || hero.isAppearing) {
            acceptInputs = false;
        } else {
            acceptInputs = true;
        }
        processCharacterMovement();
        processEnemyMovement();
        //Check collision between char and enemies
        if ((acceptInputs) && (inputRegistered)) {
            checkFutureCharCollision(hero, enemies);
        }
        //Move char / enemies
        for (int lv_i = 0; lv_i < enemies.length; lv_i++) {
            enemies[lv_i].move(0.5f);
        }
        hero.move(0.5f);

        //Render map
        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(mapLayer.getName()));
        tiledMapRenderer.getBatch().end();


        //Render lines/grid
        shapeRenderer.begin();
        for (int lv_i = 0; lv_i < lineCoordinates.length; lv_i++) {
            shapeRenderer.line(lineCoordinates[lv_i][0], lineCoordinates[lv_i][1], lineCoordinates[lv_i][2], lineCoordinates[lv_i][3]);
        }
        shapeRenderer.end();
        //Render characters
        spriteBatch.begin();
        spriteBatch.draw(hero.getCurrentFrame(),hero.getDrawX(),hero.getDrawY(),hero.getWidth()/2,hero.getHeight()/2,hero.getWidth(),hero.getHeight(),hero.getScaling(),hero.getScaling(),hero.getRotation());
        for (int lv_i = 0; lv_i < enemies.length; lv_i++) {
            spriteBatch.draw(enemies[lv_i].getCurrentFrame(), enemies[lv_i].getDrawX(), enemies[lv_i].getDrawY(), enemies[lv_i].getWidth()/2, enemies[lv_i].getHeight()/2, enemies[lv_i].getWidth(), enemies[lv_i].getHeight(), enemies[lv_i].getScaling(), enemies[lv_i].getScaling(), enemies[lv_i].getRotation());
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
        hero.playAnimations();
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
        for (int lv_i = 0; lv_i < enemies.length; lv_i++) {
            enemies[lv_i].playAnimations();
            if (acceptInputs) {
                enemies[lv_i].calibrateTargetPosition(-16);
                if (enemies[lv_i].isTargetSet()) {
                    enemies[lv_i].checkFutureMapCollision(mapLayer);
                }
            }
        }
    }

    public void checkEndingCondition() throws IOException {
        if (hero.mv_willWin) {
            endingTextAnimation = winAnimation;
        } else if (hero.mv_willDie && !hero.isTargetSet()) {
            endingTextAnimation = looseAnimation;
        } else {
            return;
        }
        isLevelOver = true;
        endingTextAnimation.play();

        if (textSizeMultiplier * endingTextAnimation.getCurrentFrame().getRegionWidth() < mapWidth) {
            textSizeMultiplier += 0.01;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            Savegame.writeSavestate(currentLevel);
            mainGame.chooseLevel(2);
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
        int lv_horizontalLines =  mapHeight / mapLayer.getTileHeight() - 1;
        int lv_verticalLines =  mapWidth / mapLayer.getTileWidth() - 1;
        //Set coordinates for the lines
        lineCoordinates = new float[lv_horizontalLines+lv_verticalLines][4];
        //Set coordinates for horizontal lines
        for(int lv_i = 0; lv_i < lv_horizontalLines;lv_i++) {
            lineCoordinates[lv_i][0] = 0;
            lineCoordinates[lv_i][1] = (lv_i + 1) * mapLayer.getTileHeight();
            lineCoordinates[lv_i][2] = mapLayer.getWidth() * mapLayer.getTileWidth();
            lineCoordinates[lv_i][3] = (lv_i + 1) * mapLayer.getTileHeight();
        }
        //Set coordinates for vertical lines
        for(int lv_i = lv_horizontalLines; lv_i < lv_horizontalLines + lv_verticalLines; lv_i++) {
            lineCoordinates[lv_i][0] = (lv_i + 1 - lv_horizontalLines) * mapLayer.getTileWidth();
            lineCoordinates[lv_i][1] = 0;
            lineCoordinates[lv_i][2] = (lv_i + 1 - lv_horizontalLines) * mapLayer.getTileWidth();
            lineCoordinates[lv_i][3] = mapLayer.getHeight() * mapLayer.getTileHeight();
        }
    }

    public static void checkFutureCharCollision(Hero ir_char, Enemy[] ia_enemies) {
        for (int lv_i = 0;lv_i < ia_enemies.length; lv_i++) {
            //Check if char will collide with enemy
            if (ir_char.getTargetX() == ia_enemies[lv_i].getDrawX() && ir_char.getTargetY() == ia_enemies[lv_i].getDrawY()) {
                ir_char.setTargetX(ir_char.getDrawX());
                ir_char.setTargetY(ir_char.getDrawY());
                //Skip the other checks, because then noone should move
                break;
            }
            //Check if enemy will collide with char
            if (ia_enemies[lv_i].getTargetX() == ir_char.getDrawX() && ia_enemies[lv_i].getTargetY() == ir_char.getDrawY()) {
                ia_enemies[lv_i].setTargetX(ia_enemies[lv_i].getDrawX());
                ia_enemies[lv_i].setTargetY(ia_enemies[lv_i].getDrawY());
            }
            //Check if enemy will collide with another enemy
            for (int lv_a = 0; lv_a < ia_enemies.length; lv_a++) {
                if (lv_i != lv_a && (ia_enemies[lv_i].getTargetX() == ia_enemies[lv_a].getTargetX() && ia_enemies[lv_i].getTargetY() == ia_enemies[lv_a].getTargetY())) {
                    ia_enemies[lv_i].setTargetX(ia_enemies[lv_i].getDrawX());
                    ia_enemies[lv_i].setTargetY(ia_enemies[lv_i].getDrawY());
                }
            }
            //Check if character will die
            if (ir_char.getTargetX() == ia_enemies[lv_i].getTargetX() && ir_char.getTargetY() == ia_enemies[lv_i].getTargetY()) {
                ir_char.mv_willDie = true;
                ia_enemies[lv_i].mv_willKill = true;
            }

        }
        //If character does not move, enemies should also not move
        //Reset winCondition in Hero, when character will not move to tile
        if (!ir_char.isTargetSet()) {
            ir_char.mv_willWin = false;
            for (int lv_b = 0; lv_b < ia_enemies.length; lv_b++) {
                ia_enemies[lv_b].setTargetY(ia_enemies[lv_b].getDrawY());
                ia_enemies[lv_b].setTargetX(ia_enemies[lv_b].getDrawX());
            }
        }
    }

}
