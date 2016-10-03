package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.lashleygdx.game.util.Constants;
import com.lashleygdx.game.world.Assets;

/**
 * player character
 * @author Chris Lashley
 */
public class Cat extends AbstractGameObject
{

	public static final String TAG = Cat.class.getName();

	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

	public enum VIEW_DIRECTION
	{	LEFT,
		RIGHT
	}

	public enum JUMP_STATE
	{
		GROUNDED,
		FALLING,
		JUMP_RISING,
		JUMP_FALLING
	}

	private TextureRegion regCat;
	private TextureRegion regBloodlust;

	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFrogPowerup;
	public float timeLeftFrogPowerup;
	public boolean hasBloodlust;
	public float timeLeftBloodlust;


	/**
	 * constructor
	 */
	public Cat()
	{
		init();
	}

	/**
	 * create the player
	 */
	public void init()
	{
		dimension.set(1, 1);
		regCat = Assets.instance.cat.cat;
		regBloodlust = Assets.instance.cat.bloodlust;
		// center image on game object
		origin.set(dimension.x /2, dimension.y / 2);
		// bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// view direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		//powerups
		hasFrogPowerup = false;
		timeLeftFrogPowerup = 0;
		hasBloodlust = false;
		timeLeftBloodlust = 0;
	}

	/**
	 * player jump
	 * @param jumpKeyPressed
	 */
	public void setJumping (boolean jumpKeyPressed)
	{
		switch (jumpState)
		{
		case GROUNDED:	// character is standing on a platform
			if (jumpKeyPressed)
			{
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		case JUMP_RISING:	// rising in the air
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.FALLING;
			break;
		case FALLING:	// falling down
		case JUMP_FALLING:	// falling down after jump
			if (jumpKeyPressed && hasFrogPowerup)
			{
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		}
	}

	/**
	 * enable frog jump state
	 * frog jump enables unlimited air jumps for a limited time
	 * @param pickedUp
	 */
	public void setFrogPowerup (boolean pickedUp)
	{
		hasFrogPowerup = pickedUp;
		if (pickedUp)
			timeLeftFrogPowerup = Constants.ITEM_FROG_POWERUP_DURATION;
	}

	/**
	 * check if in frog jump state
	 * @return true if time remaining on frog jump state
	 */
	public boolean hasFrogPowerup()
	{
		return hasFrogPowerup && timeLeftFrogPowerup > 0;
	}

	/**
	 * enable bloodlust state
	 * bloodlust gives the player double speed for 3 seconds per state stack
	 * bloodlust states can stack indefinitely
	 */
	public void setBloodlust(boolean pickedUp)
	{
		hasBloodlust = pickedUp;
		terminalVelocity.set(6.0f, 4.0f);
		if (pickedUp)
			timeLeftBloodlust += Constants.BLOODLUST_POWERUP_DURATION;

	}

	/**
	 * check if in bloodlust state
	 * @return
	 */
	public boolean hasBloodlust()
	{
		return hasBloodlust && timeLeftBloodlust > 0;
	}

	/**
	 * update player variables
	 */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);;
		if (velocity.x != 0)
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
		if (timeLeftFrogPowerup > 0)
		{
			timeLeftFrogPowerup -= deltaTime;
			if (timeLeftFrogPowerup < 0)
			{
				// disable powerup
				timeLeftFrogPowerup = 0;
				setFrogPowerup(false);
			}
		}
		if (timeLeftBloodlust > 0)
		{
			timeLeftBloodlust -= deltaTime;
			if (timeLeftBloodlust < 0)
			{
				timeLeftBloodlust = 0;
				hasBloodlust = false;
				terminalVelocity.set(3.0f, 4.0f);

			}
		}
	}

	/**
	 * jump motion physics
	 */
	@Override
	protected void updateMotionY (float deltaTime)
	{
		switch (jumpState)
		{
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			break;
		case JUMP_RISING:
			// keep track of jump time
			timeJumping += deltaTime;
			// jump time remaining?
			if (timeJumping <= 2 * JUMP_TIME_MAX)	// still jumping
				velocity.y = terminalVelocity.y;
			break;
		case FALLING:
			break;
		case JUMP_FALLING:
			// add delta time to track jump time
			timeJumping += deltaTime;
			// jump minimal height if jump key pressed too short
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
				velocity.y = terminalVelocity.y;
		}
		if (jumpState != JUMP_STATE.GROUNDED)
			super.updateMotionY(deltaTime);
	}

	/**
	 * change player color when in frog powerup state
	 * change player texture when in bloodlust state
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;

		// set special color when has frog powerup (overrides bloodlust color)
		if (hasFrogPowerup)
		{
			batch.setColor(0.0f, 1.0f, 0.0f, 1.0f);
			if (timeLeftFrogPowerup < 3)	// fade player in/out if have frog powerup running low 5/s
			{
				if (((int)(timeLeftFrogPowerup * 5) % 2) != 0)
				{
					batch.setColor(0, 1, 0, 0.5f);
				}
			}
		}
		// draw image
		if (hasBloodlust())	// use bloodlust image if has bloodlust
		{
			reg = regBloodlust;
		} else	// use regular cat image
		{
			reg = regCat;
		}
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);

		// reset color to white
		batch.setColor(1,  1,  1,  1);
	}
}