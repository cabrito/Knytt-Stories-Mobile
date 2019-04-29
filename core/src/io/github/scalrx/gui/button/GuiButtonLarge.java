/*
 * GuiButtonLarge.java
 * Streamlines the creation of a large button, typically used on the main menu.
 * Created by: scalr on 4/20/2019.
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

package io.github.scalrx.gui.button;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GuiButtonLarge extends ImageTextButton {
    private final Image ICON;

    public GuiButtonLarge(final Texture icon, final String title, final Skin skin) {

        // Initialize a basic ImageTextButton with the required information
        super(title, initSkin(skin));
        this.ICON = new Image(icon);

        // Produce the correct layout
        rearrangeLayout();
    }

    private void rearrangeLayout() {
        // Constants
        final int ICON_SIZE = 50;

        clearChildren();
        add(ICON).size(ICON_SIZE).row();
        add(getLabel());
    }

    private static Skin initSkin(Skin skin) {
        ImageTextButtonStyle imageTextButtonStyle = new ImageTextButtonStyle();
        imageTextButtonStyle.up = skin.newDrawable("gui-button-large");
        imageTextButtonStyle.down = skin.newDrawable("gui-button-large", 0.0f,0.0f,0.0f,0.5f);
        imageTextButtonStyle.font = skin.getFont("gui-button-large");
        imageTextButtonStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("default", imageTextButtonStyle);
        return skin;
    }
}
