package com.lashleygdx.game.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lashleygdx.game.world.Assets;

/**
 * ground underlay
 * @author Chris Lashley
 */
public class Ground extends AbstractGameObject
{
	private TextureRegion regGround;
	private float length;

	public Ground (float length)
	{
		this.length = length;
		init();
	}

	public void init()
	{
		dimension.set (length * 10, 6);

		regGround = Assets.instance.levelDecoration.ground;

		origin.x = -dimension.x / 2;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		reg = regGround;
		batch.setColor(0, 0, 0, 1); // set black
		batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y,
				dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		batch.setColor(1, 1, 1, 1); // reset to white
	}
}
