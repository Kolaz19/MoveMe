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


    LevelBasic(MyGdxGame ir_maingame,Enemy[] ia_enemies,String iv_pathToMap,String iv_mapLayerName) {
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
        //Render char
        mr_main.gr_char.animationStillPlaying();
        mr_main.gr_char.calibrateTargetPosition();
        mr_main.gr_char.checkFutureMapCollision(mr_mapLayer);
        mr_main.gr_char.move(0.5f);
        //Render enemies
        for (int lv_i = 0;lv_i < ma_enemies.length;lv_i++) {
            ma_enemies[lv_i].animationStillPlaying();
            ma_enemies[lv_i].calibrateTargetPosition();
            ma_enemies[lv_i].checkFutureMapCollision(mr_mapLayer);
            ma_enemies[lv_i].move(0.5f);
        }
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
        mr_main.gr_batch.draw(mr_main.gr_char.getCurrentFrame(),mr_main.gr_char.getDrawX(),mr_main.gr_char.getDrawY(),mr_main.gr_char.getWidth()/2,mr_main.gr_char.getHeight()/2,mr_main.gr_char.getWidth(),mr_main.gr_char.getHeight(),1f,1f,mr_main.gr_char.getRotation());
        for (int lv_i = 0;lv_i < ma_enemies.length;lv_i++) {
            mr_main.gr_batch.draw(ma_enemies[lv_i].getCurrentFrame(),ma_enemies[lv_i].getDrawX(),ma_enemies[lv_i].getDrawY(),ma_enemies[lv_i].getWidth()/2,ma_enemies[lv_i].getHeight()/2,ma_enemies[lv_i].getWidth(),ma_enemies[lv_i].getHeight(),1f,1f,ma_enemies[lv_i].getRotation());
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

}
