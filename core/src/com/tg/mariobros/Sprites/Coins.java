package com.tg.mariobros.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tg.mariobros.Items.ItemDef;
import com.tg.mariobros.Items.Mushroom;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Scenes.Hud;
import com.tg.mariobros.Screens.GameOver;
import com.tg.mariobros.Screens.PlayScreen;

/**
 * Created by TUSHAR on 07-02-2017.
 */
public class Coins extends InteractiveTileObject {
    private static TiledMapTileSet tileset;
    private final int BLANK_COIN=28;

    public  static boolean end;
    public Coins(PlayScreen screen,MapObject object){
        super(screen,object);
        tileset=map.getTileSets().getTileSet("tileset_gutter");
        setCategoryFilter(MarioBros.COIN_BIT);
fixture.setUserData(this);
        end=false;
    }

    @Override
    public void OnHeadHit(Mario mario) {

        if(getCell().getTile().getId()==BLANK_COIN)
        MarioBros.manager.get("audio/SOUNDS/bump.wav", Sound.class).play();

        else if (object.getProperties().containsKey("Mushroom"))
        {MarioBros.manager.get("audio/SOUNDS/powerup_spawn.wav", Sound.class).play();
            screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x,body.getPosition().y+16/MarioBros.PPM), Mushroom.class));

        }
        else if(object.getProperties().containsKey("end"))
        {end=true;
            MarioBros.manager.get("audio/MUSIC/mario_music.ogg",Music.class).stop();
            MarioBros.manager.get("audio/SOUNDS/ending.wav",Sound.class).play();
           }
        else
           MarioBros.manager.get("audio/SOUNDS/coin.wav", Sound.class).play();

        getCell().setTile(tileset.getTile(BLANK_COIN));
        Hud.addScore(100);

        

    }
public static boolean getend()
{
return  end;}
}
