package io.github.scalrx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.scalrx.screens.MenuScreen;

public class KnyttStories extends Game {

	public SpriteBatch batch;
	public static final int V_WIDTH = 25*24;
	public static final int V_HEIGHT = 10*24;
	Texture img;

	// World
	public World world;


	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World("The Machine", "Nifflas", 2);
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
		//img.dispose();
	}
}
