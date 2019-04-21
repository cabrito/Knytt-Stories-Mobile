package io.github.scalrx.screendata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import io.github.scalrx.KsmFiles;
import io.github.scalrx.World;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * Tiler.java
 * Created by: scalr at 10:20 PM, 4/14/19
 *
 * Module that handles the tileset rendering and information for any particular screen.
 *
 **************************************************************************************************/
public class Tiler {

    ///////////////////////////////////////////////// Members that directly influence the imagery
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private Texture tilesetA;
    private Texture tilesetB;
    private Texture bg;
    private byte tsetAID;      // 0x00 to 0x7F in tile data
    private byte tsetBID;      // 0x80 to 0xFF in tile data
    private byte backgroundID;    // Background picture
    private byte[][][] sceneryData = new byte[4][10][25];

    // Helpful constants
    private final int[] BACKGROUND_LAYER = {4};
    private final int[] PRIMARY_LAYERS = {0, 1, 2, 3};
    private final AssetManager manager;
    private final World world;

    /***********************************************************************************************             Constructors */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public Tiler(final AssetManager manager, final World world)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        this.manager = manager;
        this.world = world;
    }

    /***********************************************************************************************             Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void generateTiledMap(int xID, int yID)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        if(world.getMap().screenOffsetExists(xID, yID)) {
            generateData(xID, yID);
        } else {
            tsetAID = 0;
            tsetBID = 0;
            backgroundID = 0;
        }

        // Load the data we're going to be dealing with
        manager.load(world.getFiles().tileset(tsetAID), Texture.class);
        manager.finishLoading();
        manager.load(world.getFiles().tileset(tsetBID), Texture.class);
        manager.finishLoading();
        manager.load(world.getFiles().gradient(backgroundID), Texture.class);
        manager.finishLoading();

        // Use the generated data in order to produce the tile scenery
        tilesetA = manager.get(world.getFiles().tileset(tsetAID));
        tilesetB = manager.get(world.getFiles().tileset(tsetBID));

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
        bg = manager.get(world.getFiles().gradient(backgroundID));
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void generateData(int xID, int yID)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        final int LAYER_SIZE = 250;
        final int OBJECT_LAYER_SIZE = LAYER_SIZE * 2;
        final int NUMBER_OF_OBJECT_LAYERS = 4;

        // Open Map.bin.raw as a byte array
        FileHandle mapFile = Gdx.files.external(world.getFiles().mapBin(true));
        byte[] mapFileBytes = mapFile.readBytes();

        // Seek the specific location in the Map file
        int cursorPosition = world.getMap().getScreenOffset(xID, yID);

        // Copy byte data for layers 0 - 3
        for (int layer = 0; layer < 4; layer++) {
            for (int row = 0; row < 10; row++) {
                for (int column = 0; column < 25; column++, cursorPosition++) {
                    sceneryData[layer][row][column] = mapFileBytes[cursorPosition];
                }
            }
        }

        // Skip over the object data to reach the screen parameters
        cursorPosition += OBJECT_LAYER_SIZE * NUMBER_OF_OBJECT_LAYERS;

        // Initialize tileset attributes
        tsetAID = mapFileBytes[cursorPosition++];
        tsetBID = mapFileBytes[cursorPosition];
        // Skip over audio attributes, and finally read the background data
        cursorPosition += 4;
        backgroundID = mapFileBytes[cursorPosition];
    }

    /***********************************************************************************************             Getter Functions */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public TiledMapRenderer getTiledMapRenderer()
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        return tiledMapRenderer;
    }

    /***********************************************************************************************             Utility Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void render()
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        tiledMapRenderer.render(BACKGROUND_LAYER);
        tiledMapRenderer.render(PRIMARY_LAYERS);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void dispose()
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        // Unload/dispose the resources since we're not using them.
        manager.unload(world.getFiles().tileset(tsetAID));
        manager.unload(world.getFiles().tileset(tsetBID));
        manager.unload(world.getFiles().gradient(backgroundID));
        tiledMap.dispose();
    }
}
