package com.tg.mariobros.Sprites;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.utils.Array;

import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Scenes.Hud;
import com.tg.mariobros.Screens.PlayScreen;

/**
 * Created by TUSHAR on 07-02-2017.
 */
public class Mario extends Sprite {
    public float stateTimer;
    private boolean stateRight;
    public State currentState;
    public State previousState;
    private World world;
    public Body b2body;
    private TextureRegion marioStand;
    private TextureRegion marioJump;
    private Animation marioRun;
    private TextureRegion bigMarioStand;
    private TextureRegion marioDead;
    private boolean marioisDead;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeDefineMario;
    private boolean timeSmallMario;
    public enum State{JUMPING,STANDING,RUNNING,FALLING,GROWING,DEAD};
    private Music music;



    public void setMarioisDead(boolean marioisDead) {
        if(!this.marioisDead)
        this.marioisDead = marioisDead;
        MarioBros.manager.get("audio/MUSIC/mario_music.ogg",Music.class).stop();
        MarioBros.manager.get("audio/SOUNDS/mariodie.wav",Sound.class).play();
    }

    public Mario(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));

        this.world=screen.getWorld();
        stateTimer=0;
        stateRight=true;
        currentState=State.STANDING;
        previousState=State.STANDING;
        Array<TextureRegion> frames=new Array<TextureRegion>();

        for(int i=1;i<4;i++)
            frames.add(new TextureRegion(getTexture(),i*16,0,16,16));
        marioRun=new Animation(0.1f,frames);
        frames.clear();
        for(int i=1;i<4;i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),i*16,0,16,32));
        bigMarioRun=new Animation(0.1f,frames);
frames.clear();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        growMario=new Animation(0.2f,frames);
         marioJump=new TextureRegion(screen.getAtlas().findRegion("little_mario"),80,0,16,16);
        bigMarioJump=new TextureRegion(screen.getAtlas().findRegion("big_mario"),80,0,16,32);

        defineMario();
        marioStand=new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);
        bigMarioStand=new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);
        marioDead=new TextureRegion(screen.getAtlas().findRegion("little_mario"),96,0,16,16);
        setBounds(0,0,16/MarioBros.PPM,16/MarioBros.PPM);
        setRegion(marioStand);
        music=MarioBros.manager.get("audio/MUSIC/mario_music.ogg",Music.class);
        music.setLooping(true);
        music.play();

    }
    public boolean isDead(){
        return marioisDead;
    }
    public float getStateTimer(){
        return stateTimer;
    }
    public void update(float dt){

        if(marioIsBig)
            setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2-6/MarioBros.PPM);
        else
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
        if(timeDefineMario)
            defineBigMario();
        if(timeSmallMario)
            redefineMario();
    }
    public void defineBigMario(){
        Vector2 currentpos=b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef=new BodyDef();
        bdef.position.set(currentpos.add(0,6/MarioBros.PPM));
        bdef.type= BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(6/MarioBros.PPM);
        fdef.filter.categoryBits=MarioBros.MARIO_BIT;
        fdef.filter.maskBits=MarioBros.BRICK_BIT|MarioBros.COIN_BIT|MarioBros.GROUND_BIT|MarioBros.ENEMY_BIT|MarioBros.OBJECT_BIT
                |MarioBros.ENEMY_HEAD_BIT|MarioBros.ITEM_BIT;
        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0,-14/MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);


        EdgeShape head=new EdgeShape();
        head.set(new Vector2(-2/MarioBros.PPM,8/MarioBros.PPM),new Vector2(2/MarioBros.PPM,8/MarioBros.PPM));
        fdef.shape=head;
        fdef.filter.categoryBits=MarioBros.MARIO_HEAD_BIT;
        fdef.isSensor=true;
        b2body.createFixture(fdef).setUserData(this);
        timeDefineMario=false;
    }
    public TextureRegion getFrame(float dt){
        currentState=getState();
     TextureRegion region;
        switch(currentState) {
            case DEAD:
                region=marioDead;
                break;
            case GROWING:
                region=(TextureRegion)growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation=false;
                break;

            case JUMPING:
                region=marioIsBig?bigMarioJump: marioJump;
                break;

            case RUNNING:
                region= marioIsBig?(TextureRegion)bigMarioRun.getKeyFrame(stateTimer,true):(TextureRegion) marioRun.getKeyFrame(stateTimer,true);
                break;

                case FALLING:
                    case STANDING:
            default:
                region=marioIsBig?bigMarioStand:marioStand;


        }
        if((b2body.getLinearVelocity().x<0||!stateRight)&&!region.isFlipX()){
            region.flip(true,false);
            stateRight=false;
        }
        else if((b2body.getLinearVelocity().x>0|| stateRight)&&region.isFlipX()){
            region.flip(true, false);
            stateRight=true;

        }
        stateTimer=currentState==previousState? stateTimer+dt:0;
        previousState=currentState;
        return  region;
    }

    public State getState(){
        if(marioisDead)
            return State.DEAD;
        else if(runGrowAnimation)
            return State.GROWING;
       else if(b2body.getLinearVelocity().y>0||(b2body.getLinearVelocity().y<0&&previousState==State.JUMPING))
            return State.JUMPING;
       else if(b2body.getLinearVelocity().y<0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x!=0)
            return State.RUNNING;
        else return State.STANDING;
    }
    public void grow(){
        runGrowAnimation=true;
        marioIsBig=true;
        timeDefineMario=true;
        setBounds(getX(),getY(),getWidth(),getHeight()*2);
    }
    public boolean IsBig(){
        return marioIsBig;
    }
    public void hit(Enemy enemy){
        if(enemy instanceof Turtle&&((Turtle)enemy).getCurrentState()== Turtle.State.STANDING_SHELL){
            ((Turtle)enemy).kick(this.getX()<=enemy.getX()?Turtle.KICK_RIGHT_SPEED:Turtle.KICK_LEFT_SPEED);
        }
        else if(marioIsBig){

         marioIsBig=false;
        timeSmallMario=true;
            MarioBros.manager.get("audio/SOUNDS/powerdown.wav", Sound.class).play();
        setBounds(getX(),getY(),getWidth(),getHeight()/2);
    }
    else
        { marioisDead=true;

            MarioBros.manager.get("audio/MUSIC/mario_music.ogg",Music.class).stop();
            MarioBros.manager.get("audio/SOUNDS/mariodie.wav", Sound.class).play();
    Filter filter=new Filter();
    filter.categoryBits=MarioBros.MARIO_BIT;
    filter.maskBits=MarioBros.NOTHING_BIT;
    for(Fixture fixture: b2body.getFixtureList()){
        fixture.setFilterData(filter);
    }
    b2body.applyLinearImpulse(new Vector2(0,4f),b2body.getWorldCenter(),true);}}

    public void redefineMario(){

        Vector2 position=b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef=new BodyDef();
        bdef.position.set(position);
        bdef.type= BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(6/MarioBros.PPM);
        fdef.filter.categoryBits=MarioBros.MARIO_BIT;
        fdef.filter.maskBits=MarioBros.BRICK_BIT|MarioBros.COIN_BIT|MarioBros.GROUND_BIT|MarioBros.ENEMY_BIT|MarioBros.OBJECT_BIT
                |MarioBros.ENEMY_HEAD_BIT|MarioBros.ITEM_BIT;
        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);


        EdgeShape head=new EdgeShape();
        head.set(new Vector2(-2/MarioBros.PPM,8/MarioBros.PPM),new Vector2(2/MarioBros.PPM,6/MarioBros.PPM));
        fdef.shape=head;
        fdef.filter.categoryBits=MarioBros.MARIO_HEAD_BIT;
        fdef.isSensor=true;
        b2body.createFixture(fdef).setUserData(this);
        timeSmallMario=false;
    }
    public void defineMario(){
        BodyDef bdef=new BodyDef();
       bdef.position.set(200/ MarioBros.PPM,100/MarioBros.PPM);
        bdef.type= BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(6/MarioBros.PPM);
        fdef.filter.categoryBits=MarioBros.MARIO_BIT;
        fdef.filter.maskBits=MarioBros.BRICK_BIT|MarioBros.COIN_BIT|MarioBros.GROUND_BIT|MarioBros.ENEMY_BIT|MarioBros.OBJECT_BIT
                |MarioBros.ENEMY_HEAD_BIT|MarioBros.ITEM_BIT;
        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);


        EdgeShape head=new EdgeShape();
        head.set(new Vector2(-2/MarioBros.PPM,8/MarioBros.PPM),new Vector2(2/MarioBros.PPM,8/MarioBros.PPM));
        fdef.shape=head;
        fdef.filter.categoryBits=MarioBros.MARIO_HEAD_BIT;
        fdef.isSensor=true;
        b2body.createFixture(fdef).setUserData(this);



    }

}
