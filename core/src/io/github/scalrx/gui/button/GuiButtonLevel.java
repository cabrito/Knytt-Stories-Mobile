/*
 * GuiButtonLevel.java
 * Streamlines the creation of a level button, typically used on the level selection screen.
 * Created by: scalr on 4/9/2019.
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class GuiButtonLevel extends ImageTextButton {
    private final Label LEVEL_DESCRIPTION;
    private final Image ICON;

    public GuiButtonLevel(final Texture icon, final String title, final String description, final Skin skin) {

        // Initialize a basic ImageTextButton with the required information
        super(title, initSkin(skin));
        this.ICON = new Image(icon);
        LEVEL_DESCRIPTION = new Label(description, getSkin());

        // Produce the correct layout
        rearrangeLayout();
    }

    private static Skin initSkin(Skin skin) {
        ImageTextButtonStyle imageTextButtonStyle = new ImageTextButtonStyle();
        imageTextButtonStyle.up = skin.newDrawable("gui-button-level");
        imageTextButtonStyle.down = skin.newDrawable("gui-button-level", 0.0f, 0.0f, 0.0f, 0.5f);
        imageTextButtonStyle.font = skin.getFont("gui-button-level");
        imageTextButtonStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("default", imageTextButtonStyle);
        Label.LabelStyle descriptionLayoutStyle = new Label.LabelStyle();
        descriptionLayoutStyle.font = skin.getFont("description-font");
        descriptionLayoutStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("default", descriptionLayoutStyle);
        return skin;
    }

    private void rearrangeLayout() {
        // Constants
        final int ICON_SIZE = 30;
        final float PADDING = 2;
        final float COLUMN_WIDTH = getSkin().getDrawable("gui-button-level").getMinWidth() - (ICON_SIZE + 3 * PADDING);

        clearChildren();
        left().padLeft(PADDING);
        add(ICON).size(ICON_SIZE).padRight(PADDING);

        // Stack the title and the description labels
        Table table = new Table();
        add(table);
        getLabel().setEllipsis(true);
        getLabel().setAlignment(Align.left);
        table.add(getLabel()).width(COLUMN_WIDTH);
        table.row();
        LEVEL_DESCRIPTION.setEllipsis(true);
        table.add(LEVEL_DESCRIPTION).width(COLUMN_WIDTH);

        // This was an older solution that was more lightweight than the Table, but setEllipses/Labels were bugged at initial implementation.
        /*VerticalGroup vg = new VerticalGroup();

        // Finish stacking
        vg.columnAlign(Align.left);
        vg.addActor(getLabel());
        vg.addActor(LEVEL_DESCRIPTION);
        vg.align(Align.left);

        // Add these to the button
        add(vg).width(198f);*/

    }
}
