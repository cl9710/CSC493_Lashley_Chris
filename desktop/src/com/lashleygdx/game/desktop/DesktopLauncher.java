package com.lashleygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lashleygdx.game.LashleyGdxGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

/**
 * DesktopLauncher is the class that runs the game
 * also builds the texture atlas if needed via boolean variable
 * @author Chris Lashley
 */
public class DesktopLauncher
{
//	private static boolean rebuildAtlas = true;
//	private static boolean drawDebugOutline = true;
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	public static void main (String[] arg)
	{
		// conditional to rebuild the texture atlas if desired
		if (rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings,  "assets-raw/images",  "../core/assets/images", "canyonbunny.pack");
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CanyonBunny";
		config.width = 800;
		config.height = 480;

		new LwjglApplication(new LashleyGdxGame(), config);
	}
}