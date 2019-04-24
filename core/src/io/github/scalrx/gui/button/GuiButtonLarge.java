package io.github.scalrx.gui.button;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * button.java
 * Created by: scalr at 11:48 AM, 4/20/19
 *
 * Streamlines the creation of a large button, typically used on the main menu.
 *
 **************************************************************************************************/
public class GuiButtonLarge extends ImageTextButton
{
    private final Image ICON;

    /***********************************************************************************************			 Constructors */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public GuiButtonLarge(final Texture icon, final String title, final Skin skin)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {

        // Initialize a basic ImageTextButton with the required information
        super(title, initSkin(skin));
        this.ICON = new Image(icon);

        // Produce the correct layout
        rearrangeLayout();
    }

    /***********************************************************************************************			 Methods */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void rearrangeLayout()
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        // Constants
        final int ICON_SIZE = 50;

        clearChildren();
        add(ICON).size(ICON_SIZE).row();
        add(getLabel());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static Skin initSkin(Skin skin)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    {
        ImageTextButtonStyle imageTextButtonStyle = new ImageTextButtonStyle();
        imageTextButtonStyle.up = skin.newDrawable("gui-button-large");
        imageTextButtonStyle.down = skin.newDrawable("gui-button-large", 0.0f,0.0f,0.0f,0.5f);
        imageTextButtonStyle.font = skin.getFont("gui-button-large");
        imageTextButtonStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("default", imageTextButtonStyle);
        return skin;
    }
}
