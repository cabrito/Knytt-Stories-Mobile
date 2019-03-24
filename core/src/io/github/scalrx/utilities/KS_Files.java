package io.github.scalrx.utilities;

import com.badlogic.gdx.Gdx;

/*
 * Used for *easier* filesystem access
 */
public class KS_Files {

    // Filesystem
    private final String ksmDirName = "Knytt Stories Mobile";
    private String worldDir;

    // Constructor
    public KS_Files(String author, String worldName) {
        worldDir = Gdx.files.external(ksmDirName + "/" + author + " - " + worldName).path();
    }

    // Methods
    public String getWorldDir() {
        return Gdx.files.getExternalStoragePath() + worldDir;
    }

    public String music(byte mID) {
        // Custom Music
        if(Gdx.files.external(worldDir + "/Music/Song" + (mID & 0xFF) + ".ogg").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Music/Song" + (mID & 0xFF) + ".ogg").path();
        }
        else if(Gdx.files.external(worldDir + "/Music/Song" + (mID & 0xFF) + ".mp3").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Music/Song" + (mID & 0xFF) + ".mp3").path();
        }
        else if(Gdx.files.external(worldDir + "/Music/Song" + (mID & 0xFF) + ".wav").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Music/Song" + (mID & 0xFF) + ".wav").path();
        }
        // Otherwise, use vanilla music instead
        else {
            return Gdx.files.internal("Data/Music/Song" + (mID & 0xFF) + ".ogg").path();
        }
    }

    // Custom Ambiance
    public String ambiance(byte aID) {
        // Ogg
        if(Gdx.files.external(worldDir + "/Ambiance/Ambi" + (aID & 0xFF) + ".ogg").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Ambiance/Ambi" + (aID & 0xFF) + ".ogg").path();
        }
        // MP3
        else if(Gdx.files.external(worldDir + "/Ambiance/Ambi" + (aID & 0xFF) + ".mp3").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Ambiance/Ambi" + (aID & 0xFF) + ".mp3").path();
        }
        // Wav
        else if(Gdx.files.external(worldDir + "/Ambiance/Ambi" + (aID & 0xFF) + ".wav").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Ambiance/Ambi" + (aID & 0xFF) + ".wav").path();
        }
        // Otherwise, use vanilla ambiance instead
        else {
            return Gdx.files.internal("Data/Ambiance/Ambi" + (aID & 0xFF) + ".ogg").path();
        }
    }

    // Custom Tilesets
    public String tileset(byte tID) {
        if(Gdx.files.external(worldDir + "/Tilesets/Tileset" + (tID & 0xFF) + ".png").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Tilesets/Tileset" + (tID & 0xFF) + ".png").path();
        }
        // Otherwise, use vanilla tilesets instead
        else {
            return Gdx.files.internal("Data/Tilesets/Tileset" + (tID & 0xFF) + ".png").path();
        }
    }

    // Custom Tilesets
    public String gradient(byte gID) {
        if(Gdx.files.external(worldDir + "/Gradients/Gradient" + (gID & 0xFF) + ".png").exists()) {
            return Gdx.files.getExternalStoragePath() + Gdx.files.external(worldDir + "/Gradients/Gradient" + (gID & 0xFF) + ".png").path();
        }
        // Otherwise, use vanilla tilesets instead
        else {
            return Gdx.files.internal("Data/Gradients/Gradient" + (gID & 0xFF) + ".png").path();
        }
    }



}
