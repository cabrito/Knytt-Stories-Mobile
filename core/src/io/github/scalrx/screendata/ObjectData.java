package io.github.scalrx.screendata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.github.scalrx.World;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * ObjectData.java
 * Created by: scalr at 4:57 PM, 4/19/19
 *
 * Used for gathering and storing object data.
 *
 **************************************************************************************************/
public class ObjectData {

    ///////////////////////////////////////////////// Members that directly influence the objects
    private byte[][][] objectData = new byte[4][10][50];

    /////////////////////////////////////////////////////////////////////////// Helpful constants
    private final World world;

    //////////////////////////////////////////////////////////////////////////////// Constructors
    public ObjectData(final World world) {
        this.world = world;
    }

    ///////////////////////////////////////////////////////////////////////////////////// Methods
    public void placeObjects(int xID, int yID) {
        if(world.getMap().screenOffsetExists(xID, yID)) {
            generateData(xID, yID);
        } else {
            // Otherwise, there's nothing for us to do since we're in the VOID
        }
    }

    private void generateData(int xID, int yID) {
        final int LAYER_SIZE = 250;
        final int NUMBER_OF_LAYERS = 4;

        // Open Map.bin.raw as a byte array
        FileHandle mapFile = Gdx.files.external(world.getFiles().mapBin(true));
        byte[] mapFileBytes = mapFile.readBytes();

        // Seek the specific location in the Map file
        int objectDataLocation = world.getMap().getScreenOffset(xID, yID) + (LAYER_SIZE * NUMBER_OF_LAYERS);
        int cursorPosition = objectDataLocation;

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
    }

    ///////////////////////////////////////////////////////////////////////////// Getter Functions

    ////////////////////////////////////////////////////////////////////////////// Utility Methods
    public void render() {

    }
    public void dispose() {
        // Unload/dispose the resources since we're not using them anymore
    }
}