/*
 * AudioData.java
 * A small container designed to help facilitate the audio system when loading maps.
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

package io.github.scalrx.screendata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.github.scalrx.world.World;

public class AudioData {
	private byte musicID;
	private byte atmosAID;
	private byte atmosBID;
	private World world;

	public AudioData(final World world)
	{
		this.world = world;
	}

	public void setAudioBytes(int xID, int yID) {
		if(world.getMap().screenOffsetExists(xID, yID)) {
			final int LAYER_SIZE = 250;
			final int OBJECT_LAYER_SIZE = LAYER_SIZE * 2;
			final int NUMBER_OF_SCENE_LAYERS = 4;
			final int NUMBER_OF_OBJECT_LAYERS = 4;

			// Open Map.bin.raw as a byte array
			FileHandle mapFile = Gdx.files.external(world.getFiles().mapBin(true));
			byte[] mapFileBytes = mapFile.readBytes();

			// Seek the specific location in the Map file
			int attributeLocation = world.getMap().getScreenOffset(xID, yID) +
					(LAYER_SIZE * NUMBER_OF_SCENE_LAYERS) + (OBJECT_LAYER_SIZE * NUMBER_OF_OBJECT_LAYERS);
			int cursorPosition = attributeLocation + 2;
			atmosAID = mapFileBytes[cursorPosition++];
			atmosBID = mapFileBytes[cursorPosition++];
			musicID = mapFileBytes[cursorPosition];
		} else {
			// Initialize audio attributes
			atmosAID = 0;
			atmosBID = 0;
			musicID = 0;
		}
	}

	public byte getMusicID()
	{
		return musicID;
	}

	public byte getAtmosAID()
	{
		return atmosAID;
	}

	public byte getAtmosBID()
	{
		return atmosBID;
	}
}
