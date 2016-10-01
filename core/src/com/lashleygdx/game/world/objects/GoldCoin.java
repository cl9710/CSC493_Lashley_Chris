package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.world.Assets;

/**
 * gold coins are added to score
 * @author Chris Lashley
 */
public class GoldCoin extends AbstractGameObject
{
	private TextureRegion regGoldCoin;

	public boolean collected;

	/**
	 * constructor
	 */
	public GoldCoin()
	{
		init();
	}

	/**
	 * create a new gold coin for the player to collect
	 */
	private void init()
	{
		dimension.set(0.5f, 0.5f);

		regGoldCoin = Assets.instance.goldCoin.goldCoin;

		// set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		collected = false;
	}

	/**
	 * draw uncollected gold coins
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;

		TextureRegion reg = null;
		reg = regGoldCoin;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}

	/**
	 * gold coins are worth 100 pts
	 * @return 100
	 */
	public int getScore()
	{
		return 100;
	}
}