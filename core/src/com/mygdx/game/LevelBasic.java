package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

public class LevelBasic extends ScreenAdapter {

    MyGdxGame mr_main;
    private TiledMap mr_overworldMap;
    Character mr_char;
    OrthogonalTiledMapRenderer mr_mapRender;


    LevelBasic(MyGdxGame iv_maingame,Character ir_char) {
        mr_main = iv_maingame;
        mr_char = ir_char;
    }

    public void show() {
        //Map
    }

    public void render(float iv_delta) {
        //Camera
        mr_mapRender.setView(mr_main.gr_camera);

        mr_main.gr_camera.update();
        mr_main.gr_batch.setProjectionMatrix(mr_main.gr_camera.combined);
        AnimatedTiledMapTile.updateAnimationBaseTime();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mr_mapRender.getBatch().begin();

        mr_mapRender.getBatch().end();
        mr_main.gr_batch.begin();

        mr_main.gr_batch.end();
    }

    public void dispose() {
        mr_overworldMap.dispose();
    }

}
