/*
 * KnyttStories.java
 * Entry point into the Knytt Stories Mobile game core.
 * Created by: scalr on 12/27/2018.
 *
 * Knytt Stories Mobile
 * https://github.com/scalrx
 * Copyright (c) 2018 by scalr.
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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import io.github.scalrx.screens.MenuScreen;
import io.github.scalrx.utilities.Permissions;
import io.github.scalrx.world.World;

public class KnyttStories extends Game {

    // Attributes we use for rendering a screen in a Knytt Stories level
    public static final int V_WIDTH = 854;
    public static final int V_HEIGHT = 480;
    // Declare the getResources our program will use during its lifecycle
    public SpriteBatch batch;
    public AssetManager assetManager;
    // World object
    public World currWorld;

    // Used for handling all the music and ambiance.
    public KsmAudio audio;
    public KsmFiles files;
    // For Android
    private Permissions permissions;

    // Desktop Constructor
    public KnyttStories() {
        super();
    }

    // Android Constructor
    public KnyttStories(Permissions permissions) {
        super();
        this.permissions = permissions;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    @Override
    public void create() {
        // Initialize our SpriteBatch and AssetManager
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        files = new KsmFiles(assetManager);

        FileHandleResolver internal = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(internal));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(internal));

        FreetypeFontLoader.FreeTypeFontLoaderParameter defaultParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        defaultParams.fontFileName = "fonts/lsans.ttf";
        defaultParams.fontParameters.size = 12;
        defaultParams.fontParameters.color = Color.BLACK;
        //defaultParams.fontParameters.mono = true;

        FreetypeFontLoader.FreeTypeFontLoaderParameter smallParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallParams.fontFileName = "fonts/lsans.ttf";
        smallParams.fontParameters.size = 12;
        smallParams.fontParameters.color = Color.BLACK;
        //smallParams.fontParameters.mono = true;

        FreetypeFontLoader.FreeTypeFontLoaderParameter smallParamsGray = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallParamsGray.fontFileName = "fonts/lsans.ttf";
        smallParamsGray.fontParameters.size = 12;
        smallParamsGray.fontParameters.color = Color.GRAY;
        //smallParamsGray.fontParameters.mono = true;

        // You HAVE to write ".ttf" even if that's not the real name of the font file.
        assetManager.load("defaultFont.ttf", BitmapFont.class, defaultParams);
        assetManager.load("smallFont.ttf", BitmapFont.class, smallParams);
        assetManager.load("smallGrayFont.ttf", BitmapFont.class, smallParamsGray);
        assetManager.finishLoading();
        // Push the main menu onto the screen stack.
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }
}
