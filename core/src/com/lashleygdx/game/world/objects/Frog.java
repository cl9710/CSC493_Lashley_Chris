package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.world.Assets;

/**
 * Frogs are added to score
 * they also increase jump
 * @author Chris Lashley
 */
public class Frog extends AbstractGameObject
{
	private TextureRegion regFrog;
	private TextureRegion regDeadFrog;

	public boolean collected;
	public boolean dead;

	/**
	 * constructor
	 */
	public Frog()
	{
		init();
	}

	/**
	 * create a new frog for the player to murder
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f);

		regFrog = Assets.instance.frog.frog;
		regDeadFrog = Assets.instance.frog.deadFrog;

		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		collected = false;
		dead = false;
	}

	/**
	 * draw un-murdered frogs
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;

		TextureRegion reg = null;

		if (!dead)
		{
			reg = regFrog;
		} else
		{
			reg = regDeadFrog;
		}
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

	/**
	 * caught a frog
	 * @return 1
	 */
	public int getScore()
	{
		return 1;
	}

	/**
	 * flag a frog as dead for rendering at end of level
	 */
	public void isDead()
	{
		dead = true;
	}
}