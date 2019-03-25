package io.github.scalrx.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;


/***************************************************************************************************
 * Knytt Stories Mobile     (https://www.github.com/scalrx/knytt-stories-mobile)
 * KS_Music.java
 * Created by: scalr at 11:38 PM, 3/22/19
 *
 * Handles all of the Knytt Stories-related music shenanigans.
 *
 **************************************************************************************************/

public class KS_Music {

    // Music and Ambiance
    private Music music;
    private Music atmosA;
    private Music atmosB;

    // For conveniently handling filesystem calls
    final AssetManager assetManager;
    KS_Files files;

    // KS_Files-based constructor
    public KS_Music(final AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    // Used for when we load a world
    public void setFiles(final KS_Files files) {
        this.files = files;
    }

    // Call before loading music
    public void loadMusic(byte musicID) {
        if(files != null) {
            assetManager.load(files.music(musicID), Music.class);
        } else {
            assetManager.load(Gdx.files.internal("Data/Music/Song" + musicID + ".ogg").path(), Music.class);
        }
    }

    public void loadAmbience(byte atmosID) {
        if(files != null) {
            assetManager.load(files.music(atmosID), Music.class);
        } else {
            assetManager.load(Gdx.files.internal("Data/Ambiance/Ambi" + atmosID + ".ogg").path(), Music.class);
        }
    }

    // Plays music contextually
    public void playMusic(byte musicID, float delta) {
        // If there is no world set (e.g., we're on the main menu), attempt to play default music
        if(files == null) {
            if((musicID & 0xFF) > 0) {
                if(assetManager.isLoaded(Gdx.files.internal("Data/Music/Song" + (musicID & 0xFF) + ".ogg").path())) {
                    if(!assetManager.get(Gdx.files.internal("Data/Music/Song" + (musicID & 0xFF) + ".ogg").path(), Music.class).isPlaying()) {
                        if(music != null) {
                            music.stop();
                            music.dispose();
                        }
                        music = assetManager.get(Gdx.files.internal("Data/Music/Song" + (musicID & 0xFF) + ".ogg").path(), Music.class);
                        music.setLooping(true);
                        music.play();
                    }

                }
            } else {
                if(music != null)
                    music.stop();
            }
            return;
        }

        // Otherwise, if there IS a world loaded, load that music instead
        if((musicID & 0xFF) > 0) {
            if(music != null) {
                if(music.isPlaying()) {
                    if(!assetManager.get(files.music(musicID), Music.class).isPlaying() || (music.getVolume() < 1f)) {
                        music.setVolume(music.getVolume() - 0.2f*delta);
                        if(music.getVolume() < 0.02f) {
                            music.stop();
                            music = assetManager.get(files.music(musicID), Music.class);
                            music.setVolume(1f);
                            music.play();
                        }
                    }
                } else {
                    music = assetManager.get(files.music(musicID), Music.class);
                    music.setVolume(1f);
                    music.play();
                }
            } else {
                music = assetManager.get(files.music(musicID), Music.class);
                music.setVolume(1f);
                music.play();
            }
        } else {
            if(music != null) {
                if(music.isPlaying()) {
                    music.setVolume(music.getVolume() - 0.2f*delta);
                    if(music.getVolume() < 0.02f) {
                        music.stop();
                        music.setVolume(1f);
                    }
                }
            }
        }
    }

    // Load the ambiance track
    public void playAmbiance(byte atmosAID, byte atmosBID) {
        // Assuming we're not just sitting on the main menu or something...
        if(files != null) {
            // Logic for playing/stopping atmosA
            if((atmosAID & 0xFF) > 0) {
                if(assetManager.isLoaded(files.ambiance(atmosAID))) {
                    if(!assetManager.get(files.ambiance(atmosAID), Music.class).isPlaying()) {
                        if(atmosA != null) {
                            atmosA.stop();
                            atmosA.dispose();
                        }
                        atmosA = assetManager.get(files.ambiance(atmosAID), Music.class);
                        atmosA.setLooping(true);
                        atmosA.play();
                    }

                }
            } else {
                if(atmosA != null)
                    atmosA.stop();
            }

            // Logic for playing/stopping atmosB
            if((atmosBID & 0xFF) > 0) {
                if(assetManager.isLoaded(files.ambiance(atmosBID))) {
                    if(!assetManager.get(files.ambiance(atmosBID), Music.class).isPlaying()) {
                        if(atmosB != null) {
                            atmosB.stop();
                            atmosB.dispose();
                        }
                        atmosB = assetManager.get(files.ambiance(atmosBID), Music.class);
                        atmosB.setLooping(true);
                        atmosB.play();
                    }

                }
            } else {
                if(atmosB != null)
                    atmosB.stop();
            }
        }
    }

    // Routines for stopping audio
    public void stopMusic() {
        if(music != null)
            music.stop();
    }
    public void stopAmbience() {
        if(atmosA != null)
            atmosA.stop();
        if(atmosB != null)
            atmosB.stop();
    }
}
