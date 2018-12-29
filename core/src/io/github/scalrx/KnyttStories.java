package io.github.scalrx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.scalrx.screens.MenuScreen;

public class KnyttStories extends Game {

	// Declare the resources our program will use during its lifecycle
	public SpriteBatch batch;
	public AssetManager assetManager;

	// Attributes we use for rendering a screen in a Knytt Stories level
	public static final int V_WIDTH = 25*24;
	public static final int V_HEIGHT = 10*24;

	// World object
	public World world;
	public Music music;
	public Music atmosA;
	public Music atmosB;


	@Override
	public void create () {
		// Initialize our SpriteBatch and AssetManager
		batch = new SpriteBatch();
		assetManager = new AssetManager(new AbsoluteFileHandleResolver());

		// Push the main menu onto the screen stack.
		this.setScreen(new MenuScreen(this));
		//setScreen(new KSScreen(this,1000,1000));
		//img = new Texture("badlogic.jpg");
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
