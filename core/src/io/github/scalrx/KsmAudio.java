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

    // Revision of the music subsystem
    public void playMusic(byte muid) {
        // Assuming we're dealing with *some* audio track
        if ((muid & 0xFF) > 0) {
            // Try to play it
            if (assetManager.isLoaded(files.music(muid))) {
                // Immediately stop anything that WAS playing (if needed)
                if (music != null) {
                    music.stop();
                    files.resources().dispose(music);
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

    // Load the ambiance track
    public void playAmbiance(byte atmosAID, byte atmosBID) {
        // Logic for playing/stopping atmosA
        if ((atmosAID & 0xFF) > 0) {
            if (assetManager.isLoaded(files.ambiance(atmosAID))) {
                if (!assetManager.get(files.ambiance(atmosAID), Music.class).isPlaying()) {
                    if (atmosA != null) {
                        atmosA.stop();
                        files.resources().dispose(atmosA);
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
                        files.resources().dispose(atmosB);
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

    public boolean isFading() {
        return fading;
    }

    public void setFading(boolean fading) {
        this.fading = fading;
    }

    // Logic for *HOW* to perform a music fadeout
    public void fadeMusic(float delta) {
        float currentVolume = music.getVolume();
        final float SCALE_FACTOR = 0.35f;
        final float THRESHOLD = 0.02f;
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

    // Logic for *WHEN* to perform a music fadeout, and what to do afterwards.
    public void handleFadeout(float delta, byte muid) {
        if (isFading()) {
            fadeMusic(delta);
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
