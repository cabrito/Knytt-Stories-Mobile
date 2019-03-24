package io.github.scalrx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.scalrx.KnyttStories;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		final int TILE_DIMENSION = 24;	// 24 pixels for width and height
		final int NUMBER_OF_TILES_HORIZONTAL = 25;
		final int NUMBER_OF_TILES_VERTICAL = 10;

		// 480p, 16:9
		final int WINDOW_WIDTH = 854;
		final int WINDOW_HEIGHT = 480;

		// Set window properties
		config.title = "Knytt Stories Mobile";
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		// config.width = NUMBER_OF_TILES_HORIZONTAL * TILE_DIMENSION;
		// config.height = NUMBER_OF_TILES_VERTICAL * TILE_DIMENSION;

		// Create the window via the LWJGL constructor
		new LwjglApplication(new KnyttStories(), config);
	}
}
