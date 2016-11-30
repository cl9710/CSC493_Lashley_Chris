package com.lashleygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.ObjectMap;
import com.lashleygdx.game.world.WorldController;
import com.lashleygdx.game.world.Assets;
import com.lashleygdx.game.world.objects.AbstractGameObject;
import com.lashleygdx.game.world.objects.Bird;
import com.lashleygdx.game.world.objects.Rock;
import com.lashleygdx.game.world.objects.Cat;
import com.lashleygdx.game.world.objects.DeathExplosion;
import com.lashleygdx.game.world.objects.Cat.JUMP_STATE;
import com.lashleygdx.game.world.objects.Dog;
import com.lashleygdx.game.world.objects.Frog;

/**
 * handle collisions for box2d
 * @author Chris Lashley
 */
public class CollisionHandler implements ContactListener
{
	private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners;

	private WorldController world;

	public CollisionHandler(WorldController w)
	{
		world = w;
		listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
	}

	public void addListener(short categoryA, short categoryB, ContactListener listener)
	{
		addListenerInternal(categoryA, categoryB, listener);
		addListenerInternal(categoryB, categoryA, listener);
	}

	@Override
	public void beginContact(Contact contact)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		AbstractGameObject objA = (AbstractGameObject)fixtureA.getBody().getUserData();
		AbstractGameObject objB = (AbstractGameObject)fixtureB.getBody().getUserData();

		if (objA instanceof Cat)
		{
			if (fixtureA.isSensor() && objB instanceof Rock)
			{
				world.player.jumpState = JUMP_STATE.GROUNDED;
				world.player.dustParticles.start();
				Gdx.app.log("grounded", "start");
			}
		}
		else if (objB instanceof Cat)
		{
			if (fixtureB.isSensor() && objA instanceof Rock)
			{
				world.player.jumpState = JUMP_STATE.GROUNDED;
				world.player.dustParticles.start();
				Gdx.app.log("grounded", "start");
			}
		}

		processContact(contact);

		ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
		if (listener != null)
		{
			listener.beginContact(contact);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
		if (listener != null)
		{
			listener.preSolve(contact, oldManifold);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
		if (listener != null)
		{
			listener.postSolve(contact, impulse);
		}
	}

	@Override
	public void endContact(Contact contact)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		AbstractGameObject objA = (AbstractGameObject)fixtureA.getBody().getUserData();
		AbstractGameObject objB = (AbstractGameObject)fixtureB.getBody().getUserData();

		if (objA instanceof Cat)
		{
			if (fixtureA.isSensor() && objB instanceof Rock)
			{
				world.player.jumpState = JUMP_STATE.FALLING;
				world.player.dustParticles.allowCompletion();
//				Gdx.app.log("grounded", "end");
			}
		}
		else if (objB instanceof Cat)
		{
			if (fixtureB.isSensor() && objA instanceof Rock)
			{
				world.player.jumpState = JUMP_STATE.FALLING;
				world.player.dustParticles.allowCompletion();
//				Gdx.app.log("grounded", "end");
			}
		}

		ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
		if (listener != null)
		{
			listener.endContact(contact);
		}
	}

	private void addListenerInternal(short categoryA, short categoryB, ContactListener listener)
	{
		ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
		if (listenerCollection == null)
		{
			listenerCollection = new ObjectMap<Short, ContactListener>();
			listeners.put(categoryA, listenerCollection);
		}
		listenerCollection.put(categoryB, listener);
	}

	private ContactListener getListener(short categoryA, short categoryB)
	{
		ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
		if (listenerCollection == null)
		{
			return null;
		}
		return listenerCollection.get(categoryB);
	}

	private void processContact(Contact contact)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		AbstractGameObject objA = (AbstractGameObject)fixtureA.getBody().getUserData();
		AbstractGameObject objB = (AbstractGameObject)fixtureB.getBody().getUserData();

		if (objA instanceof Cat)
		{
			processPlayerContact(fixtureA, fixtureB);
		}
		else if (objB instanceof Cat)
		{
			processPlayerContact(fixtureB, fixtureA);
		}
	}

	private void processPlayerContact(Fixture playerFixture, Fixture objFixture)
	{
		if (objFixture.getBody().getUserData() instanceof Bird)
		{
			// Remove the bird, grant player bloodlust, update the player's bird score by 1.
			AudioManager.instance.play(Assets.instance.sounds.deathExplosion);
			Bird bird = (Bird)objFixture.getBody().getUserData();
			if (!world.isVictory())
			{
				bird.collected = true;
				//			AudioManager.instance.play(Assets.instance.sounds.wingsFlapping);		// not sure i like
				//			AudioManager.instance.play(Assets.instance.sounds.squawk);				// not sure i like
				AudioManager.instance.play(Assets.instance.sounds.deathExplosion);
				world.birdScore += bird.getScore();
				DeathExplosion blood = new DeathExplosion(bird.position);
				world.level.deathExplosions.add(blood);
				blood.splat();
				if (!world.player.hasBloodlust)
				{
					AudioManager.instance.play(Assets.instance.music.bloodlustSong);
				}
				world.player.setBloodlust(true);
				world.flagForRemoval(bird);
				Gdx.app.log(world.TAG,  "Bird murdered");
			}
		}
		else if (objFixture.getBody().getUserData() instanceof Dog)
		{
			// Remove the bird update the player's score by 1.
			AudioManager.instance.play(Assets.instance.sounds.deathExplosion);
			if (!world.isGameOver())
				if (!world.player.dead)
				{
					world.player.freeze();
					AudioManager.instance.stopMusic();
					AudioManager.instance.play(Assets.instance.sounds.catYell);
					AudioManager.instance.play(Assets.instance.sounds.dogAttack);
					world.playerDied();
					Gdx.app.log(world.TAG,  "Stay away from dogs");
				}    	}
		else if (objFixture.getBody().getUserData() instanceof Frog)
		{
			// Remove the frog, grant the player frog jump, update the player's frog score by 1.
			Frog frog = (Frog)objFixture.getBody().getUserData();
			frog.collected = true;
			AudioManager.instance.play(Assets.instance.sounds.frog);
			world.frogScore += frog.getScore();
			world.player.setFrogPowerup(true);
			world.flagForRemoval(frog);
			Gdx.app.log(world.TAG, "Frog murdered");
		}
	}
}