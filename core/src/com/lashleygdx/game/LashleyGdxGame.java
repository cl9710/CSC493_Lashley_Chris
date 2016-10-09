package com.lashleygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.lashleygdx.game.world.Assets;
import com.lashleygdx.game.screens.MenuScreen;

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
		// start game at menu screen
		setScreen(new MenuScreen(this));
	}
}