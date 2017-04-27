package com.tg.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Screens.PlayScreen;
import com.tg.mariobros.Sprites.Bricks;
import com.tg.mariobros.Sprites.Coins;
import com.tg.mariobros.Sprites.Enemy;
import com.tg.mariobros.Sprites.Goomba;
import com.tg.mariobros.Sprites.InteractiveTileObject;
import com.tg.mariobros.Sprites.Turtle;

/**
 * Created by TUSHAR on 07-02-2017.
 */
public class B2WorldCreator {
    public Array<Goomba> goombas;
    public Array<Turtle> turtles;
    public B2WorldCreator(PlayScreen screen){
        World world=screen.getWorld();
        TiledMap map=screen.getMap();
        goombas=new Array<Goomba>();
        BodyDef bdef=new BodyDef();
        PolygonShape shape=new PolygonShape();
        FixtureDef fdef=new FixtureDef();
        Body body;
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject)object).getRectangle();
            bdef.type= BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ MarioBros.PPM,(rect.getY()+rect.getHeight()/2)/MarioBros.PPM);
            body=world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/MarioBros.PPM,rect.getHeight()/2/MarioBros.PPM);
            fdef.shape=shape;
            fdef.filter.categoryBits=MarioBros.OBJECT_BIT;

            body.createFixture(fdef);
        }
//pipes
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject)object).getRectangle();
            bdef.type= BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/MarioBros.PPM,(rect.getY()+rect.getHeight()/2)/MarioBros.PPM);
            body=world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/MarioBros.PPM,rect.getHeight()/2/MarioBros.PPM);
            fdef.shape=shape;
            body.createFixture(fdef);

        }
//coins
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Coins(screen,object);



        }

 //bricks
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new Bricks(screen,object);

        }
  //goombas
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject)object).getRectangle();
            goombas.add(new Goomba(screen,rect.getX()/MarioBros.PPM,rect.getY()/MarioBros.PPM));

        }
  //turtles
        turtles=new Array<Turtle>();
        for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject)object).getRectangle();
            turtles.add(new Turtle(screen,rect.getX()/MarioBros.PPM,rect.getY()/MarioBros.PPM));

        }


    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies=new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}
