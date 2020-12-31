package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;


public final class MapManager {
    public static TmxMapLoader mapLoader;

    static {
        mapLoader = new TmxMapLoader();
    }

    public static void replaceTilesAnimated(TiledMap map, String tileSetName,String tileSetProperty,String tileSetValue, float animationInterval, String layerName,String propertyToChange, String valueToChange) {
        Array<StaticTiledMapTile> targetTiles = new Array<StaticTiledMapTile>();
        Array<StaticTiledMapTile> sortedTargetTiles = new Array<StaticTiledMapTile>();
        TiledMapTile currentTile;
        int tileSetSortId = map.getTileSets().getTileSet(tileSetName).size();
        Iterator<TiledMapTile> tiles = map.getTileSets().getTileSet(tileSetName).iterator();
        //Add tiles to array that are for this animation
        while (tiles.hasNext()) {
            currentTile = tiles.next();
            if (currentTile.getProperties().containsKey(tileSetProperty) && currentTile.getProperties().get(tileSetProperty,String.class).equalsIgnoreCase(tileSetValue)) {
                targetTiles.add((StaticTiledMapTile) currentTile);
                //Set min. ID number for performant sort
                if (currentTile.getId() < tileSetSortId) {
                    tileSetSortId = currentTile.getId();
                }
            }
        }
        //Sort tiles by ID to get correct animation
        while (sortedTargetTiles.size < targetTiles.size) {
            for (StaticTiledMapTile tileToSort : targetTiles) {
                if (tileToSort.getId() == tileSetSortId) {
                    sortedTargetTiles.add(tileToSort);
                }
            }
            tileSetSortId++;
        }

        AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(animationInterval,sortedTargetTiles);
        //Set properties for animated tiles, because they will not automatically be transferred from static tiles
        animatedTile.getProperties().putAll(sortedTargetTiles.get(0).getProperties());
        //Choose correct layer
        TiledMapTileLayer currentLayer = (TiledMapTileLayer) map.getLayers().get(layerName);
        TiledMapTileLayer.Cell currentCell;
        //Replace every tile on the map that meets this properties
        for (int k = 0; k < currentLayer.getWidth(); k++) {
            for (int b = 0; b < currentLayer.getHeight(); b++) {
                currentCell = currentLayer.getCell(k,b);
                if(currentCell.getTile().getProperties().containsKey(propertyToChange) && currentCell.getTile().getProperties().get(propertyToChange,String.class).equalsIgnoreCase(valueToChange)) {
                    currentCell.setTile(animatedTile);
                }
            }
        }
    }

}
