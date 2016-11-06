package com.lashleygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.lashleygdx.game.world.WorldController;
import com.lashleygdx.game.world.WorldRenderer;
import com.lashleygdx.game.util.GamePreferences;

/**
 * game screen displayed while playing
 * @author Chris Lashley
 */
public class GameScreen extends AbstractGameScreen
{
	private static final String TAG = GameScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;

	private boolean paused;

	/**
	 * constructor
	 * @param game
	 */
	public GameScreen (Game game)
	{
		super(game);
	}

	/**
	 * draw the game screen
	 */
	@Override
	public void render(float deltaTime)
	{
		// do not update game world while paused
		if (!paused)
		{
			worldController.update(deltaTime);
		}
		// set clear screen color to: cornflower blue
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		// clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// render game world to screen
		worldRenderer.render();
	}

	/**
	 * set game screen to fill game window
	 */
	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}

	/**
	 * show the game screen
	 */
	@Override
	public void show()
	{
		GamePreferences.instance.load();
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * hide the game screen
	 */
	@Override
	public void hide()
	{
		worldController.dispose();
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}

	/**
	 * pause the game (android only)
	 */
	@Override
	public void pause()
	{
		paused = true;
	}

	/**
	 * resume play
	 */
	@Override
	public void resume()
	{
		super.resume();
		// only called on Android!
		paused = false;
	}
}