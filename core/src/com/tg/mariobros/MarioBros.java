package com.tg.mariobros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tg.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {
	public SpriteBatch batch;
    public static final int V_WIDTH=400;
    public static final int V_HEIGHT=208;
    public static final float PPM=100;

	public static final short MARIO_BIT=2;
    public static final short BRICK_BIT=4;
    public static final short COIN_BIT=8;
    public static final short DESTROYED_BIT=16;
    public static final short GROUND_BIT=1;
    public static final short ENEMY_HEAD_BIT=128;
    public static final short OBJECT_BIT =32;
    public static final short ENEMY_BIT =64;
    public static final short ITEM_BIT =256;
    public static final short MARIO_HEAD_BIT =512;
    public static final short NOTHING_BIT=0;
    public static  AssetManager manager;
	@Override
	public void create () {
		batch = new SpriteBatch();
        manager=new AssetManager();
        manager.load("audio/MUSIC/mario_music.ogg", Music.class);
        manager.load("audio/SOUNDS/breakblock.wav", Sound.class);
        manager.load("audio/SOUNDS/bump.wav", Sound.class);
        manager.load("audio/SOUNDS/coin.wav", Sound.class);
        manager.load("audio/SOUNDS/powerdown.wav", Sound.class);
        manager.load("audio/SOUNDS/powerup.wav", Sound.class);
        manager.load("audio/SOUNDS/powerup_spawn.wav", Sound.class);
        manager.load("audio/SOUNDS/stomp.wav", Sound.class);
        manager.load("audio/SOUNDS/mariodie.wav", Sound.class);
        manager.load("audio/SOUNDS/jump.wav",Sound.class);
        manager.load("audio/SOUNDS/ending.wav",Sound.class);
        manager.finishLoading();
		setScreen(new PlayScreen(this));


	}

	@Override
	public void render () {

		super.render();
	}
	
	@Override
	public void dispose () {
        super.dispose();
        manager.dispose();
		batch.dispose();

	}
}
