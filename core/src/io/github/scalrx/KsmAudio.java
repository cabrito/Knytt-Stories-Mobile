/*
 * KsmAudio.java
 * Handles all of the music- and ambiance-related goodies for Knytt Stories Mobile.
 * Created by: scalr on 3/30/2019.
 *
 * Knytt Stories Mobile
 * https://github.com/scalrx
 * Copyright (c) 2019 by scalr.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.scalrx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class KsmAudio {

    // For conveniently handling filesystem calls
    private final AssetManager assetManager;
    private final KsmFiles files;
    // Music and Ambiance
    private Music music;
    private Music atmosA;
    private Music atmosB;
    // Is the music supposed to be fading out?
    private boolean fading;

    public KsmAudio(final AssetManager assetManager, final KsmFiles files) {
        this.assetManager = assetManager;
        this.files = files;
        this.fading = false;
    }

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

    /**
     * Plays a {@link Music} object immediately. If this method is called while a world is loaded,
     * it will attempt to load the requested {@link Music} from the {@link io.github.scalrx.world.World},
     * provided one exists. Otherwise, it loads from the default tracks.
     * @param muid  A byte primitive, valid ranges from 1 - 255.
     */
    public void playMusic(byte muid) {
        // Assuming we're dealing with *some* audio track
        if ((muid & 0xFF) > 0) {
            // Try to play it
            if (assetManager.isLoaded(files.music(muid))) {
                // Immediately stop anything that WAS playing (if needed)
                if (music != null) {
                    music.stop();
                    files.getResources().dispose(music);
                }

                // And then play the new track
                music = assetManager.get(files.music(muid), Music.class);
                music.setVolume(1f);
                music.setLooping(true);
                music.play();
            } else {
                return;    // Otherwise, do nothing; maybe the level references a non-existent audio track? e.g., Song0.ogg
            }
        }
    }

    /**
     * Plays both atmospheric ambiance tracks simultaneously. If this method is called while a world
     * is loaded, it will attempt to load the requested {@link Music} from the
     * {@link io.github.scalrx.world.World}, provided one exists. Otherwise, it loads from the
     * default tracks.
     * @param atmosAID  A byte primitive, valid ranges from 1 - 255.
     * @param atmosBID  A byte primitive, valid ranges from 1 - 255.
     */
    public void playAmbiance(byte atmosAID, byte atmosBID) {
        // Logic for playing/stopping atmosA
        if ((atmosAID & 0xFF) > 0) {
            if (assetManager.isLoaded(files.ambiance(atmosAID))) {
                if (!assetManager.get(files.ambiance(atmosAID), Music.class).isPlaying()) {
                    if (atmosA != null) {
                        atmosA.stop();
                        files.getResources().dispose(atmosA);
                    }
                    atmosA = assetManager.get(files.ambiance(atmosAID), Music.class);
                    atmosA.setLooping(true);
                    atmosA.play();
                }

            }
        } else {
            if (atmosA != null)
                atmosA.stop();
        }

        // Logic for playing/stopping atmosB
        if ((atmosBID & 0xFF) > 0) {
            if (assetManager.isLoaded(files.ambiance(atmosBID))) {
                if (!assetManager.get(files.ambiance(atmosBID), Music.class).isPlaying()) {
                    if (atmosB != null) {
                        atmosB.stop();
                        files.getResources().dispose(atmosB);
                    }
                    atmosB = assetManager.get(files.ambiance(atmosBID), Music.class);
                    atmosB.setLooping(true);
                    atmosB.play();
                }

            }
        } else {
            if (atmosB != null)
                atmosB.stop();
        }
    }

    /**
     * Loads music and atmospheric ambiance for the given {@link io.github.scalrx.screens.KsmScreen},
     * and attempts to play them. If the current music playing is not the desired music, the music
     * is queued up and prepares the current music to start fading.
     * @param musicID       A byte primitive, valid range from 1 - 255.
     * @param atmosAID      A byte primitive, valid range from 1 - 255.
     * @param atmosBID      A byte primitive, valid range from 1 - 255.
     */
    public void prepareScreenAudio(byte musicID, byte atmosAID, byte atmosBID) {
        // Now that we have the musicID, atmosA, and atmosB bytes, try loading such audio files
        if ((musicID & 0xFF) > 0)
            loadMusic(musicID);
        if ((atmosAID & 0xFF) > 0)
            loadAmbience(atmosAID);
        if ((atmosBID & 0xFF) > 0)
            loadAmbience(atmosBID);

        // Check whether or not we need to manipulate the music
        if (!isSongPlaying(musicID)) {
            if (!music.isPlaying()) {
                playMusic(musicID);
            } else {
                setFading(true);
            }
        }

        playAmbiance(atmosAID, atmosBID);
    }

    /**
     * Determines whether the current {@link Music} track is fading or not.
     * @return
     */
    public boolean isFading() {
        return fading;
    }

    /**
     * Sets the current {@link Music} track to fade.
     * @param fading
     */
    public void setFading(boolean fading) {
        this.fading = fading;
    }

    /**
     * Logic for *HOW* to handle a music fadeout
     */
    public void fadeMusic() {
        float delta = Gdx.graphics.getDeltaTime();
        float currentVolume = music.getVolume();
        float SCALE_FACTOR = 0.35f;
        float THRESHOLD = 0.02f;

        if (music.isPlaying()) {
            if (currentVolume >= THRESHOLD) {
                music.setVolume(currentVolume - (SCALE_FACTOR * delta));

            } else {
                music.stop();
                fading = false;
            }
        } else {
            fading = false;
        }
    }

    /**
     * Logic for *WHEN* to handle a music fadeout.
     * @param muid
     */
    public void handleFadeout(byte muid) {
        if (isFading()) {
            fadeMusic();
        } else {
            if (!music.isPlaying() && (muid > 0)) {
                playMusic(muid);
            }
        }
    }

    // Much cleaner, more generic way to stop each individual audio
    public void stopMusic() {
        if (music != null)
            music.stop();
    }

    public void stopAmbience() {
        if (atmosA != null)
            atmosA.stop();
        if (atmosB != null)
            atmosB.stop();
    }

    public boolean isSongPlaying(byte muid) {
        if (music.isPlaying() && (muid > 0)) {
            return assetManager.get(files.music(muid), Music.class).isPlaying();
        } else {
            return false;
        }
    }

    public Music getMusic() {
        return music;
    }
}
