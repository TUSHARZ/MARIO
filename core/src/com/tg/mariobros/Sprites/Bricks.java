package com.tg.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Scenes.Hud;
import com.tg.mariobros.Screens.PlayScreen;

/**
 * Created by TUSHAR on 07-02-2017.
 */
public class Bricks extends InteractiveTileObject {
  private   TiledMapTileSet tileset;

    public Bricks(PlayScreen screen,MapObject object){
        super(screen,object);
        setCategoryFilter(MarioBros.BRICK_BIT);
        fixture.setUserData(this);
        tileset=map.getTileSets().getTileSet("tileset_gutter");
    }

    @Override
    public void OnHeadHit(Mario mario) {


        if(mario.IsBig()){

        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(tileset.getTile(13));
            MarioBros.manager.get("audio/SOUNDS/breakblock.wav", Sound.class).play();
        Hud.addScore(200);
       Gdx.app.log("Bricks","collision");
    }
    else  MarioBros.manager.get("audio/SOUNDS/bump.wav", Sound.class).play();}
}
