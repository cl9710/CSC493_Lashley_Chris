package com.lashleygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.lashleygdx.game.world.Assets;
import com.lashleygdx.game.screens.MenuScreen;
import com.lashleygdx.game.util.AudioManager;
import com.lashleygdx.game.util.GamePreferences;

/**
 * LashleyGdxGame is the "MurderKittyMain" class
 * creates an instance of the game
 * @author Chris Lashley
 */
public class LashleyGdxGame extends Game
{
/**
 * create a new instance of the game
 */
	@Override
	public void create ()
	{
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());

		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.normalSong);

		// start game at menu screen
		setScreen(new MenuScreen(this));
	}
}