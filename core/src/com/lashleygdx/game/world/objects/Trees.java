package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.lashleygdx.game.world.Assets;

/**
 * trees
 * @author Chris Lashley
 */
public class Trees extends AbstractGameObject
{
	private TextureRegion regTree1;
	private TextureRegion regTree2;

	private int length;

	public Trees (int length)
	{
		this.length = length;
		init();
	}

	private void init()
	{
		dimension.set(2, 4);

		regTree1 = Assets.instance.levelDecoration.tree1;
		regTree2 = Assets.instance.levelDecoration.tree2;

		// shift trees and extend length
		origin.x = -dimension.x * 6;
		length += dimension.x * 2;
	}

	private void drawTree (SpriteBatch batch, float offsetX, float offsetY, float tintColor, boolean stagger)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;

		// trees span the whole level
		int treeLength = 0;
		treeLength += MathUtils.ceil (length / (2 * dimension.x));
		treeLength += MathUtils.ceil (0.5f + offsetX);
		for (int i = 0; i < treeLength; i++)
		{
			reg = regTree1;
			// alternate trees every other row
			if (stagger)
			{
				reg = regTree2;
			}
			batch.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;

			reg = regTree2;
			// alternate trees every other row
			if (stagger)
			{
				reg = regTree1;
			}
			batch.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel, origin.x, origin.y,
					dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
		}
		// reset color to white
		batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void render(SpriteBatch batch)
	{
		// row 1
		drawTree(batch, 1.0f, 1.0f, 0.0f, false);
		// row 2
		drawTree(batch, 0.75f, 0.75f, 0.25f, true);
		// row 3
		drawTree(batch, 0.50f, 0.50f, 0.50f, false);
		// row 4
		drawTree(batch, 0.25f, 0.25f, 0.75f, true);
		// row 5
		drawTree(batch, 0.0f, 0.0f, 1.0f, false);
	}
}