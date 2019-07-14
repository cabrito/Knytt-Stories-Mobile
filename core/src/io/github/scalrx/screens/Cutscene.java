/*
 *  Cutscene.java
 *  How cutscenes are handled in levels, which includes the opening and endings of levels.
 *  Created by: scalr on 5/12/2019, 21:52.
 *
 * Knytt Stories Mobile
 * https://github.com/scalrx
 * Copyright (c) 2019 by scalr.
 *
 * DEV NOTES: Care needs to be taken when dealing with the opening, basic cutscene, and ending.
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

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.scalrx.KnyttStories;

public class Cutscene implements Screen
{
    // Members
    private final KnyttStories game;
    private OrthographicCamera camera;
    private Viewport viewport;

    // Specific to the screen that called the cutscene
    private final int x;
    private final int y;

    /**
     * Intended to be called by the current {@link KsmScreen} which passes its current {@link io.github.scalrx.world.World}
     * coordinates into this constructor to then look up which cutscene to display.
     * @param x The {@link io.github.scalrx.world.World} x-coordinate.
     * @param y The {@link io.github.scalrx.world.World} y-coordinate.
     */
    public Cutscene(final KnyttStories game, int x, int y)
    {
        this.game = game;
        this.x = x;
        this.y = y;
        // Initialize GUI elements
    }

    @Override
    public void show()
    {
        // Play music
        // Load Scene1.png from the world folder (if it exists!)
    }

    @Override
    public void render(float delta)
    {
        // Render SceneX.png from world folder.
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
