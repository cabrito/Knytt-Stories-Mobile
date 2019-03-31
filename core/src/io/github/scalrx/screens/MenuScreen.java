package io.github.scalrx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.World;
import io.github.scalrx.utilities.KsmMusic;

public class MenuScreen implements Screen {

    final KnyttStories game;
    OrthographicCamera camera;

    // Initialize our menu screen
    public MenuScreen(final KnyttStories game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH,KnyttStories.V_HEIGHT);

        // Load the various components of the GUI we expect to use
        this.game.assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        this.game.assetManager.load(Gdx.files.internal("System/Gui_btn_medium.png").path(), Texture.class);

        // TODO: For now, we initialize our currWorld so that we have something to play
        this.game.currWorld = new World(game.files);
        this.game.currWorld.setAuthor("UncleSporky");
        this.game.currWorld.setWorldName("Don't Eat the Mushroom");
        this.game.currWorld.initMap();

        //this.game.currWorld.files = new KS_Files(game.currWorld.getAuthor(), game.currWorld.getWorldName());
        this.game.audio = new KsmMusic(game.assetManager, game.files);
        //game.audio.loadMusic((byte)20);
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
            //game.audio.playMusic((byte)20, delta);
            //game.font.draw(game.batch, "Font Test", 100, 150);
            //game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        }

        // End all drawing from the SpriteBatch
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new KsmScreen(game, 1002, 1000));
            //game.audio.setFiles(game.currWorld.files);

            // Temporary. For when we actually load a world
            game.audio.stopMusic();
            game.audio.stopAmbience();
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
