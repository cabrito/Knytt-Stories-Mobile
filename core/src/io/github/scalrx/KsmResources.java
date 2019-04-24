package io.github.scalrx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * KsmResources.java
 * Created by: scalr at 7:59 PM, 4/23/19
 *
 * For use in conjunction with the AssetManager to make loading resources much cleaner, while making
 * this obscured file much messier.
 *
 **************************************************************************************************/
public final class KsmResources
{
	private final AssetManager assetManager;

	/***********************************************************************************************             Constructors */
	////////////////////////////////////////////////////////////////////////////////////////////////
	public KsmResources(final AssetManager assetManager)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		this.assetManager = assetManager;
	}

	/***********************************************************************************************             Buttons */
	private final String buttonDir = "System/buttons/";

	////////////////////////////////////////////////////////////////////////////////////////////////
	public Texture button(final String TYPE)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		final String filepath = buttonDir + "Gui_btn_" + TYPE + ".png";
		assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
		assetManager.load(filepath, Texture.class);
		assetManager.finishLoading();
		return assetManager.get(filepath, Texture.class);
	}

	/***********************************************************************************************             Button Icons */
	private final String iconDir = buttonDir + "icons/";

	////////////////////////////////////////////////////////////////////////////////////////////////
	public Texture icon(final String TYPE)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		final String filepath = iconDir + TYPE + "_icon.png";
		assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
		assetManager.load(filepath, Texture.class);
		assetManager.finishLoading();
		return assetManager.get(filepath);
	}

	/***********************************************************************************************             Disposal */
	// Used for completely unloading the resource we pass into it
	////////////////////////////////////////////////////////////////////////////////////////////////
	public <T> void dispose(final T resource)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		String filename = assetManager.getAssetFileName(resource);
		assetManager.unload(filename);
	}
}
