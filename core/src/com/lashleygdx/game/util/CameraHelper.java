package com.lashleygdx.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Camera Helper sets aspects of the game camera
 * @author Chris Lashley
 */
public class CameraHelper
{
	private static final String TAG = CameraHelper.class.getName();

	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;

	private Vector2 position;
	private float zoom;
	private Sprite target;

	public CameraHelper ()
	{
		position = new Vector2();
		zoom = 1.0f;
	}

	// update camera
	public void update (float deltaTime)
	{
		if (!hasTarget()) return;

		position.x = target.getX() + target.getOriginX();
		position.y = target.getY() + target.getOriginY();
	}

	// set camera position
	public void setPosition (float x, float y)
	{
		this.position.set(x, y);
	}

	// get camera position
	public Vector2 getPosition()
	{
		return position;
	}

	// zoom in/out
	public void addZoom (float amount)
	{
		setZoom(zoom + amount);
	}

	// set specific zoom level
	public void setZoom(float zoom)
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	// get zoom
	public float getZoom()
	{
		return zoom;
	}

	// set camera target
	public void setTarget (Sprite target)
	{
		this.target = target;
	}

	// get camera target
	public Sprite getTarget()
	{
		return target;
	}

	public boolean hasTarget()
	{
		return target != null;
	}

		public boolean hasTarget (Sprite target)
	{
		return hasTarget() && this.target.equals(target);
	}

	// refresh camera position & zoom
	public void applyTo (OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
}