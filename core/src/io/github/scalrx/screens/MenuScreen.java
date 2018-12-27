package io.github.scalrx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import io.github.scalrx.KnyttStories;

public class MenuScreen implements Screen {

    final KnyttStories game;
    OrthographicCamera camera;
    Texture guiButton;  // REMOVE?!

    public MenuScreen(final KnyttStories game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH,KnyttStories.V_HEIGHT);
        guiButton = new Texture("System/Gui_btn_medium.png");   // REMOVE?!
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(guiButton,(KnyttStories.V_WIDTH/2) - guiButton.getWidth()/2,5);
        //game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        //game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new KSScreen(game, 1025, 1002));
            dispose();
        }
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
        guiButton.dispose();    // REMOVE?!
    }
}
