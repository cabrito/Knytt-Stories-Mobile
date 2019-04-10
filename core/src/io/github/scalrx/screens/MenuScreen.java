package io.github.scalrx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.World;
import io.github.scalrx.KsmMusic;
import io.github.scalrx.utilities.PixelPerfectViewport;

public class MenuScreen implements Screen {

    private final KnyttStories game;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;

    // Initialize our menu screen
    public MenuScreen(final KnyttStories game) {
        // Initialize our members
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH,KnyttStories.V_HEIGHT);
        viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Texture guiButtonLarge;
        Texture playIcon;
        Texture tutorialIcon;
        Texture installIcon;
        Texture downloadIcon;
        Texture settingsIcon;
        Texture aboutIcon;
        Texture graphic;

        loadAssets();

        guiButtonLarge = game.assetManager.get("System/Gui_btn_large.png", Texture.class);
        playIcon = game.assetManager.get("System/icons/play_icon.png");
        tutorialIcon = game.assetManager.get("System/icons/tutorial_icon.png");
        installIcon = game.assetManager.get("System/icons/install_icon.png");
        downloadIcon = game.assetManager.get("System/icons/download_icon.png");
        settingsIcon = game.assetManager.get("System/icons/settings_icon.png");
        aboutIcon = game.assetManager.get("System/icons/about_icon.png");
        graphic = game.assetManager.get("graphic.png");
        Image ksmGraphic = new Image(graphic);

        /**     Assemble our GUI     */
        // Fill out the skin each of our buttons will use
        Skin menuBtnSkin = new Skin();
        final float ICON_SIZE = 50;
        menuBtnSkin.add("large-gui-button", guiButtonLarge);
        menuBtnSkin.add("large-gui-button", game.assetManager.get("defaultFont.ttf", BitmapFont.class));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = menuBtnSkin.newDrawable("large-gui-button");
        textButtonStyle.down = menuBtnSkin.newDrawable("large-gui-button", 0.0f,0.0f,0.0f,0.5f);
        textButtonStyle.font = menuBtnSkin.getFont("large-gui-button");

        TextButton playButton = new TextButton("Play", textButtonStyle);
        playButton.clearChildren();                     // Reorganize the layout of the ImageTextButton
        playButton.add(new Image(playIcon)).size(ICON_SIZE).row();
        playButton.add(playButton.getLabel());

        TextButton tutorialButton = new TextButton("Tutorial", textButtonStyle);
        tutorialButton.clearChildren();                     // Reorganize the layout of the ImageTextButton
        tutorialButton.add(new Image(tutorialIcon)).size(ICON_SIZE).row();
        tutorialButton.add(tutorialButton.getLabel());

        TextButton installButton = new TextButton("Install", textButtonStyle);
        installButton.clearChildren();                     // Reorganize the layout of the ImageTextButton
        installButton.add(new Image(installIcon)).size(ICON_SIZE).row();
        installButton.add(installButton.getLabel());

        TextButton downloadButton = new TextButton("Download", textButtonStyle);
        downloadButton.clearChildren();                     // Reorganize the layout of the ImageTextButton
        downloadButton.add(new Image(downloadIcon)).size(ICON_SIZE).row();
        downloadButton.add(downloadButton.getLabel());

        TextButton settingsButton = new TextButton("Settings", textButtonStyle);
        settingsButton.clearChildren();                     // Reorganize the layout of the ImageTextButton
        settingsButton.add(new Image(settingsIcon)).size(ICON_SIZE).row();
        settingsButton.add(settingsButton.getLabel());

        TextButton aboutButton = new TextButton("About", textButtonStyle);
        aboutButton.clearChildren();                     // Reorganize the layout of the ImageTextButton
        aboutButton.add(new Image(aboutIcon)).size(ICON_SIZE).row();
        aboutButton.add(aboutButton.getLabel());

        // Set the position of the ImageTextButtons
        ksmGraphic.setScale(2f);
        playButton.setPosition(394f,340f);
        tutorialButton.setPosition(624f,340f);
        installButton.setPosition(394f,185f);
        downloadButton.setPosition(624f,185f);
        settingsButton.setPosition(394f,30f);
        aboutButton.setPosition(624f,30f);
        ksmGraphic.setPosition(-128,0);


        // TODO: IT WORKS! Now decipher *why* it works...
        // Now, add items to the columns...

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });

        /*playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new KsmScreen(game, 1000, 1000));
                //game.audio.setFiles(game.currWorld.files);

                // Temporary. For when we actually load a world
                game.audio.stopMusic();
                game.audio.stopAmbience();
                dispose();
            }
        });*/


        // Lastly, add the container to the stage
        stage.addActor(playButton);
        stage.addActor(tutorialButton);
        stage.addActor(installButton);
        stage.addActor(downloadButton);
        stage.addActor(settingsButton);
        stage.addActor(aboutButton);
        stage.addActor(ksmGraphic);

        // Load the various components of the GUI we expect to use
        this.game.assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        this.game.assetManager.load(Gdx.files.internal("System/Gui_btn_medium.png").path(), Texture.class);

        // TODO: For now, we initialize our currWorld so that we have something to play

        this.game.audio = new KsmMusic(game.assetManager, game.files);
        game.audio.loadMusic((byte)20);
    }

    /**     Methods     */
    public void loadAssets() {
        game.assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        game.assetManager.load("System/Gui_btn_large.png", Texture.class);
        game.assetManager.load("System/icons/play_icon.png", Texture.class);
        game.assetManager.load("System/icons/tutorial_icon.png", Texture.class);
        game.assetManager.load("System/icons/install_icon.png", Texture.class);
        game.assetManager.load("System/icons/download_icon.png", Texture.class);
        game.assetManager.load("System/icons/settings_icon.png", Texture.class);
        game.assetManager.load("System/icons/about_icon.png", Texture.class);
        game.assetManager.load("graphic.png", Texture.class);                   // TODO: TEMPORARY. DELETE!!!!
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

        // Make sure to only draw if we have all the assets loaded appropriately.
        if(game.assetManager.update())
            game.audio.playMusic((byte)20, delta);

        // End all drawing from the SpriteBatch
        game.batch.end();
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        // TODO: Need to add a viewport
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
        game.assetManager.unload("System/Gui_btn_large.png");
        game.assetManager.unload("System/icons/play_icon.png");
        game.assetManager.unload("System/icons/tutorial_icon.png");
        game.assetManager.unload("System/icons/install_icon.png");
        game.assetManager.unload("System/icons/download_icon.png");
        game.assetManager.unload("System/icons/settings_icon.png");
        game.assetManager.unload("System/icons/about_icon.png");
        game.assetManager.unload("graphic.png");
        stage.dispose();
    }
}
