package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.world.Assets;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * rocks are the platforms the player moves around on
 * @author Chris Lashley
 */
public class Rock extends AbstractGameObject
{
	private final float FLOAT_CYCLE_TIME = 2.0f;
	private final float FLOAT_AMPLITUDE = 0.25f;

	private TextureRegion regEdge;
	private TextureRegion regMiddle;

	private int length;

	private float floatCycleTimeLeft;
	private boolean floatingDownwards;
	private Vector2 floatTargetPosition;

	/**
	 * constructor
	 */
	public Rock()
	{
		init();
	}

	/**
	 * create a rock
	 */
	private void init()
	{
		dimension.set(1, 1.5f);

		regEdge = Assets.instance.rock.edge;
		regMiddle = Assets.instance.rock.middle;

		// Start length of this rock
		setLength(1);

		floatingDownwards = false;
		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
		floatTargetPosition = null;
	}

	/**
	 * set a rock length
	 * @param length
	 */
	public void setLength (int length)
	{
		this.length = length;
		// update bounding box for collision detection
		bounds.set(0, 0, dimension.x * length, dimension.y);
	}

	/**
	 * modify a rock length
	 * @param amount
	 */
	public void increaseLength (int amount)
	{
		setLength(length + amount);
	}

	/**
	 * draw the left edge, middle, then right edge of a rock
	 */
	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;

		float relX = 0;
		float relY = 0;

		// Draw left edge
		reg = regEdge;
		relX -= dimension.x / 4;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y,
				dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);

		// Draw middle
		relX = 0;
		reg = regMiddle;
		for (int i = 0; i < length; i++)
		{
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}

		// Draw right edge
		reg = regEdge;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, origin.y,
				dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), true, false);
	}

//	/**
//	 * rocks bob up and down slightly (original)
//	 */
//	@Override
//	public void update (float deltaTime)
//	{
//		super.update(deltaTime);
//
//		floatCycleTimeLeft -= deltaTime;
//		if (floatTargetPosition == null)
//		{
//			floatTargetPosition = new Vector2(position);
//		}
//		if (floatCycleTimeLeft <= 0)
//		{
//			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
//			floatingDownwards = !floatingDownwards;
//			floatTargetPosition.y += FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1);
//		}
//		position.lerp(floatTargetPosition,  deltaTime);
//	}

	/**
	 * rocks bob up and down slightly (box2d version)
	 */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);

		floatCycleTimeLeft -= deltaTime;
		if (floatCycleTimeLeft <= 0)
		{
			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
			floatingDownwards = !floatingDownwards;
			body.setLinearVelocity(0, FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1));
		} else
		{
			body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
		}
	}
}