package io.github.scalrx;

import io.github.scalrx.utilities.KsmFiles;
import io.github.scalrx.utilities.MapFile;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * World.java
 * Created by: scalr at 4:19 PM, 3/30/19
 *
 * All of the relevant information for the current world that's loaded.
 *
 **************************************************************************************************/

public class World {
    // World information
    private String worldName;
    private String author;
    private int size;

    // Map information
    private MapFile map;
    private final KsmFiles files;

    // Constructor (info comes from World.ini?)
    public World(final KsmFiles files) {
        // Initialize the files directory specific to the specified world
        this.files = files;
    }

    /**     Methods     */
    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void initMap() {
        files.setWorldDir(author, worldName);
        map = new MapFile(files);
    }

    public MapFile getMap() {
        return map;
    }
}
