package com.tg.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tg.mariobros.MarioBros;
import com.tg.mariobros.Sprites.Mario;

/**
 * Created by TUSHAR on 12-02-2017.
 */
public class Controller {
    Viewport viewport;
    OrthographicCamera cam;
    Stage stage;
    Stage stage1;
    public float statetime;
    public boolean jump,rit,lef;
    public Controller(Batch batch) {
        viewport = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
       stage1=new Stage(viewport,batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        Table table1=new Table();


        Image up = new Image(new Texture("up.png"));
        up.setSize(30 , 30);
        up.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jump = true;
                MarioBros.manager.get("audio/SOUNDS/jump.wav",Sound.class).play();



                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                jump = false;
            }

        });





        table1.bottom().left();
        stage.addActor(table1);
        table.setPosition(360,23);

        Image right = new Image(new Texture("right.png"));
        right.setSize(30 , 30 );
        right.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

               rit = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rit= false;
            }

        });
        Image left=new Image(new Texture("left.png"));
        left.setSize(30, 30 );
        left.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lef = true;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                lef = false;
            }

        });
        table.add();
        table.add(up).size(up.getWidth(),up.getHeight());
        table.add();
        table1.add();
        table1.add();
        table1.add();
        table1.row().pad(5,5,5,5);
        table1.add(left).size(left.getWidth(),left.getHeight());
        table1.add();
        table1.add(right).size(right.getWidth(),right.getHeight());
        table.row().padBottom(5);
        stage.addActor(table);




        }
    public void draw(){
        stage.draw();
       stage1.draw();
    }
    public void dispose(){
        stage1.dispose();
        stage.dispose();
    }

    public boolean isJump() {

        return jump;
    }

    public boolean isRit() {
        return rit;
    }

    public boolean isLef() {
        return lef;
    }
    public void resize(int width,int height)
    {viewport.update(width,height);}

}