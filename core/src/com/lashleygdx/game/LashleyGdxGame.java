package com.lashleygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.lashleygdx.game.world.WorldController;
import com.lashleygdx.game.world.WorldRenderer;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.assets.AssetManager;
import com.lashleygdx.game.world.Assets;

/**
 * LashleyGdxGame is the "CanyonBunnyMain" class
 * controls the game
 * @author Chris Lashley
 */
public class LashleyGdxGame implements ApplicationListener
{
	private static final String TAG = LashleyGdxGame.class.getName();
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;

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
		// Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		// Game world is active on start
		paused = false;
	}

	/**
	 * draw the game
	 */
	@Override
	public void render ()
	{
		// Do not update game world when paused.
		if (!paused) {
			// Update game world by the time that has passed since last rendered frame.
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		// Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render game world to screen
		worldRenderer.render();
	}

	/**
	 * control game window size
	 */
	@Override
	public void resize (int width, int height)
	{
		worldRenderer.resize(width, height);
	}

	/**
	 * pause the game
	 */
	@Override
	public void pause ()
	{
		paused = true;
	}

	/**
	 * unpause the game
	 */
	@Override
	public void resume ()
	{
		paused = false;
	}

	/**
	 * free unused asset memory
	 */
	@Override
	public void dispose ()
	{
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}