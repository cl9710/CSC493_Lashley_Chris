package com.lashleygdx.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.lashleygdx.game.util.Constants;
import com.lashleygdx.game.world.Assets.AssetFonts;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
	public AssetCat bloodlust;
	public AssetBird bird;
	public AssetFrog frog;
	public AssetRock rock;
	public AssetHouse house;
	public AssetDog dog;
	public AssetLevelDecoration levelDecoration;

	public AssetFonts fonts;

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

		public AssetCat (TextureAtlas atlas)
		{
			cat = atlas.findRegion("cat");
			bloodlust = atlas.findRegion("bloodlust");
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
		public final AtlasRegion bird;

		public AssetBird (TextureAtlas atlas)
		{
			bird = atlas.findRegion("bird");
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

		public AssetLevelDecoration (TextureAtlas atlas)
		{
			waterOverlay = atlas.findRegion("water_overlay");
			ground = atlas.findRegion("ground");
			tree1 = atlas.findRegion("tree1");
			tree2 = atlas.findRegion("tree2");
		}
	}
}