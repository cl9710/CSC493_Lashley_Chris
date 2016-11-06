package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.lashleygdx.game.world.Level.BLOCK_TYPE;
import com.lashleygdx.game.world.objects.AbstractGameObject;
import com.lashleygdx.game.world.objects.BunnyHead;
import com.lashleygdx.game.world.objects.Clouds;
import com.lashleygdx.game.world.objects.Feather;
import com.lashleygdx.game.world.objects.GoldCoin;
import com.lashleygdx.game.world.objects.Mountains;
import com.lashleygdx.game.world.objects.Rock;
import com.lashleygdx.game.world.objects.WaterOverlay;
import com.lashleygdx.game.world.objects.Carrot;
import com.lashleygdx.game.world.objects.Goal;

/**
 * level data
 * @author Chris Lashley
 */
public class Level
{
	public static final String TAG = Level.class.getName();

	public enum BLOCK_TYPE
	{
		EMPTY (0, 0, 0), // black
		GOAL (255, 0, 0),	// red
		ROCK (0, 255, 0), // green
		PLAYER_SPAWNPOINT (255, 255, 255), // white
		ITEM_FEATHER (255, 0, 255), // purple
		ITEM_GOLD_COIN (255, 255, 0); // yellow

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
	public Array<Carrot> carrots;
	public Goal goal;

	// decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;

	// player
	public BunnyHead bunnyHead;

	// items
	public Array<GoldCoin> goldCoins;
	public Array<Feather> feathers;

	/**
	 * constructor
	 * @param filename
	 */
	public Level (String filename)
	{
		init (filename);
	}

	/**
	 * build the specified level and populate with objects
	 * @param filename
	 */
	private void init (String filename)
	{
		// player character
		bunnyHead = null;
		// objects
		rocks = new Array<Rock>();
		goldCoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		carrots = new Array<Carrot>();

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
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
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
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					bunnyHead = (BunnyHead)obj;
				}
				// feather
				else if (BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel))
				{
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					feathers.add((Feather)obj);
				}
				// gold coin
				else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel))
				{
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					goldCoins.add((GoldCoin)obj);
				}
				// goal
				else if (BLOCK_TYPE.GOAL.sameColor(currentPixel))
				{
					obj = new Goal();
					offsetHeight = -7.0f;
					obj.position.set(pixelX, baseHeight + offsetHeight);
					goal = (Goal)obj;
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
		clouds = new Clouds (pixmap.getWidth());
		clouds.position.set (0, 2);
		mountains = new Mountains (pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay (pixmap.getWidth());
		waterOverlay.position.set(0, -3.71f);

		// free memory
		pixmap.dispose();
		Gdx.app.debug (TAG, "level '" + filename + "' loaded");
	}

	/**
	 * draw the level and populate it with objects
	 * @param batch
	 */
	public void render (SpriteBatch batch)
	{
		// draw mountains
		mountains.render(batch);

		// draw goal
		goal.render(batch);

		// draw rocks
		for (Rock rock : rocks)
			rock.render(batch);

		// draw gold coins
		for (GoldCoin goldCoin : goldCoins)
			goldCoin.render(batch);

		// draw feathers
		for (Feather feather : feathers)
			feather.render(batch);

		// draw carrots
		for (Carrot carrot : carrots)
			carrot.render(batch);

		// draw player characters (bunny head)
		bunnyHead.render(batch);

		// draw water overlay
		waterOverlay.render(batch);

		// draw clouds
		clouds.render(batch);
	}

	/**
	 * update level object variables
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		bunnyHead.update(deltaTime);
		for (Rock rock : rocks)
			rock.update(deltaTime);
		for (GoldCoin goldCoin : goldCoins)
			goldCoin.update(deltaTime);
		for (Feather feather : feathers)
			feather.update(deltaTime);
		for (Carrot carrot : carrots)
			carrot.update(deltaTime);
		clouds.update(deltaTime);
	}
}