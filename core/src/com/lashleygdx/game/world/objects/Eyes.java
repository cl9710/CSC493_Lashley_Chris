package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lashleygdx.game.world.Assets;

/**
 * eyes in the forest background
 * @author Chris Lashley
 */
public class Eyes extends AbstractGameObject
{
	private float length;

	private Array<TextureRegion> regEyes;
	private Array<EyePair> eyes;

	/**
	 * individual pair of eyes
	 * @author Chris Lashley
	 */
	private class EyePair extends AbstractGameObject
	{
		private TextureRegion regEye;

		/**
		 * constructor not specified so outside classes cant construct
		 */
		public EyePair()
		{

		}

		/**
		 * set eye texture
		 * @param region
		 */
		public void setRegion (TextureRegion region)
		{
			regEye = region;
		}

		/**
		 * draw pair of eyes
		 */
		@Override
		public void render(SpriteBatch batch)
		{
			TextureRegion reg = regEye;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		}
	}

	/**
	 * constructor
	 * @param length
	 */
	public Eyes (float length)
	{
		this.length = length;
		init();
	}

	/**
	 * create eyes to span the level
	 */
	private void init ()
	{
		dimension.set(1.0f, 1.0f);
		regEyes = new Array<TextureRegion>();
		regEyes.add(Assets.instance.levelDecoration.eye01);
		regEyes.add(Assets.instance.levelDecoration.eye02);
		regEyes.add(Assets.instance.levelDecoration.eye03);

		int distFac = 5;
		int numEyes = (int)((length + 20) / distFac);
		eyes = new Array<EyePair> (2 * numEyes);
		for (int i = 0; i < numEyes; i++)
		{
			EyePair eyePair = spawnEyePair();
			eyePair.position.x = i * distFac - 10;
			eyes.add(eyePair);
		}
	}

	/**
	 * spawn a pair of eyes that continuously scroll rightward across the level
	 * @return eyePair
	 */
	private EyePair spawnEyePair ()
	{
		EyePair eyes = new EyePair();
		// select random eye size
		Vector2 size = new Vector2();
		size.x = size.y = MathUtils.random(0.1f, 0.5f); // random sized eyes
		eyes.dimension.set(size);
		// select random eye image
		eyes.setRegion(regEyes.random());
		//position
		Vector2 pos = new Vector2();
		pos.x = -10; // position before beginning of level
		pos.y = 0; // base height
		// random height
		pos.y += MathUtils.random(0.0f, 0.5f) * (MathUtils.randomBoolean() ? 1 : -1);
		eyes.position.set(pos);
		// speed
		Vector2 speed = new Vector2();
		speed.x += 0.5f;	// base speed
		// random additional speed
		speed.x += MathUtils.random(0.25f, 1.0f);
		eyes.terminalVelocity.set(speed);
		eyes.velocity.set(speed);
		return eyes;
	}

	/**
	 * draw eyes
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		for (EyePair eyePair : eyes)
			eyePair.render(batch);
	}

	/**
	 * update eye positions
	 */
	@Override
	public void update(float deltaTime)
	{
		for (int i = eyes.size - 1; i >= 0; i--)
		{
			EyePair eyePair = eyes.get(i);
			eyePair.update(deltaTime);
			if (eyePair.position.x > length + 10)	// eyes moved outside right edge of world
			{
				// destroy eyes
				eyes.removeIndex(i);
				eyes.add(spawnEyePair());
			}
		}
	}
}