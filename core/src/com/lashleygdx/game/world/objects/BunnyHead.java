package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.util.Constants;
import com.lashleygdx.game.world.Assets;
import com.lashleygdx.game.util.CharacterSkin;
import com.lashleygdx.game.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.lashleygdx.game.util.AudioManager;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * player character
 * @author Chris Lashley
 */
public class BunnyHead extends AbstractGameObject
{
	public static final String TAG = BunnyHead.class.getName();

	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

//	private TextureRegion regHead;
	private Animation animNormal;
	private Animation animCopterTransform;
	private Animation animCopterTransformBack;
	private Animation animCopterRotate;

	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFeatherPowerup;
	public float timeLeftFeatherPowerup;

	public ParticleEffect dustParticles = new ParticleEffect();

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

	/**
	 * constructor
	 */
	public BunnyHead()
	{
		init();
	}

	/**
	 * create the player
	 */
	public void init()
	{
		dimension.set(1, 1);
		animNormal = Assets.instance.bunny.animNormal;
		animCopterTransform = Assets.instance.bunny.animCopterTransform;
		animCopterTransformBack = Assets.instance.bunny.animCopterTransformBack;
		animCopterRotate = Assets.instance.bunny.animCopterRotate;
		setAnimation(animNormal);

		//		regHead = Assets.instance.bunny.head;
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
		hasFeatherPowerup = false;
		timeLeftFeatherPowerup = 0;

		// particles
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
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
				AudioManager.instance.play(Assets.instance.sounds.jump);
				// start counting jump time from the beginning
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
			if (jumpKeyPressed && hasFeatherPowerup)
			{
				AudioManager.instance.play(Assets.instance.sounds.jumpWithFeather, 1, MathUtils.random(1.0f, 1.1f));
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		}
	}

	/**
	 * enable feather powerup state
	 * @param pickedUp
	 */
	public void setFeatherPowerup (boolean pickedUp)
	{
		hasFeatherPowerup = pickedUp;
		if (pickedUp)
			timeLeftFeatherPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
	}

	/**
	 * check if in feather powerup state
	 * @return true if time remaining on feather power up state
	 */
	public boolean hasFeatherPowerup()
	{
		return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
	}

	/**
	 * update player variables
	 */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);
		if (velocity.x != 0)
		{
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
		}
		if (timeLeftFeatherPowerup > 0)
		{
			if (animation == animCopterTransformBack)
			{
				setAnimation(animCopterTransform);
			}
			timeLeftFeatherPowerup -= deltaTime;
			if (timeLeftFeatherPowerup < 0)
			{
				// disable powerup
				timeLeftFeatherPowerup = 0;
				setFeatherPowerup(false);
				setAnimation(animCopterTransformBack);
			}
		}
		dustParticles.update(deltaTime);

		if (hasFeatherPowerup)
		{
			if (animation == animNormal)
			{
				setAnimation(animCopterTransform);
			}
			else if (animation == animCopterTransform)
			{
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animCopterRotate);
			}
		} else
		{
			if (animation == animCopterRotate)
			{
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animCopterTransformBack);
			}
			else if (animation == animCopterTransformBack)
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animNormal);
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
			if (velocity.x != 0)
			{
				dustParticles.setPosition(position.x + dimension.x / 2,  position.y);
				dustParticles.start();
			}
			break;
		case JUMP_RISING:
			// keep track of jump time
			timeJumping += deltaTime;
			// jump time remaining?
			if (timeJumping <= JUMP_TIME_MAX)	// still jumping
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
		{
			dustParticles.allowCompletion();
			super.updateMotionY(deltaTime);
		}
	}

	/**
	 * change player color when in feather powerup state
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;

		// draw particles
		dustParticles.draw(batch);

		// apply selected skin color
		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());

		// set special color when has feather powerup
		if (hasFeatherPowerup)
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		// draw image
		reg = animation.getKeyFrame(stateTime, true);;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);

		// reset color to white
		batch.setColor(1,  1,  1,  1);
	}
}