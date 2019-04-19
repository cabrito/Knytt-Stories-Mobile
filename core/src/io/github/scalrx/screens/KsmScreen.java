package io.github.scalrx.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.gui.OnscreenController;
import io.github.scalrx.screendata.Tiler;

public class KsmScreen implements Screen {

    // Set up how our level will display
    final KnyttStories game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private OnscreenController controller;

    // Attributes for the particular KsmScreen we're on
    private int xID, yID;       // TODO: WHEN DO THESE NEED TO BE SET? CONVERT TO PAIR OBJECT
    private byte tsetAID;      // 0x00 to 0x7F in tile data
    private byte tsetBID;      // 0x80 to 0xFF in tile data
    private byte atmosAID;        // Atmospheric sounds A
    private byte atmosBID;        // Atmospheric sounds B
    private byte musicID;         // Music for this KsmScreen
    private byte backgroundID;    // Background picture

    // Assets for the particular KsmScreen we're on
    private final Tiler tiler;
    private byte[][][] objectData = new byte[4][10][50];

    BitmapFont font = new BitmapFont();

    // Constructor
    public KsmScreen(final KnyttStories game, int xID, int yID) {
        this.game = game;
        this.xID = xID;
        this.yID = yID;

        camera = new OrthographicCamera();
        camera.position.set(24*25/2.0f,24*10/2.0f,0);
        //viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);
        viewport = new FitViewport(24*25, 24*10, camera);
        controller = new OnscreenController(game.batch);
        tiler = new Tiler(game.assetManager, game.currWorld);

        // Assemble data and layers
        assembleData();

        assembleScenery();

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

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0f / 255f, 0 / 255f, 0f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        tiler.getTiledMapRenderer().setView(camera);
        tiler.render();

        // Play game music and ambiance
        game.audio.playMusic(musicID, delta);
        game.audio.playAmbiance(atmosAID, atmosBID);

        // Render the KsmScreen in the desired order TODO: Leave space for Juni
        game.batch.begin();

        // Print the current KsmScreen coordinates in the bottom left of the screen.
        font.draw(game.batch, "(" + xID + ", " + yID + ")", 10, 40);
        font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);

        // Finish drawing to the screen
        game.batch.end();
        if(Gdx.app.getType() == Application.ApplicationType.Android)
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
            game.setScreen(new KsmScreen(game,xID, yID - 1));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || controller.isDownPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game,xID, yID + 1));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        controller.resize(width, height);
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
        //texture.dispose();

        tiler.dispose();
        //tilesetA.dispose();
        //tilesetB.dispose();
        //game.assetManager.unload(game.files.tileset(tsetAID));
        //game.assetManager.unload(game.files.tileset(tsetBID));
        //game.assetManager.unload(game.files.gradient(backgroundID));
        //bg.dispose();
    }

    /**
     * Methods for assembling the KsmScreen
     */
    private void assembleScenery() {
        tiler.generateTiledMap(xID, yID);
    }

    // -----METHODS FOR ASSEMBLING DATA FOR KsmScreen-----
    private void assembleData() {
        //TODO: FIX path!!!!
        if(game.currWorld.getMap().screenOffsetExists(xID,yID)) {
            // Open Map.bin.raw as a byte array
            FileHandle mapFile = Gdx.files.external(game.files.mapBin(true));
            byte[] mapFileBytes = mapFile.readBytes();

            // Seek the specific location in the Map file
            int cursorPosition = game.currWorld.getMap().getScreenOffset(xID, yID);

            // Copy byte data for layers 0 - 3
            for (int scnLayer = 0; scnLayer < 4; scnLayer++) {
                for (int row = 0; row < 10; row++) {
                    for (int column = 0; column < 25; column++, cursorPosition++) {
                        //sceneryData[scnLayer][row][column] = mapFileBytes[cursorPosition];
                    }
                }
            }

            // Copy in byte data for layers 4 - 7
            for (int objLayer = 4; objLayer <= 7; objLayer++) {
                // Read object number
                for (int row = 0; row < 10; row++) {
                    for (int column = 0; column < 25; column++, cursorPosition++) {
                        objectData[objLayer - 4][row][column] = mapFileBytes[cursorPosition];
                    }
                }

                // Read bank number
                for (int row = 0; row < 10; row++) {
                    for (int column = 25; column < 50; column++, cursorPosition++) {
                        objectData[objLayer - 4][row][column] = mapFileBytes[cursorPosition];
                    }
                }
            }

            // Initialize KsmScreen attributes
            tsetAID = mapFileBytes[cursorPosition++];
            tsetBID = mapFileBytes[cursorPosition++];
            atmosAID = mapFileBytes[cursorPosition++];
            atmosBID = mapFileBytes[cursorPosition++];
            musicID = mapFileBytes[cursorPosition++];
            backgroundID = mapFileBytes[cursorPosition++];

        } else {
            // Initialize KsmScreen attributes
            tsetAID = 0;
            tsetBID = 0;
            atmosAID = 0;
            atmosBID = 0;
            musicID = 0;
            backgroundID = 0;
        }
    }
}
