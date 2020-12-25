package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;


public class LevelBasic extends ScreenAdapter {

    private MyGdxGame mr_main;
    private TiledMap mr_map;
    private TiledMapTileLayer mr_mapLayer;
    private Enemy[] ma_enemies;
    private int mv_mapHeight;
    private int mv_mapWidth;
    private ShapeRenderer mr_shapeRenderer;
    private float ma_lineCoordinates[][];
    private boolean mv_acceptInputs;
    private boolean mv_InputRegistered;
    private Animation mr_winAnimation;
    private Animation mr_looseAnimation;
    private Animation mr_endingTextAnimation;
    private float mv_sizeText;
    private boolean mv_levelOver;


    LevelBasic(MyGdxGame ir_maingame,Enemy[] ia_enemies,String iv_pathToMap,String iv_mapLayerName,Animation ir_winAnimation, Animation ir_looseAnimation) {
        mr_winAnimation = ir_winAnimation;
        mr_looseAnimation = ir_looseAnimation;
        mv_sizeText = 0;
        mv_acceptInputs = false;
        mr_main = ir_maingame;
        ma_enemies = ia_enemies;
        mv_levelOver = false;
        //Manage Map & animation
        mr_map = MapManager.mr_mapLoader.load(iv_pathToMap);
        MapManager.replaceTilesAnimated(mr_map,"BasicTileset","animation","target",0.5f,iv_mapLayerName,"animation","target",16);
        //Map height&width
        mr_mapLayer = (TiledMapTileLayer) mr_map.getLayers().get(iv_mapLayerName);
        mv_mapHeight = mr_mapLayer.getHeight() * mr_mapLayer.getTileHeight();
        mv_mapWidth = mr_mapLayer.getWidth() * mr_mapLayer.getTileWidth();
        //Add lines to render on screen
        addLinesToRender();

    }

    public void show() {
        //Set up Camera
        mr_main.gr_camera.viewportHeight = mv_mapHeight;
        mr_main.gr_camera.viewportWidth = mv_mapWidth;
        mr_main.gr_camera.position.x = mv_mapWidth / 2;
        mr_main.gr_camera.position.y = mv_mapHeight / 2;
    }

    public void render(float iv_delta) {
        //Camera
        mr_main.gr_mapRender.setView(mr_main.gr_camera);
        mr_main.gr_camera.update();
        mr_main.gr_batch.setProjectionMatrix(mr_main.gr_camera.combined);
        mr_shapeRenderer.setProjectionMatrix(mr_main.gr_camera.combined);
        //Update Animation of TileMap
        AnimatedTiledMapTile.updateAnimationBaseTime();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Check if inputs should be allowed
        mv_InputRegistered = false;
        if (mr_main.gr_char.isTargetSet() || mr_main.gr_char.mv_willDie || mr_main.gr_char.mv_willWin || mr_main.gr_char.mv_isAppearing) {
            mv_acceptInputs = false;
        } else {
            mv_acceptInputs = true;
        }

        processCharacterMovement();
        processEnemyMovement();

        //Check collision between char and enemies
        if ((mv_acceptInputs) && (mv_InputRegistered)) {
            checkFutureCharCollision(mr_main.gr_char, ma_enemies);
        }

        //Move char / enemies
        for (int lv_i = 0;lv_i < ma_enemies.length;lv_i++) {
            ma_enemies[lv_i].move(0.5f);
        }
        mr_main.gr_char.move(0.5f);

        //Render map
        mr_main.gr_mapRender.getBatch().begin();
        mr_main.gr_mapRender.renderTileLayer((TiledMapTileLayer) mr_map.getLayers().get("Default"));
        mr_main.gr_mapRender.getBatch().end();
        //Check if win/loose text can be displayed and player can process to next level
        checkEndingCondition();

        //Render lines/grid
        mr_shapeRenderer.begin();
        for (int lv_i = 0; lv_i < ma_lineCoordinates.length; lv_i++) {
            mr_shapeRenderer.line(ma_lineCoordinates[lv_i][0], ma_lineCoordinates[lv_i][1], ma_lineCoordinates[lv_i][2], ma_lineCoordinates[lv_i][3]);
        }
        mr_shapeRenderer.end();
        //Render characters
        mr_main.gr_batch.begin();
        mr_main.gr_batch.draw(mr_main.gr_char.getCurrentFrame(),mr_main.gr_char.getDrawX(),mr_main.gr_char.getDrawY(),mr_main.gr_char.getWidth()/2,mr_main.gr_char.getHeight()/2,mr_main.gr_char.getWidth(),mr_main.gr_char.getHeight(),mr_main.gr_char.getScaling(),mr_main.gr_char.getScaling(),mr_main.gr_char.getRotation());
        for (int lv_i = 0;lv_i < ma_enemies.length;lv_i++) {
            mr_main.gr_batch.draw(ma_enemies[lv_i].getCurrentFrame(),ma_enemies[lv_i].getDrawX(),ma_enemies[lv_i].getDrawY(),ma_enemies[lv_i].getWidth()/2,ma_enemies[lv_i].getHeight()/2,ma_enemies[lv_i].getWidth(),ma_enemies[lv_i].getHeight(),ma_enemies[lv_i].getScaling(),ma_enemies[lv_i].getScaling(),ma_enemies[lv_i].getRotation());
        }
        if (mv_levelOver) {
            mr_main.gr_batch.draw(mr_endingTextAnimation.getCurrentFrame(), 2,mv_mapHeight/2f - mr_endingTextAnimation.getCurrentFrame().getRegionHeight()/2f,mr_endingTextAnimation.getCurrentFrame().getRegionWidth()/2f,mr_endingTextAnimation.getCurrentFrame().getRegionHeight()/2f,mr_endingTextAnimation.getCurrentFrame().getRegionWidth(),mr_endingTextAnimation.getCurrentFrame().getRegionHeight(),mv_sizeText,mv_sizeText,0);
        }
        mr_main.gr_batch.end();
    }

    public void processCharacterMovement () {
        //Logic char
        mr_main.gr_char.playAnimations();
        if (mv_acceptInputs) {
            mr_main.gr_char.calibrateTargetPosition(16);
            if (mr_main.gr_char.isTargetSet()) {
                mr_main.gr_char.checkFutureMapCollision(mr_mapLayer);
                mv_InputRegistered = true;
            }
        }
    }

    public void processEnemyMovement() {
        //Logic enemies
        for (int lv_i = 0;lv_i < ma_enemies.length;lv_i++) {
            ma_enemies[lv_i].playAnimations();
            if (mv_acceptInputs) {
                ma_enemies[lv_i].calibrateTargetPosition(-16);
                if (ma_enemies[lv_i].isTargetSet()) {
                    ma_enemies[lv_i].checkFutureMapCollision(mr_mapLayer);
                }
            }
        }
    }

    public void checkEndingCondition() {
        Animation mr_animation;
        if (mr_main.gr_char.mv_willWin) {
            mr_endingTextAnimation = mr_winAnimation;
        } else if (mr_main.gr_char.mv_willDie && !mr_main.gr_char.isTargetSet()) {
            mr_endingTextAnimation = mr_looseAnimation;
        } else {
            return;
        }
        mv_levelOver = true;
        mr_endingTextAnimation.play();
        //5 is the width size of the text sprite, so I determine the max scaling size based on the map width
        float mv_maxSize = mr_mapLayer.getWidth() / 5f;
        if (mv_sizeText < mv_maxSize) {
            mv_sizeText += 0.01;
        }
    }

    public void dispose() {
        mr_map.dispose();
    }

    public void hide() {

    }

    private void addLinesToRender() {
        //Amount of lines on screen/map
        int lv_horizontalLines =  mv_mapHeight / mr_mapLayer.getTileHeight() - 1;
        int lv_verticalLines =  mv_mapWidth / mr_mapLayer.getTileWidth() - 1;
        //Shape Renderer for lines (to separate tiles)
        mr_shapeRenderer = new ShapeRenderer();
        mr_shapeRenderer.setAutoShapeType(true);
        mr_shapeRenderer.setColor(Color.BLACK);
        mr_main.gr_mapRender = new OrthogonalTiledMapRenderer(mr_map);
        //Set coordinates for the lines
        ma_lineCoordinates = new float[lv_horizontalLines+lv_verticalLines][4];
        //Set coordinates for horizontal lines
        for(int lv_i = 0; lv_i < lv_horizontalLines;lv_i++) {
            ma_lineCoordinates[lv_i][0] = 0;
            ma_lineCoordinates[lv_i][1] = (lv_i + 1) * mr_mapLayer.getTileHeight();
            ma_lineCoordinates[lv_i][2] = mr_mapLayer.getWidth() * mr_mapLayer.getTileWidth();
            ma_lineCoordinates[lv_i][3] = (lv_i + 1) * mr_mapLayer.getTileHeight();
        }
        //Set coordinates for vertical lines
        for(int lv_i = lv_horizontalLines; lv_i < lv_horizontalLines + lv_verticalLines; lv_i++) {
            ma_lineCoordinates[lv_i][0] = (lv_i + 1 - lv_horizontalLines) * mr_mapLayer.getTileWidth();
            ma_lineCoordinates[lv_i][1] = 0;
            ma_lineCoordinates[lv_i][2] = (lv_i + 1 - lv_horizontalLines) * mr_mapLayer.getTileWidth();
            ma_lineCoordinates[lv_i][3] = mr_mapLayer.getHeight() * mr_mapLayer.getTileHeight();
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
