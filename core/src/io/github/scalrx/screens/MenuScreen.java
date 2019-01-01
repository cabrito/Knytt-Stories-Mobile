package io.github.scalrx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import io.github.scalrx.KSFiles;
import io.github.scalrx.KnyttStories;
import io.github.scalrx.World;

public class MenuScreen implements Screen {

    final KnyttStories game;
    OrthographicCamera camera;

    public MenuScreen(final KnyttStories game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH,KnyttStories.V_HEIGHT);

        // Load the various components of the GUI we expect to use
        this.game.assetManager.load(Gdx.files.internal("System/Gui_btn_medium.png").path(), Texture.class);

        // TODO: For now, we initialize our world so that we have something to play
        game.world = new World("UncleSporky", "Don't Eat the Mushroom", 2);
        game.files = new KSFiles(game.world.getAuthor(), game.world.getWorldName());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear the screen for drawing the next frame
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera for any movement that may occur
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // Begin drawing our GUI to the screen
        game.batch.begin();

        // Make sure to only draw if we have all the assets loaded appropriately.
        if(game.assetManager.update()) {
            Texture guiButton = game.assetManager.get(Gdx.files.internal("System/Gui_btn_medium.png").path(), Texture.class);
            game.batch.draw(guiButton,(KnyttStories.V_WIDTH/2) - guiButton.getWidth()/2,5);
            //game.font.draw(game.batch, "Font Test", 100, 150);
            //game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        }

        // End all drawing from the SpriteBatch
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new KSScreen(game, 1000, 1000));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO: Need to add a viewport
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
        game.assetManager.unload(Gdx.files.internal("System/Gui_btn_medium.png").path());
        //guiButton.dispose();    // REMOVE?!
    }
}
