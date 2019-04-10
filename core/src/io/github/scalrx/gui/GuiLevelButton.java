package io.github.scalrx.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * GuiLevelButton.java
 * Created by: scalr at 5:39 PM, 4/9/19
 *
 * .
 *
 **************************************************************************************************/
public class GuiLevelButton extends TextButton {
    //private final Label LEVEL_DESCRIPTION;
    private final Image ICON;

    // Constructor
    public GuiLevelButton(final Texture icon, final String title, final String description, Skin skin) {

        // Initialize a basic TextButton with the required information
        super(title, initStyle(skin));
        this.ICON = new Image(icon);
        //LEVEL_DESCRIPTION = new Label(description, getSkin());

        // Produce the correct layout
        //initStyle(new TextButtonStyle());
        rearrangeLayout();

    }

    private void rearrangeLayout() {
        // Constants
        final int ICON_SIZE = 30;
        final float PADDING = 2;

        clearChildren();
        left().padLeft(PADDING);
        add(ICON).size(ICON_SIZE).padRight(PADDING);
        add(getLabel()).row();
        // add(LEVEL_DESCRIPTION);
    }

    private static Skin initStyle(Skin skin) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("gui-button-level");
        textButtonStyle.down = skin.newDrawable("gui-button-level", 0.0f,0.0f,0.0f,0.5f);
        textButtonStyle.font = skin.getFont("gui-button-level");
        skin.add("default", textButtonStyle);
        return skin;
    }
}
