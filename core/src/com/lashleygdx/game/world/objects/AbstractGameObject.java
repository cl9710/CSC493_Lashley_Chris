package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Abstract game objects
 * @author Chris Lashley
 */
public abstract class AbstractGameObject
{
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;

	public Vector2 acceleration;
	public Rectangle bounds;

	// box2d
	public Body body;
	public float removeIn = 0.0f;

	// animations
	public float stateTime;
	public Animation animation;

	/**
	 * all objects should have these variables
	 */
	public AbstractGameObject()
	{
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();
	}

	/**
	 * all objects need to update variables
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		stateTime += deltaTime;
		if (body == null)	// object not using box2d
		{
			updateMotionX(deltaTime);
			updateMotionY(deltaTime);

			// move to new position
			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
		} else	// object uses box2d
		{
			position.set(body.getPosition());
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
		}
	}

	/**
	 * all objects need draw conditions
	 * @param batch
	 */
	public abstract void render (SpriteBatch batch);

	/**
	 * object x-axis physics
	 * @param deltaTime
	 */
	protected void updateMotionX (float deltaTime)
	{
		if (velocity.x != 0)
		{
			// apply friction
			if (velocity.x > 0)
				velocity.x = Math.max(velocity.x - friction.x * deltaTime,  0);
			else
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
		}
		// apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// make sure object velocity does not exceed terminal velocity
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
	}

	/**
	 * object y-axis physics
	 * @param deltaTime
	 */
	protected void updateMotionY (float deltaTime)
	{
		if (velocity.y != 0)
		{
			// apply friction
			if (velocity.y > 0)
				velocity.y = Math.max(velocity.y - friction.y * deltaTime,  0);
			else
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
		}
		// apply acceleration
		velocity.y += acceleration.y * deltaTime;
		// make sure object velocity does not exceed terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
	}

	public void setAnimation (Animation animation)
	{
		this.animation = animation;
		stateTime = 0;
	}
}