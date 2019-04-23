package io.github.scalrx.screendata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import io.github.scalrx.World;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * AudioData.java
 * Created by: scalr at 4:33 PM, 4/23/19
 *
 * A small container designed to help facilitate the audio system when loading maps.
 *
 **************************************************************************************************/
public class AudioData
{
	private byte musicID;
	private byte atmosAID;
	private byte atmosBID;
	private World world;

	/***********************************************************************************************			Constructors */
	public AudioData(final World world)
	{
		this.world = world;
	}

	/***********************************************************************************************			Methods */
	////////////////////////////////////////////////////////////////////////////////////////////////
	public void setAudioBytes(int xID, int yID)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		if(world.getMap().screenOffsetExists(xID, yID))
		{
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

	/***********************************************************************************************			Getters and setters */
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
