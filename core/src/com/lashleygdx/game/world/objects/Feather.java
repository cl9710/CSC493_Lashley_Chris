package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.world.Assets;

/**
 * feathers are powerups that let you multijump
 * feathers are added to the score
 * @author Chris Lashley
 */
public class Feather extends AbstractGameObject
{
	private TextureRegion regFeather;

	public boolean collected;

	/**
	 * constructor
	 */
	public Feather()
	{
		init();
	}

	/**
	 * create a feather
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f);

		regFeather = Assets.instance.feather.feather;

		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		collected = false;
	}

	/**
	 * draw a feather
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;

		TextureRegion reg = null;
		reg = regFeather;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

	/**
	 * feathers are worth 250 pts
	 * @return 250
	 */
	public int getScore()
	{
		return 250;
	}
}