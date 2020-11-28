package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;


public final class MapManager {
    public static TmxMapLoader mr_mapLoader;

    static {
        mr_mapLoader = new TmxMapLoader();
    }

    public static void replaceTilesAnimated(TiledMap ir_map, String iv_tileSetName,String iv_tileSetProperty,String iv_tileSetValue, float iv_intervalAnimation, String iv_layerName,String iv_propertyToChange, String iv_valueToChange, int iv_tileSize) {
        Array<StaticTiledMapTile> la_staticTiles = new Array<StaticTiledMapTile>();
        TiledMapTile lr_tile;
        Iterator<TiledMapTile> la_tiles = ir_map.getTileSets().getTileSet(iv_tileSetName).iterator();
        while (la_tiles.hasNext()) {
            lr_tile = la_tiles.next();
            if (lr_tile.getProperties().containsKey(iv_tileSetProperty) && lr_tile.getProperties().get(iv_tileSetProperty,String.class).equalsIgnoreCase(iv_tileSetValue)) {
                la_staticTiles.add((StaticTiledMapTile) lr_tile);
            }
        }
        AnimatedTiledMapTile lr_animatedTile = new AnimatedTiledMapTile(iv_intervalAnimation,la_staticTiles);

        TiledMapTileLayer lr_tileLayer = (TiledMapTileLayer) ir_map.getLayers().get(iv_layerName);
        TiledMapTileLayer.Cell lr_cell;
        int lv_iterX = 0;
        int lv_iterY = 0;
        //TODO is every Cell captured? Does it start with 0 and end with tilesetwidth*X - 1 ?
        while (lv_iterX < lr_tileLayer.getWidth()) {
            while (lv_iterY < lr_tileLayer.getHeight()) {
                lr_cell = lr_tileLayer.getCell(lv_iterX,lv_iterY);
                if (lr_cell.getTile().getProperties().containsKey(iv_propertyToChange) && lr_cell.getTile().getProperties().get(iv_propertyToChange,String.class).equalsIgnoreCase(iv_valueToChange)) {
                    lr_cell.setTile(lr_animatedTile);
                }
                lv_iterY+=iv_tileSize;
            }
            lv_iterX+=iv_tileSize;
        }

        for (int lv_x = 0; lv_x < lr_tileLayer.getWidth(); lv_x++) {
            for (int lv_y = 0; lv_y < lr_tileLayer.getHeight(); lv_y++) {
                lr_cell = lr_tileLayer.getCell(lv_x,lv_y);
                if(lr_cell.getTile().getProperties().containsKey(iv_propertyToChange) && lr_cell.getTile().getProperties().get(iv_propertyToChange,String.class).equalsIgnoreCase(iv_valueToChange)) {
                    lr_cell.setTile(lr_animatedTile);
                }
            }
        }
    }

}
