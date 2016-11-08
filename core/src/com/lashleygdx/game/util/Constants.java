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

	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/murderkitty.pack.atlas";

	// Locations of animation texture atlas files
	public static final String DEAD = "images/dead.pack.atlas";
	public static final String FALL = "images/fall.pack.atlas";
	public static final String IDLE = "images/idle.pack.atlas";
	public static final String JUMP = "images/jump.pack.atlas";
	public static final String RUN = "images/run.pack.atlas";
	public static final String WALK = "images/walk.pack.atlas";

	// Location of image file for levels
	public static final String LEVEL_01 = "levels/level-01.png";
	public static final String LEVEL_02 = "levels/level-02.png";

	// Amount of extra lives at level start
	public static final int LIVES_START = 3;

	// Duration of frog powerup in seconds
	public static final float ITEM_FROG_POWERUP_DURATION = 9;

	// Duration of bloodlust powerup in seconds
	public static final float BLOODLUST_POWERUP_DURATION = 1.5f;

	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;

	// Delay after death
	public static final float TIME_DELAY_DEAD = 3;

	// Temporary damage immunity after hitting something
	public static final float DMG_IMMUNITY = 3;

	// Location of description file for ui texture atlas
	public static final String TEXTURE_ATLAS_UI = "images/murderkitty-ui.pack.atlas";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";

	// Location of description file for skins
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String SKIN_MURDERKITTY_UI = "images/murderkitty-ui.json";

	// preferences saved location
	public static final String PREFERENCES = "game.prefs";

	// spawn radius for carrots
	public static final float BIRDS_SPAWN_RADIUS = 2;

	// delay after game finished
	public static final float TIME_DELAY_LEVEL_FINISHED = 8;

	// delay removing objects until after particle effect
	public static final float TIME_DELAY_REMOVAL = 2;

	// number of levels
	public static final float NUM_LEVELS = 2;
}