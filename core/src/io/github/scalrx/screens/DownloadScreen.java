/*
 *  DownloadScreen.java
 *  FILE DESCRIPTION GOES HERE.
 *  Created by: scalr on 6/14/2019, 8:57.
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

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.scalrx.KnyttStories;

/**
 * Displays a list of Knytt Stories levels available to the user to download AND unpackage to their
 * device.
 */
public class DownloadScreen implements Screen
{
    private final KnyttStories ksm;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;

    // Button textures
    private Texture guiBtnLevel;

    /**
     * Called to push the Download screen onto the Screen stack.
     *
     * @param ksm
     */
    public DownloadScreen(final KnyttStories ksm)
    {
        this.ksm = ksm;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT);
        viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);

    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {

    }

    @Override
    public void resize(int width, int height)
    {

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
