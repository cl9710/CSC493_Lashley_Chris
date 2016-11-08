package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.lashleygdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;;

/**
 * Assets manages game assets
 * @author Chris Lashley
 */
public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	/**
	 *  singleton to prevent instantiation from other classes
	 */
	private Assets()
	{

	}

	public AssetCat cat;
	public AssetBird bird;
	public AssetFrog frog;
	public AssetRock rock;
	public AssetHouse house;
	public AssetDog dog;
	public AssetLevelDecoration levelDecoration;

	public AssetFonts fonts;
	public AssetSounds sounds;
	public AssetMusic music;

	/**
	 * fonts used in the game
	 * @author Chris Lashley
	 */
	public class AssetFonts
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		/**
		 * constructor
		 */
		public AssetFonts()
		{
			// create three fonts using Libgdx 15px bitmap font
			defaultSmall = new BitmapFont (Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont (Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont (Gdx.files.internal("images/arial-15.fnt"), true);
			//set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	/**
	 * Sounds
	 * @author Chris Lashley
	 */
	public class AssetSounds
	{
		public final Sound jump;
		public final Sound jumpWithFrog;
		public final Sound wingsFlapping;		// not used atm
		public final Sound deathExplosion;
		public final Sound frog;
		public final Sound catYell;
		public final Sound dogAttack;
		public final Sound splash;
		public final Sound squawk;				// not used atm
		public final Sound victory;

		public AssetSounds (AssetManager am)
		{
			jump = am.get("sounds/jump.wav", Sound.class);
			jumpWithFrog = am.get("sounds/jump_with_frog.wav", Sound.class);
			wingsFlapping = am.get("sounds/wings_flapping.wav", Sound.class);
			deathExplosion = am.get("sounds/death_explosion.wav", Sound.class);
			frog = am.get("sounds/frog.wav", Sound.class);
			catYell = am.get("sounds/cat_yell.wav", Sound.class);
			dogAttack = am.get("sounds/dog_attack.wav", Sound.class);
			splash = am.get("sounds/splash.wav", Sound.class);
			squawk = am.get("sounds/squawk.wav", Sound.class);
			victory = am.get("sounds/victory.wav", Sound.class);
		}
	}

	/**
	 * music
	 * @author Chris
	 */
	public class AssetMusic
	{
		public final Music normalSong;
		public final Music bloodlustSong;

		public AssetMusic (AssetManager am)
		{
			normalSong = am.get("music/gold_saucer.mp3", Music.class);
			bloodlustSong = am.get("music/dwts.mp3", Music.class);
		}
	}

	/**
	 * create the asset manager and assets
	 * @param assetManager
	 */
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		// load sounds
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/jump_with_frog.wav", Sound.class);
		assetManager.load("sounds/wings_flapping.wav", Sound.class);
		assetManager.load("sounds/death_explosion.wav", Sound.class);
		assetManager.load("sounds/frog.wav", Sound.class);
		assetManager.load("sounds/cat_yell.wav", Sound.class);
		assetManager.load("sounds/dog_attack.wav", Sound.class);
		assetManager.load("sounds/splash.wav", Sound.class);
		assetManager.load("sounds/squawk.wav", Sound.class);
		assetManager.load("sounds/victory.wav", Sound.class);
		assetManager.load("music/gold_saucer.mp3", Music.class);
		assetManager.load("music/dwts.mp3", Music.class);
		// start loading assets and wait until finished
		assetManager.finishLoading();

		Gdx.app.debug(TAG,  "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG,  "asset: " + a);
		}

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		// create game resource objects
		fonts = new AssetFonts();
		cat = new AssetCat(atlas);
		rock = new AssetRock(atlas);
		bird = new AssetBird(atlas);
		frog = new AssetFrog(atlas);
		dog = new AssetDog(atlas);
		house = new AssetHouse(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}

	/**
	 * free up unused asset memory
	 */
	@Override
	public void dispose()
	{
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	/**
	 * error handling
	 * @param filename
	 * @param type
	 * @param throwable
	 */
	public void error(String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG,  "Couldn't load asset '" + filename + "'", (Exception)throwable);;
	}

	/**
	 * error handling
	 * @param asset
	 * @param throwable
	 */
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG,  "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}

	/**
	 * cat texture (player)
	 * @author Chris Lashley
	 */
	public class AssetCat
	{
		public final AtlasRegion cat;
		public final AtlasRegion bloodlust;
		public final AtlasRegion deadCat;

		// animations
		public final Animation isDead;
		public final Animation isFalling;
		public final Animation isIdle;
		public final Animation isJumping;
		public final Animation isRunning;
		public final Animation isWalking;

		public AssetCat (TextureAtlas atlas)
		{
			cat = atlas.findRegion("cat");
			bloodlust = atlas.findRegion("bloodlust");
			deadCat = atlas.findRegion("deadCat");

			Array<AtlasRegion> regions = null;
			AtlasRegion region = null;

			// dead
			regions = atlas.findRegions("Dead");
			isDead = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.NORMAL);

			// fall
			regions = atlas.findRegions("Fall");
			isFalling = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.NORMAL);

			// idle
			regions = atlas.findRegions("Idle");
			isIdle = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);

			// jump
			regions = atlas.findRegions("Jump");
			isJumping = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.NORMAL);

			// run
			regions = atlas.findRegions("Run");
			isRunning = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);

			// walk
			regions = atlas.findRegions("Walk");
			isWalking = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
		}
	}

	/**
	 * rock textures
	 * @author Chris Lashley
	 */
	public class AssetRock
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetRock (TextureAtlas atlas)
		{
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	/**
	 * bird texture
	 * @author Chris Lashley
	 */
	public class AssetBird
	{
		public final AtlasRegion birdUp;
		public final AtlasRegion birdDown;
		public final AtlasRegion deadBird;

		public AssetBird (TextureAtlas atlas)
		{
			birdUp = atlas.findRegion("birdUp");
			birdDown = atlas.findRegion("birdDown");
			deadBird = atlas.findRegion("deadBird");
		}
	}

	/**
	 * frog texture
	 * @author Chris Lashley
	 */
	public class AssetFrog
	{
		public final AtlasRegion frog;

		public AssetFrog (TextureAtlas atlas)
		{
			frog = atlas.findRegion("frog");
		}
	}

	/**
	 * house texture (goal)
	 * @author Chris Lashley
	 */
	public class AssetHouse
	{
		public final AtlasRegion house;

		public AssetHouse (TextureAtlas atlas)
		{
			house = atlas.findRegion("house");
		}
	}

	/**
	 * dog texture
	 * @author Chris Lashley
	 */
	public class AssetDog
	{
		public final AtlasRegion dog;

		public AssetDog (TextureAtlas atlas)
		{
			dog = atlas.findRegion("dog");
		}
	}

	/**
	 * level decoration textures
	 * @author Chris Lashley
	 */
	public class AssetLevelDecoration
	{
		public final AtlasRegion waterOverlay;
		public final AtlasRegion ground;
		public final AtlasRegion tree1;
		public final AtlasRegion tree2;
		public final AtlasRegion eye01;
		public final AtlasRegion eye02;
		public final AtlasRegion eye03;

		public AssetLevelDecoration (TextureAtlas atlas)
		{
			waterOverlay = atlas.findRegion("water_overlay");
			ground = atlas.findRegion("ground");
			tree1 = atlas.findRegion("tree1");
			tree2 = atlas.findRegion("tree2");
			eye01 = atlas.findRegion("eye01");
			eye02 = atlas.findRegion("eye02");
			eye03 = atlas.findRegion("eye03");
		}
	}
}