package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.util.AudioManager;
import com.lashleygdx.game.world.Assets;

/**
 * player character
 * @author Chris Lashley
 */
public class Dog extends AbstractGameObject
{

	public static final String TAG = Dog.class.getName();

	private Animation dog;

	/**
	 * constructor
	 */
	public Dog()
	{
		init();
	}

	/**
	 * create the dog
	 */
	public void init()
	{
		dimension.set(1, 1);

		dog = Assets.instance.dog.dog;
		setAnimation(dog);

		origin.set(dimension.x / 2, dimension.y / 2);

		// bounding box for collision detection
		//		bounds.set(origin.x, 0, dimension.x / 5, dimension.y * (4 / 5));
		bounds.set(position.x + dimension.x / 3, 0, dimension.x / 4, dimension.y * 0.75f);
	}

	/**
	 * draw dog
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;

		reg = animation.getKeyFrame(stateTime, false);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), true, false);

		// reset color to white
		batch.setColor(1,  1,  1,  1);
	}
}