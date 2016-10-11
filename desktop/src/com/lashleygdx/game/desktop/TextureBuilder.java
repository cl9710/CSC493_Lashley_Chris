package com.lashleygdx.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

/**
 * builds the texture atlas with or without debug outlines as needed
 * @author Chris Lashley
 */
public class TextureBuilder
{
//	private static boolean drawDebugOutline = true;
	private static boolean drawDebugOutline = false;

	public static void main (String[] arg)
	{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings,  "assets-raw/images",  "../core/assets/images", "canyonbunny.pack");
			TexturePacker.process(settings, "assets-raw/images-ui", "../core/assets/images", "canyonbunny-ui.pack");
	}
}