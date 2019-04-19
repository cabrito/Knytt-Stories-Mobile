package io.github.scalrx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * KsmMusic.java
 * Created by: scalr at 3:48 PM, 3/30/19
 *
 * Handles all of the music-related goodies for Knytt Stories Mobile.
 *
 **************************************************************************************************/

public class KsmMusic {

    // Music and Ambiance
    private Music music;
    private Music atmosA;
    private Music atmosB;

    // For conveniently handling filesystem calls
    private final AssetManager assetManager;
    private final KsmFiles files;

    /**     Constructor     */
    public KsmMusic(final AssetManager assetManager, final KsmFiles files) {
        this.assetManager = assetManager;
        this.files = files;
    }

    /**     Methods for handling music      */
    // Call before loading music
    public void loadMusic(byte muid) {
        assetManager.load(files.music(muid), Music.class);
        assetManager.finishLoading();
    }

    // Call before loading ambiance
    public void loadAmbience(byte amid) {
        assetManager.load(files.ambiance(amid), Music.class);
        assetManager.finishLoading();
    }

    // Plays music contextually
    public void playMusic(byte muid, float delta) {
        if((muid & 0xFF) > 0) {
            if(music != null) {
                if(music.isPlaying()) {
                    if(!assetManager.get(files.music(muid), Music.class).isPlaying() || (music.getVolume() < 1f)) {
                        music.setVolume(music.getVolume() - 0.2f*delta);
                        if(music.getVolume() < 0.02f) {
                            music.stop();
                            music = assetManager.get(files.music(muid), Music.class);
                            music.setVolume(1f);
                            music.play();
                        }
                    }
                } else {
                    music = assetManager.get(files.music(muid), Music.class);
                    music.setVolume(1f);
                    music.play();
                }
            } else {
                music = assetManager.get(files.music(muid), Music.class);
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

    ///////////////////////////////////////////////////////////// Method for fading the music out
    public void fadeMusic(float delta) {

    }

    ///////////////////////////////////////////////////////////////// Routines for stopping audio
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
