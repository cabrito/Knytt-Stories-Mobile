/*
 * KsmResources.java
 * For use in conjunction with the AssetManager to make loading resources much cleaner, while making
 * this obscured file much messier.
 * Created by: scalr on 4/23/2019.
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

package io.github.scalrx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

public final class KsmResources {
	private final AssetManager assetManager;
	private final String buttonDir = "System/buttons/";
	private final String iconDir = buttonDir + "icons/";

	public KsmResources(final AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Texture button(final String TYPE) {
		final String filepath = buttonDir + "Gui_btn_" + TYPE + ".png";
		assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
		assetManager.load(filepath, Texture.class);
		assetManager.finishLoading();
		return assetManager.get(filepath, Texture.class);
	}

	public Texture icon(final String TYPE) {
		final String filepath = iconDir + TYPE + "_icon.png";
		assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
		assetManager.load(filepath, Texture.class);
		assetManager.finishLoading();
		return assetManager.get(filepath);
	}

	// Used for completely unloading the resource we pass into it
	public <T> void dispose(final T resource) {
		String filename = assetManager.getAssetFileName(resource);
		assetManager.unload(filename);
	}
}
