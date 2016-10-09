package com.lashleygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.lashleygdx.game.world.Assets;

/**
 * abstract game screen shared variables/methods
 * @author Chris Lashley
 */
public abstract class AbstractGameScreen implements Screen
{
	protected Game game;

	/**
	 * constructor
	 * @param game
	 */
	public AbstractGameScreen (Game game)
	{
		this.game = game;
	}

	/**
	 * draw
	 */
	public abstract void render (float deltaTime);

	/**
	 * change screen size
	 */
	public abstract void resize (int width, int height);

	/**
	 * show screen
	 */
	public abstract void show ();

	/**
	 * hide screen
	 */
	public abstract void hide ();

	/**
	 * pause game (android only so unused)
	 */
	public abstract void pause ();

	/**
	 * play/continue
	 */
	public void resume ()
	{
		Assets.instance.init(new AssetManager());
	}

	/**
	 * clear unused memory
	 */
	public void dispose ()
	{
		Assets.instance.dispose();
	}
}