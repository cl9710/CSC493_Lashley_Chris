package com.lashleygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.lashleygdx.game.util.*;

/**
 * game preferences
 * @author Chris Lashley
 */
public class GamePreferences
{
	public static final String TAG = GamePreferences.class.getName();

	public static final GamePreferences instance = new GamePreferences();

	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	public int charSkin;
	public boolean showFpsCounter;
	public int[] highScore = new int[Constants.MAX_SCORES];
	public int first;
	public int second;
	public int third;
	public int fourth;
	public int fifth;


	private Preferences prefs;

	/**
	 *  singleton: prevent instantiation from other classes
	 */
	private GamePreferences()
	{
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
		highScore[0] = prefs.getInteger("first", 0);
		highScore[1] = prefs.getInteger("second", 0);
		highScore[2] =	prefs.getInteger("third", 0);
		highScore[3] = prefs.getInteger("fourth", 0);
		highScore[4] = prefs.getInteger("fifth", 0);
	}

	/**
	 * load current/default preferences
	 */
	public void load()
	{
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
		charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0), 0, 2);
		showFpsCounter = prefs.getBoolean("showFpsCounter", false);
	}

	/**
	 * save current preferences
	 */
	public void save()
	{
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		prefs.putInteger("charSkin", charSkin);
		prefs.putBoolean("showFpsCounter",  showFpsCounter);
		prefs.flush();
	}

	/**
	 * save high scores
	 */
	public void saveHighScores()
	{
		prefs.putInteger("first", highScore[0]);
		prefs.putInteger("second", highScore[1]);
		prefs.putInteger("third", highScore[2]);
		prefs.putInteger("fourth", highScore[3]);
		prefs.putInteger("fifth", highScore[4]);
		prefs.flush();
	}

	/**
	 * load high scores
	 */
	public void loadHighScores()
	{
		highScore[0] = 	prefs.getInteger("first",0);
		highScore[1] = 	prefs.getInteger("second", 0);
		highScore[2] = 	prefs.getInteger("third", 0);
		highScore[3] = 	prefs.getInteger("fourth", 0);
		highScore[4] = 	prefs.getInteger("fifth[4]", 0);
	}
}