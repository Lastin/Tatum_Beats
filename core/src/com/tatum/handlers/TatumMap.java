package com.tatum.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.tatum.entities.Bat;
import com.tatum.entities.Slime;

import java.util.ArrayList;

public class TatumMap {

    private final TiledMap tiledMap;
    private final int[] barsPositions;

    private ArrayList<Bat> bats = new ArrayList<Bat>();
    private ArrayList<Slime> slime = new ArrayList<Slime>();

    public TatumMap(TiledMap tiledMap, int[] barsPositions){
        this.tiledMap = tiledMap;
        this.barsPositions = barsPositions;
    }

    public TiledMap getTiledMap(){
        return tiledMap;
    }
    public int[] getBarsPositions(){
        return barsPositions;
    }
}
