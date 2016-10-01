package com.lashleygdx.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lashleygdx.game.world.objects.AbstractGameObject;

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
	private AbstractGameObject target;

	/**
	 * constructor
	 */
	public CameraHelper ()
	{
		position = new Vector2();
		zoom = 1.0f;
	}

	/**
	 * update camera position
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		if (!hasTarget()) return;

		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;

		// prevent camera from moving down too far
		position.y = Math.max(-1f, position.y);
	}

	/**
	 * set camera position
	 * @param x
	 * @param y
	 */
	public void setPosition (float x, float y)
	{
		this.position.set(x, y);
	}

	/**
	 * get camera position
	 * @return position
	 */
	public Vector2 getPosition()
	{
		return position;
	}

	/**
	 * zoom in/out
	 * @param amount
	 */
	public void addZoom (float amount)
	{
		setZoom(zoom + amount);
	}

	/**
	 * set specific zoom level
	 * @param zoom
	 */
	public void setZoom(float zoom)
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	/**
	 * get current zoom level
	 * @return zoom
	 */
	public float getZoom()
	{
		return zoom;
	}

	/**
	 * set a camera target to follow
	 * @param target
	 */
	public void setTarget (AbstractGameObject target)
	{
		this.target = target;
	}

	/**
	 * get camera target
	 * @return target
	 */
	public AbstractGameObject getTarget()
	{
		return target;
	}

	/**
	 * check for camera target
	 * @return true if it has a target
	 */
	public boolean hasTarget()
	{
		return target != null;
	}

	/**
	 * check for a specific camera target
	 * @param target
	 * @return true if parameter is current target
	 */
	public boolean hasTarget (AbstractGameObject target)
	{
		return hasTarget() && this.target.equals(target);
	}

	/**
	 * refresh camera and zoom levels
	 * @param camera
	 */
	public void applyTo (OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
}