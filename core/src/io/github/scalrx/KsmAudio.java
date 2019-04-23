package io.github.scalrx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * KsmAudio.java
 * Created by: scalr at 3:48 PM, 3/30/19
 *
 * Handles all of the music-related goodies for Knytt Stories Mobile.
 *
 **************************************************************************************************/

public class KsmAudio
{

    // Music and Ambiance
    private Music music;
    private Music atmosA;
    private Music atmosB;

    // For conveniently handling filesystem calls
    private final AssetManager assetManager;
    private final KsmFiles files;

    // Is the music supposed to be fading out?
	private boolean fading;

    /***********************************************************************************************			 Constructors */
	////////////////////////////////////////////////////////////////////////////////////////////////
    public KsmAudio(final AssetManager assetManager, final KsmFiles files)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        this.assetManager = assetManager;
        this.files = files;
        this.fading = false;
    }

    /***********************************************************************************************		 Methods for handling music */
    // Call before loading music
	////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadMusic(byte muid)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        assetManager.load(files.music(muid), Music.class);
        assetManager.finishLoading();
    }

    // Call before loading ambiance
	////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadAmbience(byte amid)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        assetManager.load(files.ambiance(amid), Music.class);
        assetManager.finishLoading();
    }

    // Revision of the music subsystem
	////////////////////////////////////////////////////////////////////////////////////////////////
	public void playMusic(byte muid)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		// Assuming we're dealing with *some* audio track
		if((muid & 0xFF) > 0)
		{
			// Try to play it
			if (assetManager.isLoaded(files.music(muid)))
			{
				// Immediately stop anything that WAS playing (if needed)
				if (music != null)
				{
					music.stop();
					music.dispose();
				}

				// And then play the new track
				music = assetManager.get(files.music(muid), Music.class);
				music.setVolume(1f);
				music.setLooping(true);
				music.play();
			} else {
				return;    // Otherwise, do nothing; maybe the level references a non-existent audio track? e.g., Song0.ogg
			}
		}
	}

    // Load the ambiance track
	////////////////////////////////////////////////////////////////////////////////////////////////
    public void playAmbiance(byte atmosAID, byte atmosBID)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		// Logic for playing/stopping atmosA
		if((atmosAID & 0xFF) > 0) {
			if(assetManager.isLoaded(files.ambiance(atmosAID))) {
				if(!assetManager.get(files.ambiance(atmosAID), Music.class).isPlaying()) {
					if(atmosA != null) {
						atmosA.stop();
						atmosA.dispose();
					}
					atmosA = assetManager.get(files.ambiance(atmosAID), Music.class);
					atmosA.setLooping(true);
					atmosA.play();
				}

			}
		} else {
			if(atmosA != null)
				atmosA.stop();
		}

		// Logic for playing/stopping atmosB
		if((atmosBID & 0xFF) > 0) {
			if(assetManager.isLoaded(files.ambiance(atmosBID))) {
				if(!assetManager.get(files.ambiance(atmosBID), Music.class).isPlaying()) {
					if(atmosB != null) {
						atmosB.stop();
						atmosB.dispose();
					}
					atmosB = assetManager.get(files.ambiance(atmosBID), Music.class);
					atmosB.setLooping(true);
					atmosB.play();
				}

			}
		} else {
			if(atmosB != null)
				atmosB.stop();
		}
    }

    public void prepareScreenAudio(byte musicID, byte atmosAID, byte atmosBID)
	{
		// Now that we have the musicID, atmosA, and atmosB bytes, try loading such audio files
		if((musicID & 0xFF) > 0)
			loadMusic(musicID);
		if((atmosAID & 0xFF) > 0)
			loadAmbience(atmosAID);
		if((atmosBID & 0xFF) > 0)
			loadAmbience(atmosBID);

		// Check whether or not we need to manipulate the music
		if(!isSongPlaying(musicID))
		{
			if(!music.isPlaying())
			{
				playMusic(musicID);
			} else {
				setFading(true);
			}
		}

		playAmbiance(atmosAID, atmosBID);
	}

    /***********************************************************************************************	Routines for manipulating audio */
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isFading()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		return fading;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	public void setFading(boolean fading)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
    	this.fading = fading;
	}

	// Logic for *HOW* to perform a music fadeout
	////////////////////////////////////////////////////////////////////////////////////////////////
	public void fadeMusic(float delta)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		float currentVolume = music.getVolume();
		final float SCALE_FACTOR = 0.35f;
		final float THRESHOLD = 0.02f;
		if(music.isPlaying())
		{
			if(currentVolume >= THRESHOLD)
			{
				music.setVolume(currentVolume - (SCALE_FACTOR * delta));

			} else {
				music.stop();
				music.dispose();
				fading = false;
			}
		} else {
			fading = false;
		}
	}

	// Logic for *WHEN* to perform a music fadeout, and what to do afterwards.
	////////////////////////////////////////////////////////////////////////////////////////////////
	public void handleFadeout(float delta, byte muid)
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
		if(isFading())
		{
			fadeMusic(delta);
		}
		else {
			if(!music.isPlaying() && (muid > 0))
			{
				playMusic(muid);
			}
		}
	}

	// Much cleaner, more generic way to stop each individual audio
	////////////////////////////////////////////////////////////////////////////////////////////////
    public void stopMusic()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        if(music != null)
            music.stop();
    }
	////////////////////////////////////////////////////////////////////////////////////////////////
    public void stopAmbience()
	////////////////////////////////////////////////////////////////////////////////////////////////
	{
        if(atmosA != null)
            atmosA.stop();
        if(atmosB != null)
            atmosB.stop();
    }

	/***********************************************************************************************	Getters */
	public boolean isSongPlaying(byte muid)
	{
		if(music.isPlaying() && (muid > 0))
		{
			if(assetManager.get(files.music(muid), Music.class).isPlaying())
			{
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Music getMusic()
	{
		return music;
	}
}
