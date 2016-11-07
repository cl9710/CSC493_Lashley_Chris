package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.world.Assets;

/**
 * House is the goal
 * @author Chris Lashley
 */
public class House extends AbstractGameObject
{
	private TextureRegion regHouse;

	public boolean reached;

	/**
	 * constructor
	 */
	public House()
	{
		init();
	}

	/**
	 * create a house for the player to reach
	 */
	private void init()
	{
		dimension.set(3.0f, 3.0f);
		origin.set(dimension.x /2, dimension.y / 2);

		regHouse = Assets.instance.house.house;

		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		//		bounds.set(1, Float.MIN_VALUE, 10, Float.MAX_VALUE);
		origin.set(dimension.x / 2, 0.0f);

		reached = false;
	}

	/**
	 * draw house
	 */
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regHouse;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}