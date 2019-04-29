/*
 * KsmScreen.java
 * Handles the displaying of any particular screen, loaded from the Map.bin file.
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

package io.github.scalrx.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.gui.OnscreenController;
import io.github.scalrx.screendata.AudioData;
import io.github.scalrx.screendata.ObjectData;
import io.github.scalrx.screendata.Tiler;

public class KsmScreen implements Screen {

    // Set up how our level will display
    final KnyttStories game;
    // Assets for the particular KsmScreen we're on
    private final Tiler tiler;
    private final ObjectData objects;
    private final AudioData audioData;
    BitmapFont font = new BitmapFont();
    private OrthographicCamera camera;
    private Viewport viewport;
    private OnscreenController controller;
    // Attributes for the particular KsmScreen we're on
    private int xID, yID;           // Identifies where we are in the world
    private byte atmosAID;          // Atmospheric sounds A
    private byte atmosBID;          // Atmospheric sounds B
    private byte musicID;           // Music for this KsmScreen

    public KsmScreen(final KnyttStories game, int xID, int yID) {
        this.game = game;
        this.xID = xID;
        this.yID = yID;

        camera = new OrthographicCamera();
        camera.position.set(24 * 25 / 2.0f, 24 * 10 / 2.0f, 0);
        //viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);
        viewport = new FitViewport(24 * 25, 24 * 10, camera);
        controller = new OnscreenController(game.batch);
        tiler = new Tiler(game.assetManager, game.currWorld);
        objects = new ObjectData(game.currWorld);
        audioData = new AudioData(game.currWorld);

        // Assemble all the data for this screen from the Map.bin.raw file
        initializeData();
    }

    private void initializeData() {
        tiler.generateTiledMap(xID, yID);
        objects.placeObjects(xID, yID);

        // Set the audio for this screen
        audioData.setAudioBytes(xID, yID);
        musicID = audioData.getMusicID();
        atmosAID = audioData.getAtmosAID();
        atmosBID = audioData.getAtmosBID();
    }

    @Override
    public void show() {
        // Handle all audio for this screen: do we need to fade out or play something immediately?
        game.audio.prepareScreenAudio(musicID, atmosAID, atmosBID);
    }

    @Override
    public void render(float delta) {
        // Handle audio fading if necessary
        game.audio.handleFadeout(delta, musicID);

        // Clear the screen for the next frame
        game.batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0f / 255f, 0 / 255f, 0f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        tiler.getTiledMapRenderer().setView(camera);
        tiler.render();

        // Render the KsmScreen in the desired order TODO: Leave space for Juni
        game.batch.begin();

        // Print the current KsmScreen coordinates in the bottom left of the screen.
        font.draw(game.batch, "(" + xID + ", " + yID + ")", 10, 40);
        font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);

        // Finish drawing to the screen
        game.batch.end();

        // Draw controller to screen for mobile platforms
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS)
            controller.draw();

        // Movement-related controls for us to use temporarily
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || controller.isLeftPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game, xID - 1, yID));
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || controller.isRightPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game, xID + 1, yID));
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || controller.isUpPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game, xID, yID - 1));
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || controller.isDownPressed()) {
            controller.resetTouch();
            game.setScreen(new KsmScreen(game, xID, yID + 1));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        controller.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        tiler.dispose();
        objects.dispose();
    }
}
