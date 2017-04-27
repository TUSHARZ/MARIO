package com.tg.mariobros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tg.mariobros.MarioBros;

/**
 * Created by TUSHAR on 11-03-2017.
 */

public class end implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;

    public end(Game game){
        this.game=game;
        viewport=new FitViewport(MarioBros.V_WIDTH,MarioBros.V_HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,((MarioBros)game).batch);
        Label.LabelStyle font=new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table=new Table();
        table.center();
        table.setFillParent(true);
        Label gameOver=new Label("The End",font);
        Label playAgain=new Label("PlayAgain",font);
        table.add(gameOver).expandX();
        table.row();
        table.add(playAgain).expandX().padTop(10f);
        stage.addActor(table);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            game.setScreen(new PlayScreen((MarioBros)game));
            dispose();
        }
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();

    }
}
