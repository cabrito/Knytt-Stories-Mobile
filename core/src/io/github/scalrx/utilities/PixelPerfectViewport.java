package io.github.scalrx.utilities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * PixelPerfectViewport.java
 * Created by: scalr at 1:41 PM, 4/1/19
 *
 * To fit with the style of Knytt Stories having a pixel-art protagonist, I feel it to be fitting
 * for the game to have a consistent pixel-art feel throughout. Based on the PixelPerfectViewport
 * by mgsx-dev on GitHub:
 *
 * https://github.com/mgsx-dev/dl6/blob/master/core/src/net/mgsx/dl3/utils/PixelPerfectViewport.java
 *
 **************************************************************************************************/

public class PixelPerfectViewport extends FitViewport {

    /**
     * Constructors
     */
    public PixelPerfectViewport(float worldWidth, float worldHeight) {
        super(worldWidth, worldHeight);
    }

    public PixelPerfectViewport(float worldWidth, float worldHeight, Camera camera) {
        super(worldWidth, worldHeight, camera);
    }

    /**
     * Methods
     */
    // Must be called so that the screen is updated accordingly on window resize or on respective display.
    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {

        // First, find the ratios of the screen dimensions to the world dimensions to decide which needs to be corrected.
        float widthRatio = screenWidth / getWorldWidth();
        float heightRatio = screenHeight / getWorldHeight();
        float scaleFactor = Math.min(widthRatio, heightRatio);

        // Create an integer scale factor to use so that we actually produce a whole pixel at minimum
        int intScaleFactor = Math.max(1, MathUtils.floor(scaleFactor));

        // Then, compute the boundaries for the rounded Viewport
        int viewportWidth = (int) getWorldWidth() * intScaleFactor;
        int viewportHeight = (int) getWorldHeight() * intScaleFactor;

        // And lastly, apply the pixel-perfect scaling.
        setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
        apply(centerCamera);

    }

}
