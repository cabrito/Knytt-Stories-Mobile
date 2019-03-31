package io.github.scalrx.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * MapFile.java
 * Created by: scalr at 4:03 PM, 3/30/19
 *
 * Handles all routines related to the Map.bin and Map.bin.raw files for the currently-loaded World.
 *
 **************************************************************************************************/

public class MapFile {

    /**     Members     */
    private final HashMap<Pair<Integer>, Integer> mapFileOffsets;
    private final KsmFiles files;

    /**     Constructor     */
    public MapFile(final KsmFiles files) {
        mapFileOffsets = new HashMap<Pair<Integer>, Integer>();
        this.files = files;

        // Make sure the optimized Map.bin.raw and date files actually exist before we try making file offsets
        /*if(!Gdx.files.absolute(files.MapBin() + ".raw").exists())
            decompress();
        if (getMapBinDatDate(files.MapBin()) != Gdx.files.absolute(files.MapBin()).lastModified())
            decompress();*/

        /*if(!Gdx.files.external(files.mapBin(true)).exists())
            decompress();
        if(getMapBinDatDate(files.mapBin(false)) != Gdx.files.external(files.mapBin(false)).lastModified())
            decompress();
        // Now that we have the necessary files in place, begin creating the Map.bin.raw file offsets.*/
        createFileOffsets();
    }

    // Decompress the Map.bin file for optimization
    private void decompress() {
        String mapBinFile = files.mapBin(false);
        GZip rawFile = new GZip(mapBinFile);
        rawFile.decompress();
        writeDateFile(mapBinFile);
    }

    // Used for checking whether or not the map has changed/been updated    TODO: Actually have this print out the number, not the bytes
    private void writeDateFile(String filepath) {
        try {
            FileOutputStream output = new FileOutputStream(filepath + ".dat");
            DataOutputStream dataOutput = new DataOutputStream(output);
            dataOutput.writeLong(Gdx.files.external(filepath).lastModified());
            dataOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getMapBinDatDate(String filepath) {
        try {
            FileInputStream input = new FileInputStream(filepath + ".dat");
            DataInputStream dis = new DataInputStream(input);
            return dis.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Get the Map file offset for the desired KsmScreen
    public int getScreenOffset(int x, int y) {
        return mapFileOffsets.get(new Pair<Integer>(x,y));
    }

    public boolean screenOffsetExists(int x, int y) {
        return mapFileOffsets.containsKey(new Pair<Integer>(x, y));
    }

    // Produce all of the file offsets in the Map file
    private void createFileOffsets() {
        // Get file information
        final long SCREEN_DATA_SIZE = 3006;

        try {
            // Open Map.bin.raw as a byte array
            FileHandle mapFile = Gdx.files.external(files.mapBin(true));
            byte[] mapFileBytes = mapFile.readBytes();

            //FileInputStream mapFile = (FileInputStream) Gdx.files.external(files.mapBin(true)).read();

            // Now, progress through the file finding level headers
            int X = 0, Y = 0;
            int coordVal = 0;
            int offset = 0;
            boolean isNegative = false;

            int cursorPosition = 0;

            while(cursorPosition < mapFileBytes.length) {
                for (int charPos = 0; ; charPos++, cursorPosition++) {
                    if ((mapFileBytes[cursorPosition] & 0xFF) == 'x') {
                        if (charPos != 0) {
                            throw new IOException("Invalid header; unexpected x-coordinate encountered.");
                        }
                    } else if ((mapFileBytes[cursorPosition] & 0xFF) == 'y') {
                        if (isNegative)
                            X = -coordVal;
                        else
                            X = coordVal;
                        coordVal = 0;
                        isNegative = false;
                    } else if (((mapFileBytes[cursorPosition] & 0xFF) >= '0') && ((mapFileBytes[cursorPosition] & 0xFF) <= '9')) {
                        coordVal = coordVal * 10 + ((mapFileBytes[cursorPosition] & 0xFF) - '0');
                    } else if ((mapFileBytes[cursorPosition] & 0xFF) == '-') {
                        isNegative = true;
                        coordVal = 0;
                    } else if ((mapFileBytes[cursorPosition] & 0xFF) == 0) {
                        if (isNegative)
                            Y = -coordVal;
                        else
                            Y = coordVal;
                        break;
                    } else {
                            break;  // ?????????
                    }
                }
                offset = cursorPosition + 5;

                // Once the inner while loop ends, we need to take note of the submap coordinates and its file offset
                mapFileOffsets.put(new Pair<Integer>(X,Y), offset);

                // Skip to the next level header
                cursorPosition += (5 + SCREEN_DATA_SIZE);
                X = Y = coordVal = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
