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
    private boolean mv_charWillDie;
    private boolean mv_charWillWin;
    private boolean mv_acceptInputs;


    LevelBasic(MyGdxGame ir_maingame,Enemy[] ia_enemies,String iv_pathToMap,String iv_mapLayerName) {
        mv_charWillWin = false;
        mv_acceptInputs = false;
        mv_charWillDie = false;
        mr_main = ir_maingame;
        ma_enemies = ia_enemies;
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




        //Logic char
        mr_main.gr_char.playAnimations(mv_charWillDie,false);
        if (!mv_charWillDie) {
            mv_acceptInputs = mr_main.gr_char.calibrateTargetPosition();
        }
        mr_main.gr_char.checkFutureMapCollision(mr_mapLayer);
        //Logic enemies
        for (int lv_i = 0;lv_i < ma_enemies.length;lv_i++) {
            ma_enemies[lv_i].playAnimations();
            if (!mv_charWillDie) {
                if (mv_acceptInputs) {
                    ma_enemies[lv_i].calibrateTargetPosition(mr_main.gr_char);
                }
            }
                ma_enemies[lv_i].checkFutureMapCollision(mr_mapLayer);
        }
        //Check logic char/enemies
        checkFutureCharCollision(mr_main.gr_char,ma_enemies);
        //Check if char will die
        if (willDie(mr_main.gr_char,ma_enemies)) {
            mv_charWillDie = true;
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
        mr_main.gr_batch.end();
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

    public static void checkFutureCharCollision(Character ir_char, Enemy[] ia_enemies) {

        for (int lv_i = 0;lv_i < ia_enemies.length; lv_i++) {
            //Check if char will collide with enemy
            if (ir_char.getTargetX() == ia_enemies[lv_i].getDrawX() && ir_char.getTargetY() == ia_enemies[lv_i].getDrawY()) {
                ir_char.setTargetX(ir_char.getDrawX());
                ir_char.setTargetY(ir_char.getDrawY());
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
        }
        //TODO If character does not move, enemies should also not move - check if necessary
        if (ir_char.getTargetX() == ir_char.getDrawX() && ir_char.getTargetY() == ir_char.getDrawY()) {
            for (int lv_b = 0; lv_b < ia_enemies.length; lv_b++) {
                ia_enemies[lv_b].setTargetY(ia_enemies[lv_b].getDrawY());
                ia_enemies[lv_b].setTargetX(ia_enemies[lv_b].getDrawX());
            }
        }
    }

    //Has to be called after EVERY other check
    public boolean willDie(Character ir_char,Enemy[] ia_enemies) {
        for (int lv_b = 0; lv_b < ia_enemies.length; lv_b++) {
            if (ir_char.getTargetX() == ia_enemies[lv_b].getTargetX() && ir_char.getTargetY() == ia_enemies[lv_b].getTargetY()) {
                //Increase size of enemy until position reached (devour animation) - then shrink it
                if (ia_enemies[lv_b].getTargetX() == ia_enemies[lv_b].getDrawX() && ia_enemies[lv_b].getTargetY() == ia_enemies[lv_b].getDrawY()) {
                    if (ia_enemies[lv_b].getScaling()>1f) {
                        ia_enemies[lv_b].setScaling(ia_enemies[lv_b].getScaling() - 0.10f);
                    } else {
                        ia_enemies[lv_b].setScaling(1f);
                    }
                } else {
                    ia_enemies[lv_b].setScaling(ia_enemies[lv_b].getScaling()+0.04f);
                }
                return true;
            }
        }
        return false;
    }


}
