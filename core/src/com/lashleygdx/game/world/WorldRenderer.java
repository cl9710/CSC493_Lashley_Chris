package com.lashleygdx.game.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.lashleygdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * World Renderer draws assets/objects/world
 * @author Chris Lashley
 */
public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;

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
	}

	// render assets
	public void render ()
	{
		renderTestObjects();
	}

	// render test objects
	private void renderTestObjects()
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Sprite sprite : worldController.testSprites)
		{
			sprite.draw(batch);
		}
		batch.end();
	}

	// resizes camera window
	public void resize (int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
	}

	// free unused asset memory
	@Override public void dispose ()
	{
		batch.dispose();
	}
}