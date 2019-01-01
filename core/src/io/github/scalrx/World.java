package io.github.scalrx;

import com.badlogic.gdx.Gdx;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class World {
    // World information
    private String worldName;
    private String author;
    private int size;

    // Map information
    HashMap<Pair<Integer>, Long> mapFileOffsets = new HashMap<Pair<Integer>, Long>();

    // Constructor (info comes from World.ini?)
    public World(String author, String worldName, int size) {
        this.worldName = worldName;
        this.author = author;
        this.size = size;

        // Fill out the HashMap with all the offsets from the (uncompressed) Map file.
        createMapFileOffsets();
    }

    // Get the Map file offset for the desired KSScreen
    public long getScreenOffset(int x, int y) {
        return mapFileOffsets.get(new Pair<Integer>(x,y));
    }

    public boolean screenOffsetExists(int x, int y) {
        if(mapFileOffsets.containsKey(new Pair<Integer>(x,y)))
            return true;
        else
            return false;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getAuthor() {
        return author;
    }

    // Produce all of the file offsets in the Map file
    private void createMapFileOffsets() {
        //Get file information
        final long SCREEN_DATA_SIZE = 3006;

        try {
            // First, assign the directory we're working with TODO: The default directory is ~/knytt. FIX!!!
            FileInputStream mapFile = (FileInputStream) Gdx.files.external("Knytt Stories Mobile/" +
                    author + " - " + worldName + "/Map").read();

            //Now, progress through the file finding level headers
            int currByte = 255, X = 0, Y = 0;
            int coordVal = 0;
            long charPos = 0, offset = 0;
            boolean isNegative = false;

            //While we haven't reached EOF...
            while(currByte != -1) {
                //While we haven't reached the end of the submap header...
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
                    //Increase our cursor position
                    charPos++;
                }
                offset += charPos + 5;
                //charPos = 0;
                //Once the inner while loop ends, we need to take note of the submap coordinates and its file offset
                mapFileOffsets.put(new Pair<Integer>(X,Y), offset);
                //Skip to the next level header
                mapFile.skip(SCREEN_DATA_SIZE + 4);
                offset += SCREEN_DATA_SIZE;
                X = Y = coordVal = 0;

                //Reset the character counter position appropriately.
                charPos = 1;
                currByte = mapFile.read();
            }
            mapFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
