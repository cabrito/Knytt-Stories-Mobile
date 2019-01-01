package io.github.scalrx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

public class KSScreen implements Screen {

    // Set up how our level will display
    final KnyttStories game;
    private OrthographicCamera camera;
    private Viewport viewport;

    // Attributes for the particular KSScreen we're on
    private int xID, yID;       // TODO: WHEN DO THESE NEED TO BE SET? CONVERT TO PAIR OBJECT
    private byte tsetAID;      // 0x00 to 0x7F in tile data
    private byte tsetBID;      // 0x80 to 0xFF in tile data
    private byte atmosAID;        // Atmospheric sounds A
    private byte atmosBID;        // Atmospheric sounds B
    private byte musicID;         // Music for this KSScreen
    private byte backgroundID;    // Background picture

    // Assets for the particular KSScreen we're on
    private byte[][][] sceneryData = new byte[4][10][25];   // TODO: RENAME SINCE WE NEED TO IMPLEMENT OBJECTS ALSO!
    private byte[][][] objectData = new byte[4][10][50];
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private Texture tilesetA;
    private Texture tilesetB;
    private Texture bg;

    BitmapFont font = new BitmapFont();
    private final int[] BACKGROUND_LAYER = {4};
    private final int[] PRIMARY_LAYERS = {0,1,2,3};

    // Constructor
    public KSScreen(final KnyttStories game, int xID, int yID) {
        this.game = game;
        this.xID = xID;
        this.yID = yID;

        camera = new OrthographicCamera();
        camera.position.set(KnyttStories.V_WIDTH/2,KnyttStories.V_HEIGHT/2,0);
        viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);

        // Assemble data and layers
        assembleData();
        assembleScenery();

        // Now that we have the musicID, atmosA, and atmosB bytes, try loading such audio files
        if((musicID & 0xFF) > 0) {
            game.assetManager.load(game.files.music(musicID), Music.class);
        }
        if((atmosAID & 0xFF) > 0) {
            game.assetManager.load(game.files.ambiance(atmosAID), Music.class);
        }
        if((atmosBID & 0xFF) > 0) {
            game.assetManager.load(game.files.ambiance(atmosBID), Music.class);
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

        tiledMapRenderer.setView(camera);

        // Proceed only if all the assets we need have been loaded
        if(game.assetManager.update()) {
            /* Music and Ambiance */
            playMusicAndAmbiance(delta);
        }

        // Render the KSScreen in the desired order TODO: Leave space for Juni
        tiledMapRenderer.render(BACKGROUND_LAYER);
        // renderJuni();
        tiledMapRenderer.render(PRIMARY_LAYERS);
        game.batch.begin();

        // Print the current KSScreen coordinates in the bottom left of the screen.
        font.draw(game.batch, "(" + xID + ", " + yID + ")", 10, 20);
        //font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);

        // Finish drawing to the screen
        game.batch.end();

        // Movement-related controls for us to use temporarily
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            game.setScreen(new KSScreen(game,xID - 1, yID));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            game.setScreen(new KSScreen(game,xID + 1, yID));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            game.setScreen(new KSScreen(game,xID, yID - 1));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            game.setScreen(new KSScreen(game,xID, yID + 1));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);

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

        tiledMap.dispose();
        tilesetA.dispose();
        tilesetB.dispose();
        bg.dispose();
    }

    private void playMusicAndAmbiance(float delta) {
        // Logic for playing/stopping music
        if((musicID & 0xFF) > 0) {
            if(game.music != null) {
                if(game.music.isPlaying()) {
                    if(!game.assetManager.get(game.files.music(musicID), Music.class).isPlaying() || (game.music.getVolume() < 1f)) {
                        game.music.setVolume(game.music.getVolume() - 0.2f*delta);
                        if(game.music.getVolume() < 0.02f) {
                            game.music.stop();
                            game.music = game.assetManager.get(game.files.music(musicID), Music.class);
                            game.music.setVolume(1f);
                            game.music.play();
                        }
                    }
                } else {
                    game.music = game.assetManager.get(game.files.music(musicID), Music.class);
                    game.music.setVolume(1f);
                    game.music.play();
                }
            } else {
                game.music = game.assetManager.get(game.files.music(musicID), Music.class);
                game.music.setVolume(1f);
                game.music.play();
            }
        } else {
            if(game.music != null) {
                if(game.music.isPlaying()) {
                    game.music.setVolume(game.music.getVolume() - 0.2f*delta);
                    if(game.music.getVolume() < 0.02f) {
                        game.music.stop();
                        game.music.setVolume(1f);
                    }
                }

            }
        }

        // Logic for playing/stopping atmosA
        if((atmosAID & 0xFF) > 0) {
            if(game.assetManager.isLoaded(game.files.ambiance(atmosAID))) {
                if(!game.assetManager.get(game.files.ambiance(atmosAID), Music.class).isPlaying()) {
                    if(game.atmosA != null) {
                        game.atmosA.stop();
                        game.atmosA.dispose();
                    }
                    game.atmosA = game.assetManager.get(game.files.ambiance(atmosAID), Music.class);
                    game.atmosA.setLooping(true);
                    game.atmosA.play();
                }

            }
        } else {
            if(game.atmosA != null)
                game.atmosA.stop();
        }

        // Logic for playing/stopping atmosB
        if((atmosBID & 0xFF) > 0) {
            if(game.assetManager.isLoaded(game.files.ambiance(atmosBID))) {
                if(!game.assetManager.get(game.files.ambiance(atmosBID), Music.class).isPlaying()) {
                    if(game.atmosB != null) {
                        game.atmosB.stop();
                        game.atmosB.dispose();
                    }
                    game.atmosB = game.assetManager.get(game.files.ambiance(atmosBID), Music.class);
                    game.atmosB.setLooping(true);
                    game.atmosB.play();
                }

            }
        } else {
            if(game.atmosB != null)
                game.atmosB.stop();
        }
    }

    /**
     * Methods for assembling the KSScreen
     */
    private void assembleScenery() {

        // Load the appropriate tilesets A and B
        tilesetA = new Texture(game.files.tileset(tsetAID));
        tilesetB = new Texture(game.files.tileset(tsetBID));

        // Split up each tileset into 24x24 whole sections. Any section that is not whole will not be considered.
        TextureRegion[][] splitTilesA = TextureRegion.split(tilesetA, 24, 24);
        TextureRegion[][] splitTilesB = TextureRegion.split(tilesetB, 24, 24);

        tiledMap = new TiledMap();

        // Here, we interpret and assemble the tile information for Layers 0 - 3 (Scenery)
        for (int layer = 0; layer < 4; layer++) {
            //Create a new layer for us to press
            TiledMapTileLayer sceneryLayer = new TiledMapTileLayer(25, 10, 24, 24);

            // Fill out the scenery layer
            for (int row = 0; row < 10; row++) {
                for (int column = 0; column < 25; column++) {
                    // Division for row, modulo for column:
                    int tileNumber = sceneryData[layer][row][column] & 0xFF;
                    int tilesetRow = tileNumber / 16;
                    int tilesetColumn = tileNumber % 16;
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                    // Determine which tileset we need to pull from
                    if(tileNumber <= 0x7F) {
                        cell.setTile(new StaticTiledMapTile(splitTilesA[tilesetRow][tilesetColumn]));
                    } else {
                        cell.setTile(new StaticTiledMapTile(splitTilesB[tilesetRow - 8][tilesetColumn]));
                    }
					// Press the tile Cell into place (note: we set the columns horizontally!)
                    sceneryLayer.setCell(column, 9 - row, cell);
                }
            }
            //Add in the layer to be pressed
            tiledMap.getLayers().add(sceneryLayer);
        }

        // Produce the "backgroundID" gradient layer. The gradient is a single "strip" png.
        bg = new Texture(game.files.gradient(backgroundID));
        TiledMapTileLayer bgLayer = new TiledMapTileLayer(600/bg.getWidth(),
                240/bg.getHeight(), bg.getWidth(), bg.getHeight());
        TiledMapTileLayer.Cell bgCell = new TiledMapTileLayer.Cell();
        bgCell.setTile(new StaticTiledMapTile(new TextureRegion(bg)));

        // Assemble each strip of the gradient into the layer
        for(int i = 0; i < 25; i++) {
            bgLayer.setCell(i, 0, bgCell);
        }

        // Press each strip into the layer
        tiledMap.getLayers().add(bgLayer);

        // Finish
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    // -----METHODS FOR ASSEMBLING DATA FOR KSScreen-----
    private void assembleData() {
        //TODO: FIX path!!!!
        if(game.world.screenOffsetExists(xID,yID)) {
            try {
                RandomAccessFile mapFile = new RandomAccessFile(game.files.getWorldDir() + "/Map", "r");
                try {
                    // Seek the specific location in the Map file
                    mapFile.seek(game.world.getScreenOffset(xID, yID));

                    // Copy byte data for layers 0 - 3
                    for (int scnLayer = 0; scnLayer < 4; scnLayer++) {
                        for (int row = 0; row < 10; row++) {
                            for (int column = 0; column < 25; column++) {
                                sceneryData[scnLayer][row][column] = mapFile.readByte();
                            }
                        }
                    }

                    // Copy in byte data for layers 4 - 7
                    for (int objLayer = 4; objLayer <= 7; objLayer++) {
                        // Read object number
                        for (int row = 0; row < 10; row++) {
                            for (int column = 0; column < 25; column++) {
                                objectData[objLayer - 4][row][column] = mapFile.readByte();
                            }
                        }
                        // Read bank number
                        for (int row = 0; row < 10; row++) {
                            for (int column = 25; column < 50; column++) {
                                objectData[objLayer - 4][row][column] = mapFile.readByte();
                            }
                        }
                    }

                    // Initialize KSScreen attributes
                    tsetAID = mapFile.readByte();
                    tsetBID = mapFile.readByte();
                    atmosAID = mapFile.readByte();
                    atmosBID = mapFile.readByte();
                    musicID = mapFile.readByte();
                    backgroundID = mapFile.readByte();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // Initialize KSScreen attributes
            tsetAID = 0;
            tsetBID = 0;
            atmosAID = 0;
            atmosBID = 0;
            musicID = 0;
            backgroundID = 0;
        }
    }
}
