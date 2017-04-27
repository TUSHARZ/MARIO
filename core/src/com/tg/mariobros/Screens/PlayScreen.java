package com.tg.mariobros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tg.mariobros.Items.Item;
import com.tg.mariobros.Items.ItemDef;
import com.tg.mariobros.Items.Mushroom;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Scenes.Hud;
import com.tg.mariobros.Sprites.Coins;
import com.tg.mariobros.Sprites.Enemy;
import com.tg.mariobros.Sprites.Goomba;
import com.tg.mariobros.Sprites.Mario;
import com.tg.mariobros.Tools.B2WorldCreator;
import com.tg.mariobros.Tools.WorldContactListener;


import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by TUSHAR on 05-02-2017.
 */
public class PlayScreen implements Screen {
    private MarioBros game;

    private Mario player;
    private TextureAtlas atlas;
private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private Hud hud;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private TmxMapLoader mapLoader;
    private TiledMap  map;
    private OrthogonalTiledMapRenderer renderer;
    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;
    Controller controller;


    public PlayScreen(MarioBros game){
       controller=new Controller(game.batch);
       this.game=game;

        atlas=new TextureAtlas("Mario_Packs.pack");
        world=new World(new Vector2(0,-10),true);
        player=new Mario(this);

        world.setContactListener(new WorldContactListener());
        b2dr=new Box2DDebugRenderer();
        gamecam=new OrthographicCamera();
        gamePort=new FitViewport(MarioBros.V_WIDTH/MarioBros.PPM,MarioBros.V_HEIGHT/MarioBros.PPM,gamecam);
        hud=new Hud(game.batch);
        mapLoader=new TmxMapLoader();
        map=mapLoader.load("level2.tmx");
        renderer=new OrthogonalTiledMapRenderer(map,1/MarioBros.PPM);


        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);

        //shape and fixtures
        creator=new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());
            items=new Array<Item>();
        itemsToSpawn=new LinkedBlockingDeque<ItemDef>();

    }
    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }
    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef =itemsToSpawn.poll();
            if(idef.type== Mushroom.class) {
              items.add(new Mushroom(this,idef.position.x,idef.position.y));
            }
        }
    }
    @Override
    public void show() {

    }
    public void handleInput(float dt){

        if(player.currentState!= Mario.State.DEAD) {
            if (controller.isJump()&&player.getState()!=Mario.State.JUMPING)


                player.b2body.applyLinearImpulse(new Vector2(0, 4.5f), player.b2body.getWorldCenter(), true);
                player.currentState= Mario.State.JUMPING;



            if (controller.isRit() && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (controller.isLef() && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }


    public TiledMap getMap(){
        return map;

    }
    public World getWorld(){
        return world;
    }
    public void update(float dt){
        handleInput(dt);
        handleSpawningItems();
        hud.update(dt);
        world.step(1 / 60f, 6, 2);

       player.update(dt);
        if(player.b2body.getPosition().y<=-40/MarioBros.PPM&&player.currentState!= Mario.State.DEAD){
            player.setMarioisDead(true);
        }

        for(Enemy enemy: creator.getEnemies()){
            enemy.update(dt);
            if(enemy.getX()<player.getX()+224/MarioBros.PPM)
                enemy.b2body.setActive(true);}

        for(Item item:items)
            item.update(dt);

if(player.currentState!= Mario.State.DEAD){







    gamecam.position.x=player.b2body.getPosition().x;

    gamecam.update();

    renderer.setView(gamecam);
    }}
  public TextureAtlas getAtlas(){
      return atlas;
  }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
       for(Enemy enemy:creator.getEnemies()){
           enemy.draw(game.batch);
       }
        for(Item item : items)
            item.draw(game.batch);


        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();
        
        if(Coins.getend())
        {game.setScreen(new end(game));
            dispose();
        }

        if(gameOver()){

            game.setScreen(new GameOver(game));
        dispose();}


    }
    public boolean gameOver(){
        if(player.currentState==Mario.State.DEAD&&player.getStateTimer()>3){
            return true;
        }
        else return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
        controller.resize(width,height);

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
