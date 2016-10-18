package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.lashleygdx.game.world.Assets;
import com.badlogic.gdx.math.Vector2;

/**
 * trees
 * @author Chris Lashley
 */
public class Trees extends AbstractGameObject
{
	private TextureRegion regTree1;
	private TextureRegion regTree2;

	private int length;

	/**
	 * constructor
	 * @param length
	 */
	public Trees (int length)
	{
		this.length = length;
		init();
	}

	/**
	 * create a tree
	 */
	private void init()
	{
		dimension.set(2, 4);

		regTree1 = Assets.instance.levelDecoration.tree1;
		regTree2 = Assets.instance.levelDecoration.tree2;

		// shift trees and extend length
		origin.x = -dimension.x * 6;
		length += dimension.x * 2;
	}

	/**
	 * draw staggered rows of alternating tree types at specified location with specified tint and scroll speed
	 * @param batch
	 * @param offsetX
	 * @param offsetY
	 * @param tintColor
	 * @param stagger
	 * @param parallaxSpeedX
	 */
	private void drawTree (SpriteBatch batch, float offsetX, float offsetY, float tintColor, boolean stagger, float parallaxSpeedX)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;

		// trees span the whole level
		int treeLength = 0;
		treeLength += MathUtils.ceil (length / (2 * dimension.x) * (1 - parallaxSpeedX));
		treeLength += MathUtils.ceil (0.5f + offsetX);
		for (int i = 0; i < treeLength; i++)
		{
			reg = regTree1;
			// alternate trees to appear random
			if (stagger || (i % 3 == 0))
			{
				reg = regTree2;
			}
			batch.draw(reg.getTexture(), origin.x + xRel + position.x * parallaxSpeedX, position.y + origin.y + yRel, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;

			reg = regTree2;
			// alternate trees to appear random
			if (stagger || (i % 2 == 0))
			{
				reg = regTree1;
			}
			batch.draw(reg.getTexture(), origin.x + xRel + position.x * parallaxSpeedX, position.y + origin.y + yRel, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
		}
		// reset color to white
		batch.setColor(1, 1, 1, 1);
	}

	/**
	 * draw layers of trees in the background
	 * trees darken based on layer
	 * trees scroll with the player based on layer
	 * boolean parameter simulates a more random tree appearance
	 */
	@Override
	public void render(SpriteBatch batch)
	{
		// row 1
		drawTree(batch, 1.0f, 1.0f, 0.0f, false, 0.5f);
		// row 2
		drawTree(batch, 0.75f, 0.75f, 0.2f, true, 0.4f);
		// row 3
		drawTree(batch, 0.50f, 0.50f, 0.6f, true, 0.3f);
		// row 4
		drawTree(batch, 0.25f, 0.25f, 0.8f, false, 0.2f);
		// row 5
		drawTree(batch, 0.0f, 0.0f, 1.0f, false, 0.1f);
	}

	/**
	 * allow trees to scroll with player in the background
	 */
	public void updateScrollPosition (Vector2 camPosition)
	{
		position.set(camPosition.x, position.y);
	}
}