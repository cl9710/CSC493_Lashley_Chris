package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.lashleygdx.game.world.objects.AbstractGameObject;
import com.lashleygdx.game.world.objects.Bird;
import com.lashleygdx.game.world.objects.Cat;
import com.lashleygdx.game.world.objects.Dog;
import com.lashleygdx.game.world.objects.Frog;
import com.lashleygdx.game.world.objects.Ground;
import com.lashleygdx.game.world.objects.House;
import com.lashleygdx.game.world.objects.Trees;
import com.lashleygdx.game.world.objects.Rock;
import com.lashleygdx.game.world.objects.WaterOverlay;

/**
 * level data & builder
 * @author Chris Lashley
 */
public class Level
{
	public static final String TAG = Level.class.getName();

	public enum BLOCK_TYPE
	{
		EMPTY (0, 0, 0), // black
		ROCK (0, 255, 0), // green
		PLAYER_SPAWNPOINT (255, 255, 255), // white
		FROG (255, 0, 255), // purple
		BIRD (255, 255, 0), // yellow
		DOG (255, 0, 0),	// red
		HOUSE (0, 0, 255); // blue

		private int color;

		private BLOCK_TYPE (int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}

		public boolean sameColor (int color)
		{
			return this.color == color;
		}

		public int getColor()
		{
			return color;
		}
	}

	// objects
	public Array<Rock> rocks;
	public Cat cat;
	public Array<Dog> dogs;
	public Array<Bird> birds;
	public Array<Frog> frogs;
	public House house;

	// decoration
	public Trees trees;
	public WaterOverlay waterOverlay;
	public Ground ground;


	/**
	 * constructor
	 * @param filename
	 */
	public Level (String filename)
	{
		init (filename);
	}

	/**
	 * create the specified level by mapping pixel colors with objects
	 * @param filename
	 */
	private void init (String filename)
	{
		// objects
		rocks = new Array<Rock>();
		cat = null;
		birds = new Array<Bird>();
		frogs = new Array<Frog>();
		dogs = new Array<Dog>();
		//		thorns = new Array<Thorn>();
		house = null;

		// load image file that represents the level data
		Pixmap pixmap = new Pixmap (Gdx.files.internal(filename));
		// scan pixels from top left to bottom right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
		{
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX,  pixelY);

				// find matching color value to identify block type at (x, y) point and
				// create the corresponding game object if there is a match

				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					// do nothing
				}
				// rock
				else if (BLOCK_TYPE.ROCK.sameColor(currentPixel))
				{
					if (lastPixel != currentPixel)
					{
						obj = new Rock();
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight + offsetHeight);
						rocks.add((Rock)obj);
					}
					else
					{
						rocks.get(rocks.size - 1).increaseLength(1);
					}
				}
				// player spawn point
				else if	(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
					obj = new Cat();
					offsetHeight = -2.0f;
					obj.position.set(pixelX, baseHeight + offsetHeight);
					cat = (Cat)obj;
					offsetHeight = 0;
				}
				// frog
				else if (BLOCK_TYPE.FROG.sameColor(currentPixel))
				{
					obj = new Frog();
					offsetHeight = -2.1f;
					obj.position.set(pixelX, baseHeight + offsetHeight);
					frogs.add((Frog)obj);
				}
				// bird
				else if (BLOCK_TYPE.BIRD.sameColor(currentPixel))
				{
					obj = new Bird();
					offsetHeight = -2.0f;
					obj.position.set(pixelX, baseHeight + (obj.dimension.y / 2) + offsetHeight);
					birds.add((Bird)obj);
				}
				// house (goal)
				else if (BLOCK_TYPE.HOUSE.sameColor(currentPixel))
				{
					obj = new House();
					offsetHeight = -2.1f;
					obj.position.set(pixelX, baseHeight + offsetHeight);
					house = (House)obj;
				}
				// dog
				else if (BLOCK_TYPE.DOG.sameColor(currentPixel))
				{
					obj = new Dog();
					offsetHeight = -2.0f;
					obj.position.set(pixelX, baseHeight + offsetHeight);
					dogs.add((Dog)obj);
				}
				// unknown object/pixel color
				else
				{
					int r = 0xff & (currentPixel >>> 24);	// red color channel
					int g = 0xff & (currentPixel >>> 16);	// green color channel
					int b = 0xff & (currentPixel >>> 8);	// blue color channel
					int a = 0xff & currentPixel;	// alpha color channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g +
							"> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}

		// decoration
		trees = new Trees (pixmap.getWidth());
		trees.position.set(-1, -1);
		waterOverlay = new WaterOverlay (pixmap.getWidth());
		waterOverlay.position.set(0, -3.71f);
		ground = new Ground (pixmap.getWidth());
		ground.position.set(0, -1.25f);

		// free memory
		pixmap.dispose();
		Gdx.app.debug (TAG, "level '" + filename + "' loaded");
	}

	/**
	 * draw the level and objects
	 * @param batch
	 */
	public void render (SpriteBatch batch)
	{
		// draw background
		ground.render(batch);

		// draw trees
		trees.render(batch);

		// draw rocks
		for (Rock rock : rocks)
			rock.render(batch);

		// draw birds
		for (Bird bird : birds)
			bird.render(batch);

		// draw frogs
		for (Frog frog : frogs)
			frog.render(batch);

		//draw house
		house.render(batch);

		// draw player character (cat)
		cat.render(batch);

		// draw dogs
		for (Dog dog : dogs)
			dog.render(batch);

		// draw water overlay
		waterOverlay.render(batch);
	}

	/**
	 * update level object variables
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		cat.update(deltaTime);
		for (Rock rock : rocks)
			rock.update(deltaTime);
		for (Bird bird : birds)
			bird.update(deltaTime);
		for (Frog frog : frogs)
			frog.update(deltaTime);
	}
}