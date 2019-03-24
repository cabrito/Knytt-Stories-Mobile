package io.github.scalrx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.scalrx.screens.MenuScreen;
import io.github.scalrx.utilities.KS_Files;
import io.github.scalrx.utilities.KS_Music;

public class KnyttStories extends Game {

	// Declare the resources our program will use during its lifecycle
	public SpriteBatch batch;
	public AssetManager assetManager;

	// Attributes we use for rendering a screen in a Knytt Stories level
	public static final int V_WIDTH = 25*24;
	public static final int V_HEIGHT = 10*24;

	// World object
	public World currWorld;

	// Used for handling all the music and ambiance.
	public KS_Music audio;

	@Override
	public void create () {
		// Initialize our SpriteBatch and AssetManager
		batch = new SpriteBatch();
		assetManager = new AssetManager(new AbsoluteFileHandleResolver());

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
