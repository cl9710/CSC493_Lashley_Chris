package com.lashleygdx.game.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.InputAdapter;
import com.lashleygdx.game.util.CameraHelper;
import com.lashleygdx.game.util.CollisionHandler;
import com.lashleygdx.game.util.Constants;
import com.lashleygdx.game.world.objects.Cat;
import com.lashleygdx.game.world.objects.Frog;
import com.lashleygdx.game.world.objects.House;
import com.lashleygdx.game.world.objects.AbstractGameObject;
import com.lashleygdx.game.world.objects.Bird;
import com.lashleygdx.game.world.objects.Rock;
import com.lashleygdx.game.world.objects.Cat.JUMP_STATE;
import com.lashleygdx.game.world.objects.Dog;
import com.badlogic.gdx.Game;
import com.lashleygdx.game.screens.MenuScreen;
import com.lashleygdx.game.util.AudioManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * World Controller controls all the objects/assets in the game
 * @author Chris Lashley
 */
public class WorldController extends InputAdapter implements Disposable
{
	public static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Level level;
	public int lives;
	public int birdScore;
	public int frogScore;
	private Game game;
	public float livesVisual;
	public int levelNum;
	public Cat player;

	// rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	private float timeLeftGameOverDelay;
	private float timeLeftDead;

	public World b2world;
	public Array<AbstractGameObject> objectsToRemove;

	/**
	 * initialize a level
	 */
	private void initLevel()
	{
		birdScore = 0;
		frogScore = 0;
		level = new Level(getNextLevel(levelNum));
		AudioManager.instance.play(Assets.instance.music.normalSong);
		player = level.cat;
		cameraHelper.setTarget(player);
		objectsToRemove = new Array<AbstractGameObject>();
		initPhysics();
	}

	/**
	 * constructor
	 */
	public WorldController (Game game)
	{
		this.game = game;
		init();
	}

	/**
	 *  create an instance of itself
	 */
	private void init ()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		timeLeftDead = 0;
		levelNum = 0;
		initLevel();
	}

	/**
	 * tells the game which level to load
	 * @param currentLevel is the level you want to load
	 * @return level to load
	 */
	public String getNextLevel(int currentLevel)
	{
		for (int i = currentLevel; i < Constants.NUM_LEVELS; i++)
		{
			if (i == 0)
				return Constants.LEVEL_01;
			if (i == 1)
				return Constants.LEVEL_02;
		}
		backToMenu();
		return null;
	}

	/**
	 * update the game variables (multiple levels)
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		if (objectsToRemove.size > 0)
			removeObjects(deltaTime);
		if (!isDead() || isGameOver())
		{
			handleDebugInput(deltaTime);
			if (isVictory())
			{
				timeLeftGameOverDelay -= deltaTime;
				if (timeLeftGameOverDelay < 0)
				{
					AudioManager.instance.stopMusic();
					if (levelNum < Constants.NUM_LEVELS)
						initLevel();
					else
					{
						backToMenu();
					}
				}
			}
			else if (isGameOver())
			{
				timeLeftGameOverDelay -= deltaTime;
				if (timeLeftGameOverDelay < 0)
					backToMenu();
			} else
			{
				handleInputGame(deltaTime);
			}
			level.update(deltaTime);
			testCollisions();
			b2world.step(deltaTime,  8,  3);
			cameraHelper.update(deltaTime);
			if (isPlayerInWater() && !isGameOver())
			{
				if (!isDead())
				{
					AudioManager.instance.stopMusic();
					AudioManager.instance.play(Assets.instance.sounds.splash);
					playerDied();
					Gdx.app.log(TAG,  "Stay out of water");
				}
				else
					return;
			}
		} else
		{
			if (isGameOver())
			{
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			} else
			{
				timeLeftDead -= deltaTime;
				if (timeLeftDead < 0)
				{
					lives--;
					if (isGameOver())
					{
						timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
					} else
					{
						initLevel();
					}
				}
			}
		}
		level.trees.updateScrollPosition(cameraHelper.getPosition());
		if (livesVisual > lives)
		{
			livesVisual = Math.max(lives,  livesVisual - 1 * deltaTime);
		}
	}

	/**
	 * set debug keyboard controls
	 * @param deltaTime
	 */
	private void handleDebugInput (float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return;

		if (!cameraHelper.hasTarget(player))
		{
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
		}

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}

	/**
	 * change camera position
	 * @param x
	 * @param y
	 */
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	/**
	 * game world keyboard controls
	 */
	@Override
	public boolean keyUp (int keycode)
	{
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG,  "Game world resetted");
		}
		// toggle camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null: player);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// back to menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
		}
		return false;
	}

	/**
	 * handle cat / rock collisions
	 * player can not pass through rocks normally
	 * if they have a frog jump powerup you can jump/fly through the bottom
	 * @param rock
	 */
	private void onCollisionCatWithRock (Rock rock)
	{
		float heightDifference = Math.abs(player.position.y - (rock.position.y + rock.bounds.height));
		boolean below = (player.position.y + player.bounds.height) < (rock.position.y + 0.1f);

		if (below)
		{
			player.jumpState = JUMP_STATE.JUMP_FALLING;
			player.velocity.y = MathUtils.clamp(player.velocity.y, -player.terminalVelocity.y, 0.0f);
			return;
		}
		if (heightDifference >= 0.1f)
		{
			boolean hitRightEdge = player.position.x > (rock.position.x + rock.bounds.width - 0.11f);
			boolean hitLeftEdge = player.position.x  < (rock.position.x);

			if (hitRightEdge)
			{
				player.position.x = rock.position.x + rock.bounds.width;
			} else if (hitLeftEdge)
			{
				player.position.x = rock.position.x - player.bounds.width;
			}
			return;
		}

		switch (player.jumpState)
		{
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			player.position.y = rock.position.y + player.bounds.height;
			player.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			player.position.y = rock.position.y + player.bounds.height;
			break;
		}
	}

	/**
	 * murder birds and grants 1 stack of bloodlust
	 * @param bird
	 */
	private void onCollisionCatWithBird (Bird bird)
	{
		if (!isVictory())
		{
			bird.collected = true;
			//			AudioManager.instance.play(Assets.instance.sounds.wingsFlapping);		// not sure i like
			//			AudioManager.instance.play(Assets.instance.sounds.squawk);				// not sure i like
			AudioManager.instance.play(Assets.instance.sounds.deathExplosion);
			birdScore += bird.getScore();
			if (!player.hasBloodlust)
			{
				AudioManager.instance.play(Assets.instance.music.bloodlustSong);
			}
			player.setBloodlust(true);
			flagForRemoval(bird);
			Gdx.app.log(TAG,  "Bird murdered");
		}
	}

	/**
	 * murder frogs and grants the frog jump powerup
	 * @param frog
	 */
	private void onCollisionCatWithFrog (Frog frog)
	{
		frog.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.frog);
		frogScore += frog.getScore();
		player.setFrogPowerup(true);
		flagForRemoval(frog);
		Gdx.app.log(TAG, "Frog murdered");
	}

	/**
	 * reach the goal
	 * @param house
	 */
	private void onCollisionCatWithHouse (House house)
	{
		if (!isVictory())
		{
			if (!house.reached)
				Gdx.app.log(TAG, "You made it home");
			AudioManager.instance.stopMusic();
			AudioManager.instance.play(Assets.instance.sounds.victory);
			house.reached = true;
			timeLeftGameOverDelay = Constants.TIME_DELAY_LEVEL_FINISHED;

			if (player.jumpState != JUMP_STATE.GROUNDED)
				player.jumpState = JUMP_STATE.JUMP_FALLING;
			player.freeze();
			levelNum++;

			Vector2 playerCenter = new Vector2(player.position);
			playerCenter.x += player.bounds.width;
			spawnDeadBirds(playerCenter, birdScore, Constants.BIRDS_SPAWN_RADIUS);
		}
	}

	/**
	 * check for cat being attacked by a dog
	 * @param dog
	 */
	private void onCollisionCatWithDog (Dog dog)
	{
		if (!isGameOver())
			if (!player.dead)
			{
				AudioManager.instance.stopMusic();
				AudioManager.instance.play(Assets.instance.sounds.catYell);
				AudioManager.instance.play(Assets.instance.sounds.dogAttack);
				playerDied();
				Gdx.app.log(TAG,  "Stay away from dogs");
			}
	}

	/**
	 * check for player collision with game objects and world
	 */
	private void testCollisions()
	{
		r1.set(player.position.x, player.position.y, player.bounds.width,
				player.bounds.height);

		// test collision: player <--> rocks
		for (Rock rock : level.rocks)
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionCatWithRock(rock);
			// IMPORTANT: must do all collisions for valid edge testing on rocks
		}
		// test collision: player <--> bird
		for (Bird bird : level.birds)
		{
			if (bird.collected) continue;
			r2.set(bird.position.x, bird.position.y, bird.bounds.width, bird.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionCatWithBird(bird);
			break;
		}
		// test collision: player <--> frog
		for (Frog frog : level.frogs)
		{
			if (frog.collected) continue;
			r2.set(frog.position.x, frog.position.y, frog.bounds.width, frog.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionCatWithFrog(frog);
			break;
		}
		// test collision: player <--> dog
		for (Dog dog : level.dogs)
		{
			r2.set(dog.position.x + dog.origin.x, dog.position.y, dog.bounds.width, dog.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionCatWithDog(dog);
			break;
		}
		// test collision: cat <--> house
		r2.set(level.house.position.x, level.house.position.y, level.house.bounds.width, level.house.bounds.height);
		if (r1.overlaps(r2))
			onCollisionCatWithHouse(level.house);
	}

	/**
	 * player movement / controls
	 * @param deltaTime
	 */
	private void handleInputGame(float deltaTime)
	{
		if ((cameraHelper.hasTarget(player)) && (!isVictory()))	// player movement
		{
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				player.velocity.x = -player.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				player.velocity.x = player.terminalVelocity.x;
			} else	// execute auto forward movement on non-desktop platform
			{
				if (Gdx.app.getType() != ApplicationType.Desktop)
				{
					player.velocity.x = player.terminalVelocity.x;
				}
			}

			// jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
			{
				player.setJumping(true);
			} else
			{
				player.setJumping(false);
			}
		}
	}

	/**
	 * check for game over
	 * @return true if player loses a life with no lives remaining
	 */
	public boolean isGameOver()
	{
		return lives < 0;
	}

	/**
	 * check if player fell in water
	 * @return true if player fell in water
	 */
	public boolean isPlayerInWater()
	{
		return player.position.y < -5;
	}

	/**
	 * check for victory
	 */
	public boolean isVictory()
	{
		return level.house.reached;
	}

	/**
	 * check for death
	 */
	public boolean isDead()
	{
		return player.dead;
	}

	/**
	 * switch to menu screen
	 */
	private void backToMenu()
	{
		game.setScreen(new MenuScreen(game));
	}

	/**
	 * make it the player drop all collected birds when the goal is reached
	 * @param pos
	 * @param numBirds
	 * @param radius
	 */
	private void spawnDeadBirds (Vector2 pos, int numBirds, float radius)
	{
		float birdShapeScale = 0.5f;

		// create birds with box2d body and fixture
		for (int i = 0; i < numBirds; i++)
		{
			Bird deadBird = new Bird();
			deadBird.isDead();
			// calculate random spawn rotation, and scale
			float rotation = MathUtils.random(0.0f, 360.0f) * MathUtils.degreesToRadians;
			float birdScale = MathUtils.random(0.5f, 1.0f);
			deadBird.scale.set(birdScale, birdScale);
			// create box2d body for deadBirds with start position and angle of rotation
			BodyDef bodyDef = new BodyDef();
			// spawn them from the player
			bodyDef.position.set(pos);
			//			bodyDef.position.add(x, y);
			bodyDef.angle = rotation;
			Body body = b2world.createBody(bodyDef);
			body.setType(BodyType.DynamicBody);
			deadBird.body = body;
			// create rectangular shape for deadBirds to allow interactions (collisions) with other objects
			PolygonShape polygonShape = new PolygonShape();
			float halfWidth = deadBird.bounds.width / 2.0f * birdScale;
			float halfHeight = deadBird.bounds.height / 2.0f * birdScale;
			polygonShape.setAsBox(halfWidth * birdShapeScale, halfHeight * birdShapeScale);
			// set physical attributes
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 50;
			fixtureDef.restitution = 0.5f;
			fixtureDef.friction = 0.5f;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
			// finally, add new deadBird to list for updating/rendering
			level.deadBirds.add(deadBird);
		}
	}

//	/**
//	 * make it the player drop all collected birds when the goal is reached
//	 * @param pos
//	 * @param numBirds
//	 * @param radius
//	 */
//	private void spawnDeadBirds (Vector2 pos, int numBirds, float radius)
//	{
//		float birdShapeScale = 0.5f;
//
//		// create birds with box2d body and fixture
//		for (int i = 0; i < numBirds; i++)
//		{
//			Bird deadBird = new Bird();
//			deadBird.isDead();
//			deadBird.position.set(pos);
//			float x = MathUtils.random(-radius, radius);
//			float y = MathUtils.random(5.0f, 15.0f);
//			deadBird.position.add(x, y);
//			// calculate random spawn rotation, and scale
//			float rotation = MathUtils.random(0.0f, 360.0f) * MathUtils.degreesToRadians;
//			float birdScale = MathUtils.random(0.5f, 1.0f);
//			deadBird.scale.set(birdScale, birdScale);
//			// create box2d body for deadBirds with start position and angle of rotation
//			createBody(deadBird, BodyType.DynamicBody, rotation);
//			// finally, add new deadBird to list for updating/rendering
//			level.deadBirds.add(deadBird);
//		}
//	}

	/**
	 * create a box2d body (for most objects)
	 */
	private Body createBody(AbstractGameObject obj, BodyType type, float rotation)
	{
		Vector2 origin = new Vector2();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(obj.position);
		Body body = b2world.createBody(bodyDef);
		body.setUserData(obj);	// collision handler
		obj.body = body;
		PolygonShape polygonShape = new PolygonShape();
		origin.x = obj.bounds.width / 2.0f;
		origin.y = obj.bounds.height / 2.0f;
		polygonShape.setAsBox(obj.bounds.width / 2.0f, obj.bounds.height / 2.0f, origin, rotation);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
//		fixtureDef.density = 50;
//		fixtureDef.restitution = 0.5f;
//		fixtureDef.friction = 0.5f;
		body.createFixture(fixtureDef);
		polygonShape.dispose();
		return body;
	}

	/**
	 * initialize box2d physics
	 */
	private void initPhysics ()
	{
		if (b2world != null) b2world.dispose();

		b2world = new World(new Vector2(0, -9.81f), true);
		//		b2world.setContactListener(new CollisionHandler(this));	// box2d
		// rocks
		for (Rock rock : level.rocks)
		{
			createBody(rock, BodyType.KinematicBody, 0);
		}
		// birds
		for (Bird bird : level.birds)
		{
			createBody(bird, BodyType.KinematicBody, 0);
		}
		// frogs
		for (Frog frog : level.frogs)
		{
			createBody(frog, BodyType.DynamicBody, 0);
		}
		// dogs are different because they have a different bounding box than dimensions
		Vector2 origin = new Vector2();
		for (Dog dog : level.dogs)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(dog.position);
			Body body = b2world.createBody(bodyDef);
			body.setUserData(dog);	// collision handler
			dog.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = dog.dimension.x / 2.0f;
			origin.y = dog.dimension.x / 2.0f;
			polygonShape.setAsBox(dog.dimension.x / 2.0f, dog.dimension.y / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
	}

	/**
	 * flag a box2d object for removal
	 * @param obj
	 */
	public void flagForRemoval(AbstractGameObject obj)
	{
		objectsToRemove.add(obj);
	}

	/**
	 * remove flagged box2d objects when done with them
	 * @param deltaTime
	 */
	public void removeObjects(float deltaTime)
	{
		{
			for (AbstractGameObject obj : objectsToRemove)
			{
				if (obj instanceof Bird)
				{
					if (obj.removeIn > 0)
					{
						obj.removeIn -= deltaTime;
					} else
					{
						int index = level.birds.indexOf((Bird) obj, true);
						if (index != -1)
						{
							level.birds.removeIndex(index);
							b2world.destroyBody(obj.body);
						}
					}
				}
				if (obj instanceof Frog)
				{
					if (obj.removeIn > 0)
					{
						obj.removeIn -= deltaTime;
					} else
					{
						int index = level.frogs.indexOf((Frog) obj, true);
						if (index != -1)
						{
							level.frogs.removeIndex(index);
							b2world.destroyBody(obj.body);
						}
					}
				}
			}
		}
	}

	/**
	 * free memory of unused objects/assets
	 */
	@Override
	public void dispose ()
	{
		if (b2world != null) b2world.dispose();
	}

	/**
	 * player died
	 */
	public void playerDied()
	{
		player.died();
		timeLeftDead = Constants.TIME_DELAY_DEAD;
	}
}