/*
 * KsmFiles.java
 * Redo of the class that handles all of the filesystem calls for Knytt Stories Mobile. In
 * particular, this is response to Android needing AssetManager to swap between Internal and
 * External loaders; the previous attempt had to be scrapped for this reason.
 * Created by: scalr on 3/30/2019.
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class KsmFiles {
	// Constants
	private final String ksmDir = "Knytt Stories Mobile/";
	private final FileHandleResolver internal = new InternalFileHandleResolver();
	private final FileHandleResolver external = new ExternalFileHandleResolver();
	private final AssetManager assetManager;
	private final KsmResources res;

	// Members
	private String worldDir;                                // DO NOT rely on this going forward!!!

	public KsmFiles(AssetManager assetManager) {
		this.assetManager = assetManager;
		this.res = new KsmResources(assetManager);
	}

	public void setWorldDir(String author, String worldName) {
		this.worldDir = author + " - " + worldName + "/";
	}

	public void resetWorldDir() {
		worldDir = null;
	}

	public String getKsmDir() {
		return ksmDir;
	}

	public String getWorldDir() {
		return worldDir;
	}

	public KsmResources resources() {
		return res;
	}

	public String mapBin(boolean raw) {
		return raw ? (ksmDir + getWorldDir() + "Map.bin.raw") : (ksmDir + getWorldDir() + "Map.bin");
	}

	public String music(byte muid) {
		// If the world directory hasn't been set yet, load normal music
		if (worldDir == null || worldDir.isEmpty()) {
			// Swap the music loader for the AssetManager to load internal music files
			assetManager.setLoader(Music.class, new MusicLoader(internal));
			return "Data/Music/Song" + (muid & 0xFF) + ".ogg";
		}

		// Swap the loader for external really quick for the following if-else statement...
		assetManager.setLoader(Music.class, new MusicLoader(external));

		// If there is music for this screen, provide it
		if (Gdx.files.external(ksmDir + getWorldDir() + "Music/Song" + (muid & 0xFF) + ".ogg").exists())
			return ksmDir + getWorldDir() + "Music/Song" + (muid & 0xFF) + ".ogg";
		else {
			// Swap the music loader back to load internal music files
			assetManager.setLoader(Music.class, new MusicLoader(internal));
			return "Data/Music/Song" + (muid & 0xFF) + ".ogg";
		}
	}

	public String ambiance(byte amid) {
		// If the world directory hasn't been set yet, load normal ambiance
		if (worldDir == null || worldDir.isEmpty()) {
			// Swap the music loader for the AssetManager to load internal ambiance files
			assetManager.setLoader(Music.class, new MusicLoader(internal));
			return "Data/Ambiance/Ambi" + (amid & 0xFF) + ".ogg";
		}

		// Swap the loader for external really quick for the following if-else statement...
		assetManager.setLoader(Music.class, new MusicLoader(external));

		// If there is ambiance for this screen, provide it
		if (Gdx.files.external(ksmDir + getWorldDir() + "Ambiance/Ambi" + (amid & 0xFF) + ".ogg").exists()) {
			return ksmDir + getWorldDir() + "Ambiance/Ambi" + (amid & 0xFF) + ".ogg";
		} else {
			// Swap the music loader back to load internal ambiance files
			assetManager.setLoader(Music.class, new MusicLoader(internal));
			return "Data/Ambiance/Ambi" + (amid & 0xFF) + ".ogg";
		}
	}

	public String tileset(byte tsid) {
		// Swap the loader for external really quick for the following if-else statement...
		assetManager.setLoader(Texture.class, new TextureLoader(external));

		// Provide custom tileset filepath if available
		if (Gdx.files.external(ksmDir + getWorldDir() + "Tilesets/Tileset" + (tsid & 0xFF) + ".png").exists()) {
			return ksmDir + getWorldDir() + "Tilesets/Tileset" + (tsid & 0xFF) + ".png";
		}
		// Otherwise, use vanilla tilesets instead
		else {
			// Swap the texture loader back to load internal ambiance files
			assetManager.setLoader(Texture.class, new TextureLoader(internal));
			return "Data/Tilesets/Tileset" + (tsid & 0xFF) + ".png";
		}
	}

	// Custom Tilesets
	public String gradient(byte gid) {
		// Swap the loader for external really quick for the following if-else statement...
		assetManager.setLoader(Texture.class, new TextureLoader(external));

		// Provide custom gradients filepath if available
		if (Gdx.files.external(ksmDir + getWorldDir() + "Gradients/Gradient" + (gid & 0xFF) + ".png").exists()) {
			return ksmDir + getWorldDir() + "Gradients/Gradient" + (gid & 0xFF) + ".png";
		}
		// Otherwise, use vanilla gradients instead
		else {
			// Swap the texture loader back to load internal ambiance files
			assetManager.setLoader(Texture.class, new TextureLoader(internal));
			return "Data/Gradients/Gradient" + (gid & 0xFF) + ".png";
		}
	}
}
