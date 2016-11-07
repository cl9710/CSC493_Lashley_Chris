package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lashleygdx.game.world.Assets;

/**
 * Birds are added to score
 * birds flap their wings and bob up and down slightly
 * @author Chris Lashley
 */
public class Bird extends AbstractGameObject
{
	private final float FLOAT_CYCLE_TIME = 2.0f;
	private final float FLOAT_AMPLITUDE = 0.25f;
	private final float FLAP_SPEED = 0.25f;

	private TextureRegion regBirdUp;
	private TextureRegion regBirdDown;
	private TextureRegion regDeadBird;

	public boolean collected;

	public ParticleEffect bloodParticles = new ParticleEffect();

	private float floatCycleTimeLeft;
	private boolean floatingDownwards;
	private Vector2 floatTargetPosition;
	private float flapTimeLeft;
	private boolean flapUp;
	public boolean dead;

	/**
	 * constructor
	 */
	public Bird()
	{
		init();
	}

	/**
	 * create a new bird for the player to murder
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f);
		origin.set(dimension.x /2, dimension.y / 2);

		regBirdUp = Assets.instance.bird.birdUp;
		regBirdDown = Assets.instance.bird.birdDown;
		regDeadBird = Assets.instance.bird.deadBird;

		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		collected = false;

		// set random initial random point of bobbing up and down
		floatingDownwards = false;
		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
		floatTargetPosition = null;

		// set random wing start position and random time until first flap
		int temp = (int)Math.round(Math.random());
		flapUp = temp > 0;
		flapTimeLeft = MathUtils.random(0, FLAP_SPEED);
		dead = false;
		removeIn = 0.5f;

		// particles
		bloodParticles.load(Gdx.files.internal("particles/blood.pfx"), Gdx.files.internal("particles"));
	}

	/**
	 * draw un-murdered birds
	 */
	public void render (SpriteBatch batch)
	{
		// draw particles
		bloodParticles.draw(batch);

		if (collected) return;

		TextureRegion reg = null;
		if (!dead)
		{
			if (flapUp)
				reg = regBirdUp;
			else if (!flapUp)
				reg = regBirdDown;
		} else
		{
			reg = regDeadBird;
		}
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

	/**
	 * caught a bird
	 * birds explode with blood when caught
	 * @return 1
	 */
	public int getScore()
	{
		bloodParticles.setPosition(position.x + dimension.x / 2,  position.y);
		bloodParticles.start();
		return 1;
	}

	/**
	 * update bird to allow bob, flap, and blood spray animations
	 */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);

		// blood spray
		bloodParticles.update(deltaTime);

//		// bobbing non box2d
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
//		position.lerp(floatTargetPosition, deltaTime);

		// bobbing box2d
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

		// flapping
		flapTimeLeft -= deltaTime;
		if (flapTimeLeft <= 0)
		{
			flapTimeLeft = FLAP_SPEED;
			flapUp = !flapUp;
		}
	}

	/**
	 * flag a bird as dead for rendering at end of level
	 */
	public void isDead()
	{
		dead = true;
	}
}