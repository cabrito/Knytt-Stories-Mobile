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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.ini4j.Wini;

import java.io.IOException;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.World;
import io.github.scalrx.gui.button.GuiButtonLevel;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * LevelSelectScreen.java
 * Created by: scalr at 11:40 AM, 4/9/19
 *
 * Used for selecting which level the user wishes to play.
 *
 **************************************************************************************************/
public class LevelSelectScreen implements Screen {

    private final KnyttStories game;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;

    public LevelSelectScreen(final KnyttStories game) {

        // Prepare our variables
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH,KnyttStories.V_HEIGHT);
        viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);

        // Load our assets we'll use
        loadAssets();
        Texture guiBtnLevel = game.assetManager.get("System/Gui_btn_level.png", Texture.class);

        // Prepare the stage for level selection
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // Create the look for each element in the scrollpane
        Skin buttonSkin = new Skin();
        final float BUTTON_SPACING = 5f;
        buttonSkin.add("gui-button-level", guiBtnLevel);
        buttonSkin.add("gui-button-level", game.assetManager.get("smallFont.ttf", BitmapFont.class));
        buttonSkin.add("description-font", game.assetManager.get("smallGrayFont.ttf", BitmapFont.class));

        // Make the table used in the scrollpane
        Table table = new Table();
        table.left();

        FileHandle[] files = Gdx.files.external("Knytt Stories Mobile/").list();
        try {
            for (final FileHandle fh : files) {
                if (fh.isDirectory()) {
                    if (Gdx.files.external(fh.path() + "/World.ini").exists()) {
                        Wini ini = new Wini(Gdx.files.external(fh.path() + "/World.ini").file());
                        final String name = ini.get("World", "Name", String.class);
                        final String author = ini.get("World", "Author", String.class);
                        final String description = ini.get("World", "Description", String.class);

                        // Load stuff
                        game.assetManager.setLoader(Texture.class, new TextureLoader(new ExternalFileHandleResolver()));
                        game.assetManager.load(Gdx.files.external(fh.path() + "/Icon.png").path(), Texture.class);
                        game.assetManager.finishLoading();
                        Texture icon = game.assetManager.get(Gdx.files.external(fh.path() + "/Icon.png").path(), Texture.class);

                        // Make our button and add it to the scrollpane table
                        GuiButtonLevel button = new GuiButtonLevel(icon, name + " (" + author + ")",
                                description, buttonSkin);
                        button.addListener(new ChangeListener() {
                            // TODO: IMPLEMENT FILE SELECTION/CUTSCENES BEFORE GOING TO THE GAME
                            @Override
                            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                                try {
                                    Wini ini = new Wini(Gdx.files.external(fh.path() + "/DefaultSavegame.ini").file());
                                    int xID = ini.get("Positions","X Map", int.class);
                                    int yID = ini.get("Positions", "Y Map", int.class);

                                    game.currWorld = new World(game.files);
                                    game.currWorld.setAuthor(author);
                                    game.currWorld.setWorldName(name);
                                    game.currWorld.initMap();
                                    game.setScreen(new KsmScreen(game, xID, yID));
                                    //game.audio.setFiles(game.currWorld.files);

                                    // Temporary. For when we actually load a world
                                    game.audio.stopMusic();
                                    game.audio.stopAmbience();
                                    dispose();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        table.add(button).padBottom(BUTTON_SPACING).row();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setPosition(50f,0);
        scrollPane.setSize(300,200);

        stage.addActor(scrollPane);
    }

    private void loadAssets() {
        // Load internal resources
        game.assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        game.assetManager.load("System/Gui_btn_level.png", Texture.class);
        game.assetManager.finishLoading();
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
        viewport.getCamera().update();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // Begin drawing our GUI to the screen
        game.batch.begin();

        // End all drawing from the SpriteBatch
        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        viewport.update(width,height,true);
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
        game.assetManager.unload("System/Gui_btn_level.png");
        stage.dispose();
    }
}
