package io.github.scalrx;

import io.github.scalrx.utilities.KS_Files;
import io.github.scalrx.utilities.MapFile;

public class World {
    // World information
    private String worldName;
    private String author;
    private int size;

    // Used for simplifying filepaths
    public KS_Files files;

    // Map information
    public MapFile map;

    // Constructor (info comes from World.ini?)
    public World(String author, String worldName, int size) {
        this.worldName = worldName;
        this.author = author;
        this.size = size;

        // Initialize the files directory specific to the specified world
        files = new KS_Files(author, worldName);

        // Create object referring to our Map.bin file
        map = new MapFile(files);
    }

    public String getWorldName() {
        return worldName;
    }

    public String getAuthor() {
        return author;
    }

}
