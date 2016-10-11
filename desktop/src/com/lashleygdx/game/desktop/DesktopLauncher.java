package com.lashleygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lashleygdx.game.LashleyGdxGame;

/**
 * DesktopLauncher is the class that runs the game
 * @author Chris Lashley
 */
public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CanyonBunny";
		config.width = 800;
		config.height = 480;

		new LwjglApplication(new LashleyGdxGame(), config);
	}
}