package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.world.Assets;

/**
 * Birds are added to score
 * @author Chris Lashley
 */
public class Bird extends AbstractGameObject
{
	private TextureRegion regBird;

	public boolean collected;

	/**
	 * constructor
	 */
	public Bird()
	{
		init();
	}

	/**
	 * create a new bird for the player to murder
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f);

		regBird = Assets.instance.bird.bird;

		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		collected = false;
	}

	/**
	 * draw un-murdered birds
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;

		TextureRegion reg = null;
		reg = regBird;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

	/**
	 * caught a bird
	 * @return 1
	 */
	public int getScore()
	{
		return 1;
	}
}