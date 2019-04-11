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

public class KnyttStories extends Game {

	// Declare the resources our program will use during its lifecycle
	public SpriteBatch batch;
	public AssetManager assetManager;

	// Attributes we use for rendering a screen in a Knytt Stories level
	public static final int V_WIDTH	= 854;
	public static final int V_HEIGHT = 480;

	// World object
	public World currWorld;

	// Used for handling all the music and ambiance.
	public KsmMusic audio;
	public KsmFiles files;

	// Used for the various fonts we'll encounter throughout the game



	@Override
	public void create () {
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
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}
}
