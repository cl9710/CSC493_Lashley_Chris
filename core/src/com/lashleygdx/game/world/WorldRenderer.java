package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.lashleygdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

	// get or create an instance of itself if needed
	public WorldRenderer (WorldController worldController)
	{
		this.worldController = worldController;
		init();
	}

	// initialize an instance of itself
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

	// render assets
	public void render ()
	{
//		renderTestObjects();
		renderWorld(batch);
		renderGui(batch);
	}

//	// render test objects
//	private void renderTestObjects()
//	{
//		worldController.cameraHelper.applyTo(camera);
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		for (Sprite sprite : worldController.testSprites)
//		{
//			sprite.draw(batch);
//		}
//		batch.end();
//	}

	// render test objects
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}

	// resizes camera window
	public void resize (int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}

	// display birds
	private void renderGuiBirdScore (SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.bird.bird, x, y, 50, 50, 100, 100, 0.35f, -0.35f,  0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.birds, x + 75, y + 37);
	}

	// display frogs
	private void renderGuiFrogScore (SpriteBatch batch)
	{
		float x = -15;
		float y = 20;
		batch.draw(Assets.instance.frog.frog, x, y, 50, 50, 100, 100, 0.35f, -0.35f,  0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.frogs, x + 75, y + 37);
	}

	// display lives
	private void renderGuiExtraLive (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 100;// - Constants.LIVES_START * 50;
		float y = -15;
		batch.draw(Assets.instance.cat.cat, x, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.lives, x + 75, y + 37);
	}

	// display fps
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

	// display gui
	private void renderGui (SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		// draw killed birds icon + text anchored to top left corner
		renderGuiBirdScore(batch);
		// draw killed frogs icon + text anchored to top left corner
		renderGuiFrogScore(batch);
		// draw extra lives icon + text anchored to top right corner
		renderGuiExtraLive(batch);
		// draw fps text anchored to bottom right corner
		renderGuiFpsCounter(batch);
		batch.end();
	}

	// free unused asset memory
	@Override public void dispose ()
	{
		batch.dispose();
	}
}