package com.lashleygdx.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

/**
 * builds the texture atlas
 * with/without debug outlines per boolean
 * @author Chris Lashley
 */
public class TextureAtlasBuilder
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
			TexturePacker.process(settings,  "assets-raw/images",  "../core/assets/images", "murderkitty.pack");
//			TexturePacker.process(settings, "assets-raw/images-ui", "../core/assets/images", "murderkitty-ui.pack");
	}
}