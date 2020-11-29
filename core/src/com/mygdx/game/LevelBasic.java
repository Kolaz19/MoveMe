package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

public class LevelBasic extends ScreenAdapter {

    MyGdxGame mr_main;
    private TiledMap mr_map;
    Enemy[] ma_enemies;
    int mv_mapHeight;
    int mv_mapWidth;

    LevelBasic(MyGdxGame ir_maingame,Enemy[] ia_enemies,String iv_pathToMap) {
        mr_main = ir_maingame;
        ma_enemies = ia_enemies;
        //Manage Map & animation
        mr_map = MapManager.mr_mapLoader.load(iv_pathToMap);
        MapManager.replaceTilesAnimated(mr_map,"BasicTileset","animation","target",0.5f,"Default","animation","target",16);
        //Map height&width
        TiledMapTileLayer lr_mapLayer = (TiledMapTileLayer) mr_map.getLayers().get("Default");

        mv_mapHeight = lr_mapLayer.getHeight() * lr_mapLayer.getTileHeight();
        mv_mapWidth = lr_mapLayer.getWidth() * lr_mapLayer.getTileWidth();
        mr_main.gr_mapRender = new OrthogonalTiledMapRenderer(mr_map);
    }

    public void show() {
        //Camera
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
        AnimatedTiledMapTile.updateAnimationBaseTime();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mr_main.gr_mapRender.getBatch().begin();
        mr_main.gr_mapRender.renderTileLayer((TiledMapTileLayer) mr_map.getLayers().get("Default"));
        mr_main.gr_mapRender.getBatch().end();
        mr_main.gr_batch.begin();
        mr_main.gr_batch.end();
    }

    public void dispose() {
        mr_map.dispose();
    }

    public void hide() {

    }

}
