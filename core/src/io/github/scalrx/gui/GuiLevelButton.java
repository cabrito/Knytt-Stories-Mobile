package io.github.scalrx.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * GuiLevelButton.java
 * Created by: scalr at 5:39 PM, 4/9/19
 *
 * .
 *
 **************************************************************************************************/
public class GuiLevelButton extends TextButton {
    private final Label LEVEL_DESCRIPTION;
    private final Image ICON;

    // Constructor
    public GuiLevelButton(final Texture icon, final String title, final String description, final Skin skin) {

        // Initialize a basic TextButton with the required information
        super(title, initSkin(skin));
        this.ICON = new Image(icon);
        LEVEL_DESCRIPTION = new Label(description, getSkin());

        // Produce the correct layout
        rearrangeLayout();
    }

    private void rearrangeLayout() {
        // Constants
        final int ICON_SIZE = 30;
        final float PADDING = 2;

        clearChildren();
        left().padLeft(PADDING);
        add(ICON).size(ICON_SIZE).padRight(PADDING);
        VerticalGroup vg = new VerticalGroup();
        vg.columnAlign(Align.left);
        vg.addActor(getLabel());
        vg.addActor(LEVEL_DESCRIPTION);

        add(vg);
    }

    private static Skin initSkin(Skin skin) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("gui-button-level");
        textButtonStyle.down = skin.newDrawable("gui-button-level", 0.0f,0.0f,0.0f,0.5f);
        textButtonStyle.font = skin.getFont("gui-button-level");
        skin.add("default", textButtonStyle);
        Label.LabelStyle descriptionLayoutStyle = new Label.LabelStyle();
        descriptionLayoutStyle.font = skin.getFont("description-font");
        skin.add("default", descriptionLayoutStyle);
        return skin;
    }
}
