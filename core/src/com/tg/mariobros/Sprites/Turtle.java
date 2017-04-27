package com.tg.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Screens.PlayScreen;

/**
 * Created by TUSHAR on 12-02-2017.
 */
public class Turtle extends Enemy {
    public  static final int KICK_LEFT_SPEED=-2;
    public  static final int KICK_RIGHT_SPEED=2;
    private float deadRotationDeg;
    public enum State{WALKING,STANDING_SHELL,MOVING_SHELL,DEAD};
    public State currentState;
    public State previousState;
    private Array<TextureRegion> frames;
    private Animation walkAnimation;
    private float stateTime;
    private boolean setToDestroy;
    private boolean destroyed;
    private TextureRegion shell;


    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames=new Array<TextureRegion>();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),0,0,16,24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),16,0,16,24));
        walkAnimation=new Animation(0.4f,frames);
        shell=new TextureRegion(screen.getAtlas().findRegion("turtle"),64,0,16,24);
        currentState=previousState= State.WALKING;
        setBounds(getX(),getY(),16/ MarioBros.PPM,24/MarioBros.PPM);
        deadRotationDeg=0;


    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef=new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type= BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(6/MarioBros.PPM);
        fdef.filter.categoryBits=MarioBros.ENEMY_BIT;
        fdef.filter.maskBits=MarioBros.BRICK_BIT|MarioBros.COIN_BIT|MarioBros.GROUND_BIT|MarioBros.OBJECT_BIT|MarioBros.ENEMY_BIT|MarioBros.MARIO_BIT;
        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);
//create head

        PolygonShape head=new PolygonShape();
        Vector2[] vertice= new Vector2[4];
        vertice[0]=new Vector2(-4,9).scl(1/MarioBros.PPM);
        vertice[1]=new Vector2(4,9).scl(1/MarioBros.PPM);
        vertice[2]=new Vector2(-3,2).scl(1/MarioBros.PPM);
        vertice[3]=new Vector2(3,2).scl(1/MarioBros.PPM);
        head.set(vertice);
        fdef.shape=head;
        fdef.restitution=0.5f;
        fdef.filter.categoryBits=MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);




    }
    public void onEnemyHit(Enemy enemy){

        if(enemy instanceof Turtle){
            if((((Turtle) enemy).getCurrentState()==State.MOVING_SHELL&&currentState!=State.MOVING_SHELL)){
                killed();}
        else if(currentState!=State.MOVING_SHELL&&(((Turtle) enemy).currentState==State.WALKING))
        return;
        else
                reverse(true,false);
        }
       else if(currentState!=State.MOVING_SHELL)
            reverse(true,false);


    }

    private TextureRegion getFrame(float dt){
        TextureRegion region;
        switch (currentState){
        case WALKING:
            region=(TextureRegion)walkAnimation.getKeyFrame(stateTime,true);
            break;
            case STANDING_SHELL:
                case MOVING_SHELL:
                default:
                region=shell;
                break;
    }
        if(velocity.x>0&&region.isFlipX()==false){
            region.flip(true,false);}
        if(velocity.x<0&&region.isFlipX()==true){
            region.flip(true,false);}

        stateTime=currentState==previousState? stateTime+dt:0;
        previousState=currentState;
        return region;


    }


    @Override
    public void hitOnHead(Mario mario) {
        if(currentState!=State.STANDING_SHELL) {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }
        else
            kick(mario.getX()<=this.getX()?KICK_RIGHT_SPEED:KICK_LEFT_SPEED);
    }
   // public void draw(Batch batch){
       // if(!destroyed){
        //    super.draw(batch);
          //  destroyed=true;
       // }

   // }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if (currentState == State.STANDING_SHELL && stateTime > 5) {
            currentState = State.WALKING;
            velocity.x = 1;
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / MarioBros.PPM);
        if (currentState == State.DEAD){
            deadRotationDeg += 3;
        rotate(deadRotationDeg);
        if (stateTime > 5 && !destroyed)
            world.destroyBody(b2body);
        destroyed = true;
    }
        else
        b2body.setLinearVelocity(velocity);
    }
    public void kick(int speed){
        velocity.x=speed;
        currentState=State.MOVING_SHELL;
    }
    public State getCurrentState(){
        return currentState;
    }
    public void killed(){
        currentState=State.DEAD;
        Filter filter=new Filter();
        filter.maskBits=MarioBros.NOTHING_BIT;
        for(Fixture fixture:b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0,5f),b2body.getWorldCenter(),true);
    }
}
