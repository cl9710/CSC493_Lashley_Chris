package com.lashleygdx.game.util;

/**
 * Constants is a class containing easy access to cross class constants
 * @author Chris Lashley
 */
public class Constants
{
	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;

	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;

	// GUI width
	public static final float VIEWPORT_GUI_WIDTH = 800.0f;

	// GUI height
	public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

	// Location of description file for game objects texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack.atlas";

	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";

	// Amount of extra lives at level start
	public static final int LIVES_START = 3;

	// Duration of feather powerup in seconds
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;

	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;

	// Location of description file for ui texture atlas
	public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack.atlas";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";

	// Location of description file for skins
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";

	// preferences saved location
	public static final String PREFERENCES = "canyonbunny.prefs";

	// number of carrots to spawn
	public static final int CARROTS_SPAWN_MAX = 100;

	// spawn radius for carrots
	public static final float CARROTS_SPAWN_RADIUS = 3.5f;

	// delay after game finished
	public static final float TIME_DELAY_GAME_FINISHED = 6.0f;

	//shader
	public static final String shaderMonochromeVertex = "shaders/monochrome.vs";
	public static final String shaderMonochromeFragment = "shaders/monochrome.fs";

}