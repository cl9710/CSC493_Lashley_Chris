package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.lashleygdx.game.util.Constants;
import com.lashleygdx.game.util.GamePreferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * World Renderer draws assets/objects/world
 * @author Chris Lashley
 */
public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera cameraGUI;

	/**
	 * get or create the world renderer
	 * @param worldController
	 */
	public WorldRenderer (WorldController worldController)
	{
		this.worldController = worldController;
		init();
	}

	/**
	 * create a world renderer
	 */
	private void init ()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		cameraGUI = new OrthographicCamera (Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);	// flip y axis
		cameraGUI.update();
	}

	/**
	 * render game
	 */
	public void render ()
	{
		renderWorld(batch);
		renderGui(batch);
	}

	/**
	 * render level and objects
	 * @param batch
	 */
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}

	/**
	 * resize game window
	 * @param width
	 * @param height
	 */
	public void resize (int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}

	/**
	 * draw birds murdered
	 * @param batch
	 */
	private void renderGuiBirdScore (SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.bird.birdUp, x, y, 50, 50, 100, 100, 0.35f, -0.35f,  0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.birdScore, x + 75, y + 37);
	}

	/**
	 * draw frogs murdered
	 * @param batch
	 */
	private void renderGuiFrogScore (SpriteBatch batch)
	{
		float x = -15;
		float y = 20;
		batch.draw(Assets.instance.frog.frog, x, y, 50, 50, 100, 100, 0.35f, -0.35f,  0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.frogScore, x + 75, y + 37);
	}

	/**
	 * draw lives remaining
	 * @param batch
	 */
	private void renderGuiExtraLive (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 100;
		float y = -15;
		batch.draw(Assets.instance.cat.cat, x, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.lives, x + 75, y + 37);

		// lose a life animation
		if (worldController.lives >= 0 && worldController.livesVisual > worldController.lives)
		{
//			int i = worldController.lives;
			float alphaColor = Math.max(0,  worldController.livesVisual - worldController.lives - 0.5f);
			float alphaScale = 0.35f * (2 + worldController.lives - worldController.livesVisual)* 2;
			float alphaRotate = -35 * alphaColor;
			batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
			batch.draw(Assets.instance.cat.cat, x, y, 50, 50, 120, 100, alphaScale, -alphaScale, alphaRotate);
			batch.setColor(1, 1, 1, 1);
		}
	}

	/**
	 * draw fps
	 * @param batch
	 */
	private void renderGuiFpsCounter (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps >= 45)
		{
			// 45+ fps displays green
			fpsFont.setColor(0, 1, 0, 1);
		}
		else if (fps >= 30)
		{
			// 30+ fps displays yellow
			fpsFont.setColor(1, 1, 0, 1);
		}
		else
		{
			// below 30 fps displays red
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch,  "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); //white
	}

	/**
	 * draw user gui
	 * @param batch
	 */
	private void renderGui (SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		if (!worldController.isGameOver())
		{
			// draw killed birds icon + text anchored to top left corner
			renderGuiBirdScore(batch);
			// draw killed frogs icon + text anchored to top left corner
			renderGuiFrogScore(batch);
			// draw bloodlust powerup icon anchored below frog score
			renderGuiBloodlustPowerup(batch);
			// draw frog powerup icon anchored below frog score
			renderGuiFrogPowerup(batch);
			// draw extra lives icon + text anchored to top right corner
			renderGuiExtraLive(batch);
			// draw fps text anchored to bottom right corner
			if (GamePreferences.instance.showFpsCounter)
				renderGuiFpsCounter(batch);
		}
		// draw dead text
		renderGuiDeath(batch);
		// draw victory text
		renderGuiVictory(batch);
		// draw game over text
		renderGuiGameOverMessage(batch);

		batch.end();
	}

	/**
	 * free unused asset memory
	 */
	@Override public void dispose ()
	{
		batch.dispose();
	}

	/**
	 * display game over text
	 * @param batch
	 */
	private void renderGuiGameOverMessage (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth * 6 / 15;
		float y = cameraGUI.viewportHeight / 2;
		if (worldController.isGameOver())
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.74f, 0.25f, 1);
			//fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 0, BitmapFont.HAlignment.CENTER); // not supported anymore
			fontGameOver.draw(batch, "GAME OVER", x, y);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}

	/**
	 * draw the gui to show when you have frog powerup
	 * @param batch
	 */
	private void renderGuiFrogPowerup (SpriteBatch batch)
	{
		float x = -15;
		float y = 80;
		float timeLeftFrogPowerup = worldController.level.cat.timeLeftFrogPowerup;
		if (timeLeftFrogPowerup > 0)
		{
			if (timeLeftFrogPowerup < 4)	// fade icon in/out if remaining powerup time is less than 4s with interval 5/s
			{
				if (((int)(timeLeftFrogPowerup * 5) % 2) != 0)
				{
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
			batch.draw(Assets.instance.frog.frog, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
			Assets.instance.fonts.defaultSmall.draw(batch,  "" + (int)timeLeftFrogPowerup, x + 60, y + 57);
		}
	}

	/**
	 * draw the gui to show when you have frog powerup
	 * @param batch
	 */
	private void renderGuiBloodlustPowerup (SpriteBatch batch)
	{
		float x = -15;
		float y = 55;
		float timeLeftBloodlust = worldController.level.cat.timeLeftBloodlust;
		if (timeLeftBloodlust > 0)
		{
			if (timeLeftBloodlust < 4)	// fade icon in/out if remaining powerup time is less than 4s with interval 5/s
			{
				if (((int)(timeLeftBloodlust * 5) % 2) != 0)
				{
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
			batch.draw(Assets.instance.bird.birdUp, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
			Assets.instance.fonts.defaultSmall.draw(batch,  "" + (int)timeLeftBloodlust, x + 60, y + 57);
		}
	}

	/**
	 * display victory text
	 * @param batch
	 */
	private void renderGuiVictory (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth * 6 / 15;
		float y = cameraGUI.viewportHeight / 2;
		if (worldController.isVictory())
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.74f, 0.25f, 1);
			//fontGameOver.drawMultiLine(batch, "YOU WIN", x, y, 0, BitmapFont.HAlignment.CENTER); // not supported anymore
			fontGameOver.draw(batch, "YOU WIN", x, y);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}

	/**
	 * display death text
	 * @param batch
	 */
	private void renderGuiDeath (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth * 6 / 15;
		float y = cameraGUI.viewportHeight / 2;
		if (worldController.isDead() && !worldController.isGameOver())
		{
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.74f, 0.25f, 1);
			//fontGameOver.drawMultiLine(batch, "YOU WIN", x, y, 0, BitmapFont.HAlignment.CENTER); // not supported anymore
			fontGameOver.draw(batch, "YOU DIED", x, y);
			fontGameOver.setColor(1, 1, 1, 1);
		}
	}
}