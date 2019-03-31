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
    private final HashMap<Pair<Integer>, Long> mapFileOffsets;
    private final KsmFiles files;

    /**     Constructor     */
    public MapFile(final KsmFiles files) {
        mapFileOffsets = new HashMap<Pair<Integer>, Long>();
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
    public long getScreenOffset(int x, int y) {
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
            int currByte = 255, X = 0, Y = 0;
            int coordVal = 0;
            long charPos = 0, offset = 0;
            boolean isNegative = false;

            // While we haven't reached EOF...
            while(currByte != -1) {
                // While we haven't reached the end of the submap header...
                while((currByte) != 0) {
                    currByte = mapFile.read();
                    if (currByte == 'x') {
                        if (charPos != 0) {
                            // Invalid header, x coord at an unexpected place - upper layers
                            throw new IOException("Invalid header; unexpected x-coordinate encountered.");
                        }
                    }
                    else if (currByte == 'y') {
                        if (isNegative)
                            X = -coordVal;
                        else
                            X = coordVal;
                        coordVal = 0;
                        isNegative = false;
                    }
                    else if ((currByte >= '0') && (currByte <= '9')) {
                        coordVal = coordVal * 10 + (currByte - '0');
                    }
                    else if (currByte == '-') {
                        isNegative = true;
                        coordVal = 0;
                    }
                    else if (currByte == 0) {
                        if (isNegative)
                            Y = -coordVal;
                        else
                            Y = coordVal;
                        break;
                    }
                    else {
                        if(currByte == -1)
                            break;
                    }
                    // Increase our cursor position
                    charPos++;
                }
                offset += charPos + 5;
                // charPos = 0;
                // Once the inner while loop ends, we need to take note of the submap coordinates and its file offset
                mapFileOffsets.put(new Pair<Integer>(X,Y), offset);
                // Skip to the next level header
                mapFile.skip(SCREEN_DATA_SIZE + 4);
                offset += SCREEN_DATA_SIZE;
                X = Y = coordVal = 0;

                // Reset the character counter position appropriately.
                charPos = 1;
                currByte = mapFile.read();
            }
            mapFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
