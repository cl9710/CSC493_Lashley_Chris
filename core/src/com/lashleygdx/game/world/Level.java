package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.lashleygdx.game.world.objects.AbstractGameObject;
import com.lashleygdx.game.world.objects.Ground;
import com.lashleygdx.game.world.objects.Trees;
import com.lashleygdx.game.world.objects.Rock;
import com.lashleygdx.game.world.objects.WaterOverlay;

/**
 * level data
 * @author Chris
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
		THORN (255, 0, 0), // red
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

	// decoration
	public Trees trees;
	public WaterOverlay waterOverlay;
	public Ground ground;

	public Level (String filename)
	{
		init (filename);
	}

	private void init (String filename)
	{
		// objects
		rocks = new Array<Rock>();

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

				}
				// frog
				else if (BLOCK_TYPE.FROG.sameColor(currentPixel))
				{

				}
				// bird
				else if (BLOCK_TYPE.BIRD.sameColor(currentPixel))
				{

				}
				// house (goal)
				else if (BLOCK_TYPE.HOUSE.sameColor(currentPixel))
				{

				}
				// thorn
				else if (BLOCK_TYPE.THORN.sameColor(currentPixel))
				{

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

	public void render (SpriteBatch batch)
	{
		// draw background
		ground.render(batch);

		// draw trees
		trees.render(batch);

		// draw rocks
		for (Rock rock : rocks)
			rock.render(batch);

		// draw water overlay
		waterOverlay.render(batch);
	}
}