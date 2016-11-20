package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * animals explode in a spray of blood when murdered
 * @author Chris Lashley
 */
public class DeathExplosion extends AbstractGameObject
{
	public ParticleEffect bloodParticles = new ParticleEffect();

	/**
	 * constructor
	 */
	public DeathExplosion(Vector2 position)
	{
		init(position);
	}

	/**
	 * create a new death explosion
	 */
	private void init(Vector2 position)
	{
		dimension.set(0.5f, 0.5f);
		this.position.set(position);

		// particles
		bloodParticles.load(Gdx.files.internal("particles/blood.pfx"), Gdx.files.internal("particles"));
		bloodParticles.setPosition(position.x + dimension.x / 2,  position.y);
	}

	/**
	 * draw death explosion
	 */
	public void render (SpriteBatch batch)
	{
		// draw particles
		bloodParticles.draw(batch);
	}

	public void splat()
	{
		bloodParticles.start();
	}

	/**
	 * update blood spray animations
	 */
	@Override
	public void update (float deltaTime)
	{
		bloodParticles.update(deltaTime);
	}
}