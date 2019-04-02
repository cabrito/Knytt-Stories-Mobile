package io.github.scalrx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * KsmFiles.java
 * Created by: scalr at 2:04 PM, 3/30/19
 *
 * Redo of the class that handles all of the filesystem calls for Knytt Stories Mobile. In
 * particular, this is response to Android needing AssetManager to swap between Internal and
 * External loaders; the previous attempt had to be scrapped for this reason.
 *
 **************************************************************************************************/

public class KsmFiles {
    /**     Constants     */
    private final String ksmDir = "Knytt Stories Mobile/";
    private final FileHandleResolver internal = new InternalFileHandleResolver();
    private final FileHandleResolver external = new ExternalFileHandleResolver();
    private final AssetManager assetManager;

    /**     Members     */
    private String worldDir;                                // DO NOT rely on this going forward!!!

    /**     Constructor     */
    public KsmFiles(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**     World-related Methods     */
    public void setWorldDir(String author, String worldName) {
        this.worldDir = author + " - " + worldName + "/";
    }

    public void resetWorldDir() {
        worldDir = null;
    }

    public String getWorldDir() {
        return worldDir;
    }

    public String mapBin(boolean raw) {
        return raw ? (ksmDir + getWorldDir() + "Map.bin.raw") : (ksmDir + getWorldDir() + "Map.bin");
    }

    /**     Music- and ambiance-related methods       */
    public String music(byte muid) {
        // If the world directory hasn't been set yet, load normal music
        if(worldDir == null || worldDir.isEmpty()) {
            // Swap the music loader for the AssetManager to load internal music files
            assetManager.setLoader(Music.class, new MusicLoader(internal));
            return Gdx.files.internal("Data/Music/Song" + (muid & 0xFF) + ".ogg").path();
        }

        // Swap the loader for external really quick for the following if-else statement...
        assetManager.setLoader(Music.class, new MusicLoader(external));

        // If there is music for this screen, provide it
        if(Gdx.files.external(ksmDir + getWorldDir() + "Music/Song" + (muid & 0xFF) + ".ogg").exists())
            return Gdx.files.external(ksmDir + getWorldDir() + "Music/Song" + (muid & 0xFF) + ".ogg").path();
        else {
            // Swap the music loader back to load internal music files
            assetManager.setLoader(Music.class, new MusicLoader(internal));
            return Gdx.files.internal("Data/Music/Song" + (muid & 0xFF) + ".ogg").path();
        }
    }

    public String ambiance(byte amid) {
        // If the world directory hasn't been set yet, load normal ambiance
        if(worldDir == null || worldDir.isEmpty()) {
            // Swap the music loader for the AssetManager to load internal ambiance files
            assetManager.setLoader(Music.class, new MusicLoader(internal));
            return Gdx.files.internal("Data/Ambiance/Ambi" + (amid & 0xFF) + ".ogg").path();
        }

        // Swap the loader for external really quick for the following if-else statement...
        assetManager.setLoader(Music.class, new MusicLoader(external));

        // If there is ambiance for this screen, provide it
        if(Gdx.files.external(ksmDir + getWorldDir() + "Ambiance/Ambi" + (amid & 0xFF) + ".ogg").exists()) {
            return Gdx.files.external(ksmDir + getWorldDir() + "Ambiance/Ambi" + (amid & 0xFF) + ".ogg").path();
        }
        else {
            // Swap the music loader back to load internal ambiance files
            assetManager.setLoader(Music.class, new MusicLoader(internal));
            return Gdx.files.internal("Data/Ambiance/Ambi" + (amid & 0xFF) + ".ogg").path();
        }
    }

    /**     Tileset-related Methods     */
    public String tileset(byte tsid) {
        // Swap the loader for external really quick for the following if-else statement...
        assetManager.setLoader(Texture.class, new TextureLoader(external));

        // Provide custom tileset filepath if available
        if(Gdx.files.external(ksmDir + getWorldDir() + "Tilesets/Tileset" + (tsid & 0xFF) + ".png").exists()) {
            return Gdx.files.external(ksmDir + getWorldDir() + "Tilesets/Tileset" + (tsid & 0xFF) + ".png").path();
        }
        // Otherwise, use vanilla tilesets instead
        else {
            // Swap the texture loader back to load internal ambiance files
            assetManager.setLoader(Texture.class, new TextureLoader(internal));
            return Gdx.files.internal("Data/Tilesets/Tileset" + (tsid & 0xFF) + ".png").path();
        }
    }

    // Custom Tilesets
    public String gradient(byte gid) {
        // Swap the loader for external really quick for the following if-else statement...
        assetManager.setLoader(Texture.class, new TextureLoader(external));

        // Provide custom gradients filepath if available
        if(Gdx.files.external(ksmDir + getWorldDir() + "Gradients/Gradient" + (gid & 0xFF) + ".png").exists()) {
            return Gdx.files.external(ksmDir + getWorldDir() + "Gradients/Gradient" + (gid & 0xFF) + ".png").path();
        }
        // Otherwise, use vanilla gradients instead
        else {
            // Swap the texture loader back to load internal ambiance files
            assetManager.setLoader(Texture.class, new TextureLoader(internal));
            return Gdx.files.internal("Data/Gradients/Gradient" + (gid & 0xFF) + ".png").path();
        }
    }
}
