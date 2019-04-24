package io.github.scalrx.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.KsmAudio;
import io.github.scalrx.gui.button.GuiButtonLarge;

public class MenuScreen implements Screen
{

	// Members
    private final KnyttStories game;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;

    // Textures
	Texture guiButtonLarge;
	Texture playIcon;
	Texture tutorialIcon;
	Texture installIcon;
	Texture downloadIcon;
	Texture settingsIcon;
	Texture aboutIcon;
	Texture graphic;

    /***********************************************************************************************			 Constructors */
    // Initialize our menu screen
	////////////////////////////////////////////////////////////////////////////////////////////////
    public MenuScreen(final KnyttStories game)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        // Initialize our members
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT);
        viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        guiButtonLarge = game.files.resources().button("large");
        playIcon = game.files.resources().icon("play");
        tutorialIcon = game.files.resources().icon("tutorial");
        installIcon = game.files.resources().icon("install");
        downloadIcon = game.files.resources().icon("download");
        settingsIcon  = game.files.resources().icon("settings");
        aboutIcon = game.files.resources().icon("about");
        loadAssets();

        graphic = game.assetManager.get("graphic.png", Texture.class);
        Image ksmGraphic = new Image(graphic);

        /**     Assemble our GUI     */
        // Fill out the skin each of our buttons will use
        Skin menuBtnSkin = new Skin();
        menuBtnSkin.add("gui-button-large", guiButtonLarge);
        menuBtnSkin.add("gui-button-large", game.assetManager.get("defaultFont.ttf", BitmapFont.class));

        GuiButtonLarge playButton = new GuiButtonLarge(playIcon, "Play", menuBtnSkin);
        GuiButtonLarge tutorialButton = new GuiButtonLarge(tutorialIcon, "Tutorial", menuBtnSkin);
        GuiButtonLarge installButton = new GuiButtonLarge(installIcon, "Install", menuBtnSkin);
        GuiButtonLarge downloadButton = new GuiButtonLarge(downloadIcon, "Download", menuBtnSkin);
        GuiButtonLarge settingsButton = new GuiButtonLarge(settingsIcon, "Settings", menuBtnSkin);
        GuiButtonLarge aboutButton = new GuiButtonLarge(aboutIcon, "About", menuBtnSkin);

        // Set the position of the buttons
        Table table = new Table();
        table.left().bottom();
        table.add(playButton).pad(30, 30, 15, 30);
        table.add(tutorialButton).pad(30, 30, 15, 30).row();
        table.add(installButton).pad(30, 30, 30, 30);
        table.add(downloadButton).pad(30, 30, 30, 30).row();
        table.add(settingsButton).pad(15, 30, 30, 30);
        table.add(aboutButton).pad(15, 30, 30, 30);

        ksmGraphic.setScale(2f);



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


        // Lastly, add the containers to the stage
        stage.addActor(ksmGraphic);
        stage.addActor(table);

        // Load the various components of the GUI we expect to use
        this.game.assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));

        this.game.audio = new KsmAudio(game.assetManager, game.files);
        game.audio.loadMusic((byte)20);
    }

    /***********************************************************************************************			 Methods */
	////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadAssets()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        game.assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        game.assetManager.load("graphic.png", Texture.class);                   // TODO: TEMPORARY. DELETE!!!!
        game.assetManager.finishLoading();
    }

    /***********************************************************************************************			 LibGDX Methods */
	////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void show()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
	    game.audio.playMusic((byte)20);
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            if(!game.getPermissions().isReadPermissionEnabled())
            {
            	// Maybe say something to the user about needing permissions
            }
            if(!game.getPermissions().isWritePermissionEnabled())
            {
				// Maybe say something to the user about needing permissions
            }
        }
    }

	////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void render(float delta)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        // Clear the screen for drawing the next frame
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera for any movement that may occur
        viewport.getCamera().update();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // Begin drawing our GUI to the screen
        game.batch.begin();

        // Make sure to only draw if we have all the assets loaded appropriately.

        // End all drawing from the SpriteBatch
        game.batch.end();
        stage.act();
        stage.draw();

    }

	////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void resize(int width, int height)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        viewport.update(width, height,true);
    }

	////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void pause()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{

    }

	////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void resume()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{

    }

	////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void hide()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{

    }

	////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void dispose()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		game.files.resources().dispose(playIcon);
		game.files.resources().dispose(installIcon);
		game.files.resources().dispose(tutorialIcon);
		game.files.resources().dispose(downloadIcon);
		game.files.resources().dispose(settingsIcon);
		game.files.resources().dispose(aboutIcon);
		game.files.resources().dispose(guiButtonLarge);
        game.assetManager.unload("graphic.png");
        stage.dispose();
    }
}
