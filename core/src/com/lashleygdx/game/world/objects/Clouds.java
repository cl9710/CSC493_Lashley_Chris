package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lashleygdx.game.world.Assets;

/**
 * clouds
 * @author Chris Lashley
 */
public class Clouds extends AbstractGameObject
{
	private float length;

	private Array<TextureRegion> regClouds;
	private Array<Cloud> clouds;

	/**
	 * individual cloud
	 * @author Chris Lashley
	 */
	private class Cloud extends AbstractGameObject
	{
		private TextureRegion regCloud;

		/**
		 * constructor not specified so outside classes cant construct
		 */
		public Cloud()
		{

		}

		/**
		 * set cloud texture
		 * @param region
		 */
		public void setRegion (TextureRegion region)
		{
			regCloud = region;
		}

		/**
		 * draw cloud
		 */
		@Override
		public void render(SpriteBatch batch)
		{
			TextureRegion reg = regCloud;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		}
	}

	/**
	 * constructor
	 * @param length
	 */
	public Clouds (float length)
	{
		this.length = length;
		init();
	}

	/**
	 * create clouds to span the level
	 */
	private void init ()
	{
		dimension.set(3.0f, 1.5f);
		regClouds = new Array<TextureRegion>();
		regClouds.add(Assets.instance.levelDecoration.cloud01);
		regClouds.add(Assets.instance.levelDecoration.cloud02);
		regClouds.add(Assets.instance.levelDecoration.cloud03);

		int distFac = 5;
		int numClouds = (int)(length / distFac);
		clouds = new Array<Cloud> (2 * numClouds);
		for (int i = 0; i < numClouds; i++)
		{
			Cloud cloud = spawnCloud();
			cloud.position.x = i * distFac;
			clouds.add(cloud);
		}
	}

	/**
	 * create a cloud
	 * @return cloud
	 */
	private Cloud spawnCloud ()
	{
		Cloud cloud = new Cloud();
		cloud.dimension.set(dimension);
		// select random cloud image
		cloud.setRegion(regClouds.random());
		//position
		Vector2 pos = new Vector2();
		pos.x = length + 10; // position after end of level
		pos.y += 1.75; // base position
		// random height
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1);
		cloud.position.set(pos);
		// speed
		Vector2 speed = new Vector2();
		speed.x += 0.5f;	// base speed
		// random additional speed
		speed.x += MathUtils.random(0.0f, 0.75f);
		cloud.terminalVelocity.set(speed);
		speed.x *= -1;	// move left
		cloud.velocity.set(speed);
		return cloud;
	}

	/**
	 * draw clouds
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		for (Cloud cloud : clouds)
			cloud.render(batch);
	}

	/**
	 * update cloud positions
	 */
	@Override
	public void update(float deltaTime)
	{
		for (int i = clouds.size - 1; i >= 0; i--)
		{
			Cloud cloud = clouds.get(i);
			cloud.update(deltaTime);
			if (cloud.position.x < -10)	// cloud moved outside left edge of world
			{
				// destroy cloud
				clouds.removeIndex(i);
				clouds.add(spawnCloud());
			}
		}
	}
}