package com.tg.mariobros.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Screens.PlayScreen;
import com.tg.mariobros.Sprites.Mario;

/**
 * Created by TUSHAR on 10-02-2017.
 */
public abstract class Item extends Sprite {
    protected Body body;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected World world;
   protected PlayScreen screen;
   protected Vector2 velocity;
    public  Item(PlayScreen screen,float x,float y){

        this.screen=screen;
        world=screen.getWorld();
        setPosition(x,y);

        velocity=new Vector2(0,0);

        setBounds(getX(),getY(),16/ MarioBros.PPM,16/MarioBros.PPM);
        defineItem();
        destroyed=false;
        toDestroy=false;

    }
    public abstract  void defineItem();
    public abstract void use(Mario mario);
    public void update(float dt){
        if(toDestroy&&!destroyed){
            world.destroyBody(body);
        destroyed=true;}

    }
    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }
    public void destroy(){

        toDestroy=true;
    }
    public void reverse(boolean x,boolean y){
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;

    }
}
