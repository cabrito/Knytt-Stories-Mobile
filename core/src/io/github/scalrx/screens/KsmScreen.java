package io.github.scalrx.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.gui.OnscreenController;
import io.github.scalrx.screendata.ObjectData;
import io.github.scalrx.screendata.Tiler;

public class KsmScreen implements Screen
{

    // Set up how our level will display
    final KnyttStories game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private OnscreenController controller;

    // Attributes for the particular KsmScreen we're on
    private int xID, yID;           // Identifies where we are in the world
    private byte atmosAID;          // Atmospheric sounds A
    private byte atmosBID;          // Atmospheric sounds B
    private byte musicID;           // Music for this KsmScreen

    // Assets for the particular KsmScreen we're on
    private final Tiler tiler;
    private final ObjectData objects;

    BitmapFont font = new BitmapFont();

    /***********************************************************************************************			 Constructors */
	////////////////////////////////////////////////////////////////////////////////////////////////
    public KsmScreen(final KnyttStories game, int xID, int yID)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        this.game = game;
        this.xID = xID;
        this.yID = yID;

        camera = new OrthographicCamera();
        camera.position.set(24*25/2.0f,24*10/2.0f,0);
        //viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);
        viewport = new FitViewport(24*25, 24*10, camera);
        controller = new OnscreenController(game.batch);
        tiler = new Tiler(game.assetManager, game.currWorld);
        objects = new ObjectData(game.currWorld);

        // Assemble data and layers
        setAudioBytes();
        createScene();

        // Now that we have the musicID, atmosA, and atmosB bytes, try loading such audio files
        if((musicID & 0xFF) > 0) {
            game.audio.loadMusic(musicID);
        }
        if((atmosAID & 0xFF) > 0) {
            game.audio.loadAmbience(atmosAID);
        }
        if((atmosBID & 0xFF) > 0) {
            game.audio.loadAmbience(atmosBID);
        }
    }

	/*********************************************************************************************** Methods for assembling the screen */
	//
	////////////////////////////////////////////////////////////////////////////////////////////////
	private void createScene()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		tiler.generateTiledMap(xID, yID);
		objects.placeObjects(xID,yID);
	}

	// Used for setting the relevant audio. TODO: Can this be separated better?
	////////////////////////////////////////////////////////////////////////////////////////////////
	private void setAudioBytes()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		if(game.currWorld.getMap().screenOffsetExists(xID, yID)) {
			final int LAYER_SIZE = 250;
			final int OBJECT_LAYER_SIZE = LAYER_SIZE * 2;
			final int NUMBER_OF_SCENE_LAYERS = 4;
			final int NUMBER_OF_OBJECT_LAYERS = 4;

			// Open Map.bin.raw as a byte array
			FileHandle mapFile = Gdx.files.external(game.currWorld.getFiles().mapBin(true));
			byte[] mapFileBytes = mapFile.readBytes();

			// Seek the specific location in the Map file
			int attributeLocation = game.currWorld.getMap().getScreenOffset(xID, yID) +
					(LAYER_SIZE * NUMBER_OF_SCENE_LAYERS) + (OBJECT_LAYER_SIZE * NUMBER_OF_OBJECT_LAYERS);
			int cursorPosition = attributeLocation + 2;
			atmosAID = mapFileBytes[cursorPosition++];
			atmosBID = mapFileBytes[cursorPosition++];
			musicID = mapFileBytes[cursorPosition];
		} else {
			// Initialize audio attributes
			atmosAID = 0;
			atmosBID = 0;
			musicID = 0;
		}
	}

    /***********************************************************************************************			 LibGDX Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void show()
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
    	// Check whether or not we need to manipulate the music
    	if(!game.audio.isSongPlaying(musicID))
    	{
			if(!game.audio.getMusic().isPlaying())
			{
				game.audio.playMusic(musicID);
			} else {
				game.audio.setFading(true);
			}
		}
		game.audio.playAmbiance(atmosAID, atmosBID);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void render(float delta)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
    	// Handle audio fading if necessary
		game.audio.handleFadeout(delta, musicID);

        game.batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0f / 255f, 0 / 255f, 0f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        tiler.getTiledMapRenderer().setView(camera);
        tiler.render();


        // Render the KsmScreen in the desired order TODO: Leave space for Juni
        game.batch.begin();

        // Print the current KsmScreen coordinates in the bottom left of the screen.
        font.draw(game.batch, "(" + xID + ", " + yID + ")", 10, 40);
        font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);

        // Finish drawing to the screen
        game.batch.end();

        // Draw controller to screen for mobile platforms
        if(Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS)
            controller.draw();

        // Movement-related controls for us to use temporarily
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || controller.isLeftPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game,xID - 1, yID));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || controller.isRightPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game,xID + 1, yID));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || controller.isUpPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game, xID, yID - 1));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || controller.isDownPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game, xID, yID + 1));
            dispose();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void resize(int width, int height)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        viewport.update(width, height);
        controller.resize(width, height);
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
        tiler.dispose();
        objects.dispose();
    }
}
