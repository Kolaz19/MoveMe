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
        Array<StaticTiledMapTile> la_staticTilesSorted = new Array<StaticTiledMapTile>();
        TiledMapTile lr_tile;
        int lv_number = ir_map.getTileSets().getTileSet(iv_tileSetName).size();
        Iterator<TiledMapTile> la_tiles = ir_map.getTileSets().getTileSet(iv_tileSetName).iterator();
        //Add tiles to array that are for this animation - set max. number for sort
        while (la_tiles.hasNext()) {
            lr_tile = la_tiles.next();
            if (lr_tile.getProperties().containsKey(iv_tileSetProperty) && lr_tile.getProperties().get(iv_tileSetProperty,String.class).equalsIgnoreCase(iv_tileSetValue)) {
                la_staticTiles.add((StaticTiledMapTile) lr_tile);
                if (lr_tile.getId() < lv_number) {
                    lv_number = lr_tile.getId();
                }
            }
        }
        //Sort tiles by ID to get correct animation
        while (la_staticTilesSorted.size < la_staticTiles.size) {
            for (StaticTiledMapTile lr_staticTile : la_staticTiles) {
                if (lr_staticTile.getId() == lv_number) {
                    la_staticTilesSorted.add(lr_staticTile);
                }
            }
            lv_number++;
        }

        AnimatedTiledMapTile lr_animatedTile = new AnimatedTiledMapTile(iv_intervalAnimation,la_staticTilesSorted);
        TiledMapTileLayer lr_tileLayer = (TiledMapTileLayer) ir_map.getLayers().get(iv_layerName);
        TiledMapTileLayer.Cell lr_cell;
        //Replace every tile on the map that meets this properties
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
