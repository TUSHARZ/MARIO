package com.tg.mariobros.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Screens.PlayScreen;
import com.tg.mariobros.Sprites.Mario;

/**
 * Created by TUSHAR on 10-02-2017.
 */
public class Mushroom extends Item {


    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16);
        velocity=new Vector2(0.7f,0);
        Gdx.app.log("Called","");
    }

    @Override
    public void defineItem() {

        BodyDef bdef=new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type= BodyDef.BodyType.DynamicBody;
        body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(6/MarioBros.PPM);
        fdef.filter.categoryBits=MarioBros.ITEM_BIT;
        fdef.filter.maskBits=MarioBros.BRICK_BIT|MarioBros.COIN_BIT|MarioBros.GROUND_BIT|MarioBros.OBJECT_BIT|MarioBros.ENEMY_BIT|MarioBros.MARIO_BIT;
        fdef.shape=shape;
        body.createFixture(fdef).setUserData(this);


    }

    @Override
    public void use(Mario mario) {
        destroy();
        mario.grow();
        MarioBros.manager.get("audio/SOUNDS/powerup.wav", Sound.class).play();

    }

    @Override
    public void update(float dt) {
        super.update(dt);

        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
         velocity.y=body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
