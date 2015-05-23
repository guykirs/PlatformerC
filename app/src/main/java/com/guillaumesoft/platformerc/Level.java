package com.guillaumesoft.platformerc;

import android.graphics.Point;
import android.view.Display;

import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.math.Clamp;
import com.badlogic.androidgames.framework.Disposable;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import tv.ouya.console.api.OuyaController;

/**
 * Created by guillaume on 02/07/14.
 * Guillaumesoft
 * Level.java
 * Feb 19, 2015
 */
public class Level implements Disposable
{
    ///////////////////////////////////////////////////////////
    // FINAL VARAIBLES TYPES
    private static final Vector2 InvalidPosition = new Vector2(-1, -1);

    ////////////////////////////////////////////////////
    // CLASS PRIVATE VARAIBLES

    // Arbitrary, but constant seed
    private Random random = new Random(354668);

    // VALUE TO HOLD LOCATION OF EXIT
    private Vector2 exit;

    // RECTANGLE EXIT VARAIBLES
    private Rectangle exitbounds= null;

    // CURRENT GAME LEVEL VALUE
    private int index;

    // CREATE THE SPRITE BATCH FOR THE LEVEL CLASS
    private SpriteBatcher spritebatcher;

    // The layer which entities are drawn on top of.
    private final int EntityLayer = 2;

    // VALUE TO HOLD CONTROLLER STATUS
    private boolean isConnected = true;

    ////////////////////////////////////////////////////
    // CLASS PUBLIC VARAIBLES

    public long time_start;
    public Tile[][] tiles;

    public double TimeRemaing = 120.0;

    public float TimeStatus;

    public boolean reachedExit = false;

    // KEEP TRACK OF GAME SCORE
    public int score;

    ////////////////////////////////////////////////////
    // CLASS INSTANCES
    private ArrayList<String>    sInfo = new ArrayList<>();
    private List<Gem>            gems;
    private List<Enemy>          enamies;
    private List<Axe>            axe;
    private List<Bowl>           bowl;
    private List<BullSkull>      bullskull;
    private List<Dragon>         dragon;
    private List<Food>           food;
    private List<Skull>          skull;
    private List<Spear>          spear;
    private List<Stars>          stars;
    private List<Armour>         armour;
    private List<Barrel>         barrel;
    private List<Fire>           fire;
    private List<GoldCoins>      goldcoins;
    private List<SilverCoins>    silvercoins;
    private List<BronzeCoins>    bronzeCoins;
    private List<FireballPickup> firePickups;
    private List<PowerPickup>    powerPickups;
    private List<Torch>          torch;

    // CREATE A PLAYER INSTANCE
    public Player player;

    public float cameraPosition;

    //private Layer[] layers;
    private Layer layer;

    ////////////////////////////////////////////////////////////
    // CLASS FUNCTIONS

    public Level(InputStream fileStream, int levelindex, SpriteBatcher sp)
    {
        // INITIALIZE CLASS AND OBJECTS
        this.exit         = new Vector2();
        this.gems         = new ArrayList<>();
        this.enamies      = new ArrayList<>();
        this.axe          = new ArrayList<>();
        this.bowl         = new ArrayList<>();
        this.bullskull    = new ArrayList<>();
        this.dragon       = new ArrayList<>();
        this.food         = new ArrayList<>();
        this.skull        = new ArrayList<>();
        this.spear        = new ArrayList<>();
        this.stars        = new ArrayList<>();
        this.armour       = new ArrayList<>();
        this.barrel       = new ArrayList<>();
        this.fire         = new ArrayList<>();
        this.goldcoins    = new ArrayList<>();
        this.silvercoins  = new ArrayList<>();
        this.bronzeCoins  = new ArrayList<>();
        this.firePickups  = new ArrayList<>();
        this.powerPickups = new ArrayList<>();
        this.torch        = new ArrayList<>();

        // LOAD THE TILES
        LoadTiles(fileStream);

        // CURRENT LEVEL
        index = levelindex;

        // GET THE SPRITE BATCH FORM THE GAME SCREEN
        this.spritebatcher = sp;

        layer = new Layer();

        //layers = new Layer[3];
        //layers[0] = new Layer(Assets.layer00Region, Assets.layer00, 0.5f);
        //layers[1] = new Layer(Assets.layer10Region, Assets.layer10, 0.5f);
        //layers[2] = new Layer(Assets.layer20Region, Assets.layer20, 0.5f);
    }

    //////////////////////////////////////////////////////////
    // LOAD THE GAME LEVELS
    //////////////////////////////////////////////////////////
    private void LoadTiles(InputStream fileStream)
    {
        String line;
        String[] saLineElements;

        // Load the level and ensure all of the lines are the same length.
        BufferedReader bufferreader = new BufferedReader(new InputStreamReader(fileStream));

        try
        {
            while ((line = bufferreader.readLine()) != null)
            {
                //The information is split into segments and stored into the array
                saLineElements = line.split(" ");
                sInfo.add(saLineElements[0]);
            }
            bufferreader.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // REVERSE THE ORDER OF THE ARRAY
        Collections.reverse(sInfo);

        // INITIALIZE THE TILE ARRAY
        tiles = new Tile[96][33];

        for(int y=0; y < 33; y++)
        {
            for (int x = 0; x < 96; x++)//29
            {
                // to load each tile.
                char tileType = sInfo.get(y).charAt(x);
                tiles[x][y] = LoadTile(tileType, x, y);
            }
        }

        // Verify that the level has a beginning and an end.
        if (player == null)
            throw new UnsupportedOperationException("A level must have a starting point.");
        if (exit == InvalidPosition)
            throw new UnsupportedOperationException("A level must have an exit.");
    }

    //////////////////////////////////////////////////////
    // TRANSLATE CHAR TO TO TILES THEN LOAD THE TILES
    //////////////////////////////////////////////////////
    private Tile LoadTile(char tileType, float x, float y)
    {
        switch (tileType)
        {
            // Blank space
            case '.':
                return new Tile(x, y, null, TileCollision.Passable);

            case 'A':
                // LOAD ALL THE ENEMY
                return LoadEnemiesTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT + 30, 0);

            case 'B':
                // LOAD ALL THE ENEMY
                return LoadEnemiesTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT + 30, 1);

            case 'C':
                // LOAD ALL THE ENEMY
                return LoadEnemiesTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT + 30, 2);

            case 'G':
                // LOAD ALL THE GEMS
                return LoadGemTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case '#':
                return LoadTile(x, y, Assets.BlockARegion, TileCollision.Impassable);

            case 'X':
                return LoadExitTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case '1':
                return LoadStartTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            //Ladder
            case 'H':
                return LoadTile(x, y, Assets.ladder, TileCollision.Ladder);

            case 'P':
                return LoadTile(x, y, Assets.BlockARegion, TileCollision.Checkpoint);

            case 'E':
               // LOAD ALL THE AXE
               return LoadAxeTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'F':
                // LOAD ALL THE BOWL
                return LoadBowlTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'I':
                // LOAD ALL THE BULLSKULL
                return LoadBullskullTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'O':
                // LOAD ALL THE DRAGON
                return LoadDragonTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'J':
                // LOAD ALL THE FOOD
                return LoadFoodTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'K':
                // LOAD ALL THE SKULL
                return LoadSkullTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'L':
                // LOAD ALL THE SPEAR
                return LoadSpearTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'N':
                // LOAD ALL THE STARS
                return LoadStarsTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'Q':
                // LOAD ALL THE STARS
                return LoadArmourTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'R':
                // LOAD ALL THE STARS
                return LoadBarrelTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'T':
                // LOAD THE FIRE
               return LoadFireTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'U':
               // LOAD THE FIRE
               return LoadGoldCoinsTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'V':
                // LOAD THE FIRE
                return LoadSilverCoinsTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case 'W':
                // LOAD THE FIRE
                return LoadBronzeCoinsTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case '2':
                // LOAD THE POWER
                return LoadPowerPickUpTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case '3':
                // LOAD THE FIRE
                return LoadFirePickUpTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            case '4':
                // LOAD THE TORCH
                return LoadTorchTile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);

            default:
                throw new UnsupportedOperationException("Unsupported tile type character");
        }
    }


    private Tile LoadTile(float x, float y, TextureRegion region, TileCollision collision)
    {
        return new Tile(x, y, region, collision);
    }

    /// <summary>
    /// Remembers the location of the level's exit.
    /// </summary>
    private Tile LoadExitTile(float x, float y)
    {
        exitbounds = new Rectangle(x , y, 0.6f, 0.6f);

        return LoadTile(x, y, Assets.ExitRegion, TileCollision.Passable );
    }

    /// <summary>
    /// Instantiates a player, puts him in the level, and remembers where to put him when he is resurrected.
    /// </summary>
    private Tile LoadStartTile(float x, float y)
    {
        if (player != null)
            throw new IllegalArgumentException("A level may only have one starting point.");

        // INITIALIZE THE PLAYER
        player = new Player(this, new Vector2(x, y));

        // RETURN TILE VALUE
        return new Tile(x, y, null, TileCollision.Passable );
    }

    /// <summary>
    /// Instantiates an enemy A and puts him in the level.
    /// </summary>
    private Tile LoadEnemiesTile(float x, float y, int iType)
    {
        Enemy ENEMY = new Enemy(this, new Vector2(x, y), iType);
        this.enamies.add(ENEMY);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates the torch animation
    /// </summary>
    private Tile LoadTorchTile(float x, float y)
    {
        Torch TORCH  = new Torch(x, y);
        this.torch.add(TORCH);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates the fire animation
    /// </summary>
    private Tile LoadFireTile(float x, float y)
    {
        Fire FIRE = new Fire(x, y);
        this.fire.add(FIRE);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    //////////////////////////////////////////////////////////////////
    // LOAD THE POWER PICKUP TILES
    private Tile  LoadPowerPickUpTile(float x, float y)
    {
        PowerPickup POWERPICKUP = new PowerPickup(x, y);
        this.powerPickups.add(POWERPICKUP);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    //////////////////////////////////////////////////////////////////
    // LOAD THE FIRE PICKUP TILES
    private Tile  LoadFirePickUpTile(float x, float y)
    {
        FireballPickup FIREPICKUP = new FireballPickup(x, y);
        this.firePickups.add(FIREPICKUP);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    //////////////////////////////////////////////////////////////////
    // LOAD THE GOLD COINS
    private Tile LoadGoldCoinsTile(float x, float y)
    {
        GoldCoins GOLDCOINS = new GoldCoins(x, y);
        this.goldcoins.add(GOLDCOINS);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    //////////////////////////////////////////////////////////////////
    // LOAD THE GOLD COINS
    private Tile LoadSilverCoinsTile(float x, float y)
    {
        SilverCoins SILVERCOINS = new SilverCoins(x, y);
        this.silvercoins.add(SILVERCOINS);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    //////////////////////////////////////////////////////////////////
    // LOAD THE GOLD COINS
    private Tile LoadBronzeCoinsTile(float x, float y)
    {
        BronzeCoins BRONZECOINS = new BronzeCoins(x, y);
        this.bronzeCoins.add(BRONZECOINS);

        return new Tile(x, y, null, TileCollision.Passable);
    }


    /// <summary>
    /// Instantiates a gem and put it in the level.
    /// </summary>
    private Tile LoadGemTile(float x, float y)
    {
        Gem GEM = new Gem(x, y);
        this.gems.add(GEM);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a armour and put it in the level.
    /// </summary>
    private Tile LoadArmourTile(float x, float y)
    {
        Armour ARMOUR = new Armour(x, y);
        this.armour.add(ARMOUR);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a axe and put it in the level.
    /// </summary>
    private Tile LoadAxeTile(float x, float y)
    {
        Axe AXE = new Axe(x, y);
        this.axe.add(AXE);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a axe and put it in the level.
    /// </summary>
    private Tile LoadBarrelTile(float x, float y)
    {
        Barrel BARREL = new Barrel(x, y);
        this.barrel.add(BARREL);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a bowl and put it in the level.
    /// </summary>
    private Tile LoadBowlTile(float x, float y)
    {
        Bowl BOWL = new Bowl(x, y);
        this.bowl.add(BOWL);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a bullskull and put it in the level.
    /// </summary>
    private Tile LoadBullskullTile(float x, float y)
    {
        BullSkull BULLSKULL = new BullSkull(x, y);
        this.bullskull.add(BULLSKULL);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a dragon and put it in the level.
    /// </summary>
    private Tile LoadDragonTile(float x, float y)
    {
        Dragon DRAGON = new Dragon(x, y);
        this.dragon.add(DRAGON);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a food and put it in the level.
    /// </summary>
    private Tile LoadFoodTile(float x, float y)
    {
        Food FOOD = new Food(x, y);
        this.food.add(FOOD);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a food and put it in the level.
    /// </summary>
    private Tile LoadSkullTile(float x, float y)
    {
        Skull SKULL = new Skull(x, y);
        this.skull.add(SKULL);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a food and put it in the level.
    /// </summary>
    private Tile LoadSpearTile(float x, float y)
    {
        Spear SPEAR = new Spear( x, y);
        this.spear.add(SPEAR);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /// <summary>
    /// Instantiates a food and put it in the level.
    /// </summary>
    private Tile LoadStarsTile(float x, float y)
    {
        Stars STAR = new Stars( x, y);
        this.stars.add(STAR);

        return new Tile(x, y, null, TileCollision.Passable);
    }

    /////////////////////////////////////////////
    // UPDATE THE THE POWER PICK UP TILE
    /////////////////////////////////////////////
    public void updatePowerPickUp(float deltaTime)
    {
        int len = this.powerPickups.size();
        for(int i = 0; i < len; i++)
        {
            PowerPickup POWERPICKUP = this.powerPickups.get(i);
            POWERPICKUP.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, POWERPICKUP.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                this.powerPickups.remove(i);

                OnPowerPickupCollected(POWERPICKUP);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE FIRE PICK UP TILE
    /////////////////////////////////////////////
    public void updateFirePickUp(float deltaTime)
    {
        int len = this.firePickups.size();
        for(int i = 0; i < len; i++)
        {
            FireballPickup FIREPICKUP = this.firePickups.get(i);
            FIREPICKUP.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, FIREPICKUP.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                this.firePickups.remove(i);

                OnFirePickupCollected(FIREPICKUP);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE GOLD COILD TILE
    /////////////////////////////////////////////
    public void updateGoldCoins(float deltaTime)
    {
        int len = this.goldcoins.size();
        for(int i = 0; i < len; i++)
        {
            GoldCoins GOLDCOINS = this.goldcoins.get(i);
            GOLDCOINS.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, GOLDCOINS.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                goldcoins.remove(i);

                OnGoldCoinsCollected(GOLDCOINS);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE SILVER COILD TILE
    /////////////////////////////////////////////
    public void updateTorchAnimation(float deltaTime)
    {
        int len = this.torch.size();
        for (int i = 0; i < len; i++)
        {
            Torch TORCH = this.torch.get(i);
            TORCH.Update(deltaTime);
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE SILVER COILD TILE
    /////////////////////////////////////////////
    public void updateSilverCoins(float deltaTime)
    {
        int len = this.silvercoins.size();
        for(int i = 0; i < len; i++)
        {
            SilverCoins SILVERCOINS = this.silvercoins.get(i);
            SILVERCOINS.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, SILVERCOINS.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                silvercoins.remove(i);

                OnSilverCoinsCollected(SILVERCOINS);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE BRONZE COILD TILE
    /////////////////////////////////////////////
    public void updateBronzeCoins(float deltaTime)
    {
        int len = this.bronzeCoins.size();
        for(int i = 0; i < len; i++)
        {
            BronzeCoins BRONZECOINS = this.bronzeCoins.get(i);
            BRONZECOINS.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, BRONZECOINS.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                bronzeCoins.remove(i);

                OnBronzeCoinsCollected(BRONZECOINS);

                break;
            }
        }
    }


    /////////////////////////////////////////////
    // UPDATE THE THE GEM TILE
    /////////////////////////////////////////////
    public void updateGem(float deltaTime)
    {
        int len = this.gems.size();
        for(int i = 0; i < len; i++)
        {
            Gem GEM = this.gems.get(i);
            GEM.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, GEM.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                gems.remove(i);

                OnGemCollected(GEM);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE AXE TILE
    /////////////////////////////////////////////
    public void updateAxe(float deltaTime)
    {
        int len = this.axe.size();
        for(int i = 0; i < len; i++)
        {
            Axe AXE = this.axe.get(i);
            AXE.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, AXE.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                axe.remove(i);

                OnAxeCollected(AXE);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE BARREL TILE
    /////////////////////////////////////////////
    public void updateBarrel(float deltaTime)
    {
        int len = this.barrel.size();
        for(int i = 0; i < len; i++)
        {
            Barrel BARREL = this.barrel.get(i);
            BARREL.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, BARREL.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                barrel.remove(i);

                OnBarrelCollected(BARREL);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE BOWL TILE
    /////////////////////////////////////////////
    public void updateBowl(float deltaTime)
    {
        int len = this.bowl.size();
        for(int i = 0; i < len; i++)
        {
            Bowl BOWL = this.bowl.get(i);
            BOWL.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, BOWL.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                bowl.remove(i);

                OnBowlCollected(BOWL);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE BOWL TILE
    /////////////////////////////////////////////
    public void updateBullskull(float deltaTime)
    {
        int len = this.bullskull.size();
        for(int i = 0; i < len; i++)
        {
            BullSkull BULLSKULL = this.bullskull.get(i);
            BULLSKULL.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, BULLSKULL.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                bullskull.remove(i);

                OnBullskullCollected(BULLSKULL);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE DRAGON TILE
    /////////////////////////////////////////////
    public void updateDragon(float deltaTime)
    {
        int len = this.dragon.size();
        for(int i = 0; i < len; i++)
        {
            Dragon DRAGON = this.dragon.get(i);
            DRAGON.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, DRAGON.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                dragon.remove(i);

                OnDragonCollected(DRAGON);

                break;
            }
        }
    }


    /////////////////////////////////////////////
    // UPDATE THE THE FOOD TILE
    /////////////////////////////////////////////
    public void updateFood(float deltaTime)
    {
        int len = this.food.size();
        for(int i = 0; i < len; i++)
        {
            Food FOOD = this.food.get(i);
            FOOD.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, FOOD.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                food.remove(i);

                OnFoodCollected(FOOD);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE SKULL TILE
    /////////////////////////////////////////////
    public void updateSkull(float deltaTime)
    {
        int len = this.skull.size();
        for(int i = 0; i < len; i++)
        {
            Skull SKULL = this.skull.get(i);
            SKULL.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, SKULL.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                skull.remove(i);

                OnSkullCollected(SKULL);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE SPEAR TILE
    /////////////////////////////////////////////
    public void updateSpear(float deltaTime)
    {
        int len = this.spear.size();
        for(int i = 0; i < len; i++)
        {
            Spear SPEAR = this.spear.get(i);
            SPEAR.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, SPEAR.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                spear.remove(i);

                OnSpearCollected(SPEAR);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE SPEAR TILE
    /////////////////////////////////////////////
    public void updateStars(float deltaTime)
    {
        int len = this.stars.size();
        for(int i = 0; i < len; i++)
        {
            Stars STARS = this.stars.get(i);
            STARS.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, STARS.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                stars.remove(i);

                OnStarsCollected(STARS);

                break;
            }
        }
    }

    /////////////////////////////////////////////
    // UPDATE THE THE SPEAR TILE
    /////////////////////////////////////////////
    public void updateArmour(float deltaTime)
    {
        int len = this.armour.size();
        for(int i = 0; i < len; i++)
        {
            Armour ARMOUR = this.armour.get(i);
            ARMOUR.Update(deltaTime);

            if (OverlapTester.overlapRectangles(player.bounds, ARMOUR.bounds))
            {
                if(Settings.soundEnabled)
                    Assets.playSound(Assets.gemCollected);

                armour.remove(i);

                OnArmourCollected(ARMOUR);

                break;
            }
        }
    }

    /////////////////////////////////////////////////////////
    // HAS THE  PLAYER COLLIDED WITH THE EXIT TILE
    /////////////////////////////////////////////////////////
    private void OnExitReached()
    {
        player.OnReachedExit();
        Assets.playSound(Assets.exitReached);
        reachedExit = true;
    }

    ///////////////////////////////////////////////
    // UPDATE THE MONSTER A
    ///////////////////////////////////////////////
    public void updateEnemies(float deltaTime)
    {
        int len = this.enamies.size();
        for(int i = 0; i < len; i++)
        {
            Enemy ENEMY = this.enamies.get(i);
            ENEMY.Update(deltaTime);

            if((player.GetIsAlive())&&(ENEMY.isAlive))
            {
                if (OverlapTester.overlapRectangles(player.bounds, ENEMY.bounds))
                {
                    //  IF THE PLAYER IS ATTACKING AND THE ENEMY IS ALIVE THEN KILL THE ENEMY
                    if (player.GetAttacking())
                    {
                        ENEMY.OnKilled();
                    }
                    else
                    {
                        player.OnKilled();

                        if (player.GetLives() == 0)
                        {
                            OnPlayerKilled();
                        }
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////
    // THE PLAYER COLLECTED THE FIRE
    private void OnFirePickupCollected(FireballPickup fire)
    {
        score += fire.PointValue;

        fire.OnCollected();
    }

    ///////////////////////////////////////////////////////////
    // THE PLAYER COLLECTED THE POWER
    private void OnPowerPickupCollected(PowerPickup power)
    {
        score += power.PointValue;

        power.OnCollected();
    }

    ///////////////////////////////////////////////////////////
    // THE PLAYER COLLECTED THE GOLD COINS
    private void OnGoldCoinsCollected(GoldCoins gold)
    {
        score += gold.PointValue;

        gold.OnCollected();
    }

    ///////////////////////////////////////////////////////////
    // THE PLAYER COLLECTED THE SILVER COINS
    private void OnSilverCoinsCollected(SilverCoins silver)
    {
        score += silver.PointValue;

        silver.OnCollected();
    }

    ///////////////////////////////////////////////////////////
    // THE PLAYER COLLECTED THE BRONZE COINS
    private void OnBronzeCoinsCollected(BronzeCoins bronze)
    {
        score += bronze.PointValue;

        bronze.OnCollected();
    }

    /// <summary>
    /// Called when a axe is collected.
    /// </summary>
    /// <param name="gem">The gem that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnGemCollected(Gem gem)
    {
        score += gem.PointValue;

        gem.OnCollected();
    }

    /// <summary>
    /// Called when a gem is collected.
    /// </summary>
    /// <param name="barrel">The barrel that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnBarrelCollected(Barrel barrel)
    {
        score += barrel.PointValue;

        barrel.OnCollected();
    }

    /// <summary>
    /// Called when a axe is collected.
    /// </summary>
    /// <param name="axe">The axe that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnAxeCollected(Axe axe)
    {
        score += axe.PointValue;

        axe.OnCollected();
    }

    /// <summary>
    /// Called when a bowl is collected.
    /// </summary>
    /// <param name="bowl">The bowl that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnBowlCollected(Bowl bowl)
    {
        score += bowl.PointValue;
        bowl.OnCollected();
    }


    /// <summary>
    /// Called when a bullskull is collected.
    /// </summary>
    /// <param name="bullskull">The bullskull that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnBullskullCollected(BullSkull bullskull)
    {
        score += bullskull.PointValue;

        bullskull.OnCollected();
    }

    /// <summary>
    /// Called when a dragon is collected.
    /// </summary>
    /// <param name="dragon">The dragon that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnDragonCollected(Dragon dragon)
    {
        score += dragon.PointValue;

        dragon.OnCollected();
    }

    /// <summary>
    /// Called when a food is collected.
    /// </summary>
    /// <param name="food">The food that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnFoodCollected(Food food)
    {
        score += food.PointValue;

        food.OnCollected();
    }

    /// <summary>
    /// Called when a skull is collected.
    /// </summary>
    /// <param name="skull">The skull that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnSkullCollected(Skull skull)
    {
        score += skull.PointValue;

        skull.OnCollected();
    }

    /// <summary>
    /// Called when a spear is collected.
    /// </summary>
    /// <param name="spear">The spear that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnSpearCollected(Spear spear)
    {
        score += spear.PointValue;

        spear.OnCollected();
    }

    /// <summary>
    /// Called when a spear is collected.
    /// </summary>
    /// <param name="spear">The spear that was collected.</param>
    /// <param name="collectedBy">The player who collected this gem.</param>
    private void OnStarsCollected(Stars star)
    {
        score += star.PointValue;

        star.OnCollected();
    }

    private void OnArmourCollected(Armour armour)
    {
        score += armour.PointValue;

        armour.OnCollected();
    }


    /// <summary>
    /// Called when the player is killed.
    /// </summary>
    /// <param name="killedBy">
    /// The enemy who killed the player. This is null if the player was not killed by an
    /// enemy, such as when a player falls into a hole.
    /// </param>
    private void OnPlayerKilled()
    {
        player.OnKilled();
    }

    /////////////////////////////////////////////
    // UPDATE THE THE FIRE TILE
    /////////////////////////////////////////////
    public void updateFire(float deltaTime)
    {
        int len = this.fire.size();
        for(int i = 0; i < len; i++)
        {
            Fire FIRE = this.fire.get(i);
            FIRE.Update(deltaTime);
        }
    }

    /////////////////////////////////////////////////////////////
    // LEVEL UPDATE FUNCTION
    public void update(float deltaTime,  OuyaController  gamePadState)
    {
        TimeStatus =+ deltaTime;

        final int PointsPerSecond = 5;

        // Pause while the player is dead or time is expired.
        if (!player.GetIsAlive() || TimeRemaing == 0.0f)
        {
            // Still want to perform physics on the player.
            player.ApplyPhysics(deltaTime);
        }
        else if (reachedExit)
        {
            // Animate the time being converted into points.
            int seconds = (int)Math.round(deltaTime * 100.0f);
            seconds      = Math.min(seconds, (int) Math.ceil(deltaTime));
            TimeRemaing -= seconds;
            score += seconds * PointsPerSecond;
        }
        else
        {
            TimeRemaing -= deltaTime;
            player.Update(deltaTime, gamePadState);
            updateGem(deltaTime);
            updateEnemies(deltaTime);
            updateAxe(deltaTime);
            updateBowl(deltaTime);
            updateBullskull(deltaTime);
            updateDragon(deltaTime);
            updateFood(deltaTime);
            updateSkull(deltaTime);
            updateSpear(deltaTime);
            updateStars(deltaTime);
            updateArmour(deltaTime);
            updateBarrel(deltaTime);
            updateFire(deltaTime);
            updateGoldCoins(deltaTime);
            updateSilverCoins(deltaTime);
            updateBronzeCoins(deltaTime);
            updateFirePickUp(deltaTime);
            updatePowerPickUp(deltaTime);
            updateTorchAnimation(deltaTime);

            // DID THE USER HIT THE EXIT
            if (OverlapTester.overlapRectangles(exitbounds, player.bounds))
                OnExitReached();

            if(player.position.y < 0.0f)
                player.OnKilled();

            // Falling off the bottom of the level kills the player.
            //if (player.BoundingRectangle().top >= Height() * Tiles.Height)
               //OnPlayerKilled(null);

            // The player has reached the exit if they are standing on the ground and
            // his bounding rectangle contains the center of the exit tile. They can only
            // exit when they have collected all of the gems.
            //if (player.isAlive && player.isOnGround && player.BoundingRectangle().Contains(exit))
            //{
            //OnExitReached();
            //}
        }

        // Clamp the time remaining at zero.
        if (TimeRemaing < 0.0f)
            TimeRemaing = 0.0f;
    }

    // DISPLAY THE RE-CONNECT MESSAGE TO THE USER IF THE CONTROLLER BECOMES DISCONNECTED
    private void presentController()
    {
        this.spritebatcher.beginBatch(Assets.RedFont);

           Assets.redfont.drawText(this.spritebatcher, "Please re-connect your controller", 1920 / 2 - 400, 1080 / 2, 20.0f, 20.0f);

        this.spritebatcher.endBatch();
    }

    // DISPLAY THE LEVEL FINISHED TO THE USER
   /* private void presentLevelEnd()
    {
        this.spritebatcher.beginBatch(Assets.messages);

           this.spritebatcher.drawSprite(1920 /2, 1080 /2, 300, 150, Assets.winRegion);

        this.spritebatcher.endBatch();
    }*/

    // DISPLAY THE PLAYER READY MESSAGE
    private void presentReady()
    {
        this.spritebatcher.beginBatch(Assets.items);

           this.spritebatcher.drawSprite(1920 /2, 1080 /2, 170, 50, Assets.ready);

        this.spritebatcher.endBatch();
    }

    ////////////////////////////////////////////////////////////////////////////
    // DRAW THE GAME SCREENS
    ////////////////////////////////////////////////////////////////////////////
    public void Draw()
    {

       //for (int i = 0; i <= 2; ++i)
           // layers[0].Draw(this.spritebatcher, cameraPosition);
        layer.Draw(this.spritebatcher, cameraPosition);

        // DRAW THE TILES TO THE SCREEN
        DrawTiles();

        // DRAW THE GEMS ON THE SCREEN
       int  len = this.gems.size();
        for(int i = 0; i < len; i++)
        {
            Gem GEM = this.gems.get(i);
            GEM.Draw( this.spritebatcher);
        }

        // DRAW MONSTER A TO THE SCREEN
        len = this.enamies.size();
        for(int i = 0; i < len; i++)
        {
            Enemy ENEMY = this.enamies.get(i);

            if(ENEMY.isAlive)
               ENEMY.Draw(this.spritebatcher);
        }

        len = this.axe.size();
        for(int i = 0; i < len; i++)
        {
            Axe AXE = this.axe.get(i);
            AXE.Draw(this.spritebatcher);
        }

        len = this.bowl.size();
        for(int i = 0; i < len; i++)
        {
            Bowl BOWL = this.bowl.get(i);
            BOWL.Draw(this.spritebatcher);
        }

        len = this.bullskull.size();
        for(int i = 0; i < len; i++)
        {
            BullSkull BULLSKULL = this.bullskull.get(i);
            BULLSKULL.Draw(this.spritebatcher);
        }

        len = this.dragon.size();
        for(int i = 0; i < len; i++)
        {
            Dragon DRAGON = this.dragon.get(i);
            DRAGON.Draw(this.spritebatcher);
        }

        len = this.food.size();
        for(int i = 0; i < len; i++)
        {
            Food FOOD = this.food.get(i);
            FOOD.Draw(this.spritebatcher);
        }

        len = this.skull.size();
        for(int i = 0; i < len; i++)
        {
            Skull SKULL = this.skull.get(i);
            SKULL.Draw(this.spritebatcher);
        }

        len = this.spear.size();
        for(int i = 0; i < len; i++)
        {
            Spear SPEAR = this.spear.get(i);
            SPEAR.Draw(this.spritebatcher);
        }

        len = this.stars.size();
        for(int i = 0; i < len; i++)
        {
            Stars STARS = this.stars.get(i);
            STARS.Draw(this.spritebatcher);
        }

        len = this.armour.size();
        for(int i = 0; i < len; i++)
        {
            Armour ARMOUR = this.armour.get(i);
            ARMOUR.Draw(this.spritebatcher);
        }

        len = this.barrel.size();
        for(int i = 0; i < len; i++)
        {
            Barrel BARREL = this.barrel.get(i);
            BARREL.Draw(this.spritebatcher);
        }

        len = this.fire.size();
        for(int i = 0; i < len; i++)
        {
            Fire FIRE = this.fire.get(i);
            FIRE.Draw(this.spritebatcher);
        }

        ////////////////////////////////////////////////////////////
        // DRAW THE GOLD COINS
        len = this.goldcoins.size();
        for(int i = 0; i < len; i++)
        {
            GoldCoins GOLDCOINS = this.goldcoins.get(i);
            GOLDCOINS.Draw(this.spritebatcher);
        }

        // DRAW THE SILVER COINS
        len = this.silvercoins.size();
        for(int i = 0; i < len; i++)
        {
            SilverCoins SILVERCOINS = this.silvercoins.get(i);
            SILVERCOINS.Draw(this.spritebatcher);
        }

        //DRAW THE BRONZE COINS
        len = this.bronzeCoins.size();
        for(int i = 0; i < len; i++)
        {
            BronzeCoins BRONZECOINS = this.bronzeCoins.get(i);
            BRONZECOINS.Draw(this.spritebatcher);
        }

        // DRAW THE POWER PICKUP TILES
        len = this.powerPickups.size();
        for(int i = 0; i < len; i++)
        {
            PowerPickup POWERPICKUP = this.powerPickups.get(i);
            POWERPICKUP.Draw(this.spritebatcher);
        }

        // DRAW THE FIRE PICKUP TILES
        len = this.firePickups.size();
        for(int i = 0; i < len; i++)
        {
            FireballPickup FIREPICKUP = this.firePickups.get(i);
            FIREPICKUP.Draw(this.spritebatcher);
        }

        len = this.torch.size();
        for (int i = 0; i < len; i++)
        {
            Torch TORCH = this.torch.get(i);
            TORCH.Draw(this.spritebatcher);
        }

        // DRAW THE PLAYER
        player.Draw(this.spritebatcher);

        /*switch(ScreenManager.STATE)
        {
            case ScreenManager.GAME_READY:
                presentReady();
                break;
            case ScreenManager.GAME_OVER:
                presentGameOver();
                break;
            case ScreenManager.GAME_LEVEL_END:
                presentLevelEnd();
                break;
        }

        // IS THE CONTROLLER STILL CONNECTED
        // IF ITS NOT TELL THE USER
        if(!isConnected)
            presentController();*/
    }

    /// <summary>
    /// Draws each tile in the level.
    /// </summary>
    private void DrawTiles()
    {
        // For each tile position
        for(int y=0; y < 33; y++)
        {
            for (int x = 0; x < 96; x++)//29
            {
                // If there is a visible tile in that position
                TextureRegion texture = tiles[x][y].texture;

                if (texture != null)
                {
                    // Draw it in screen space.
                    Vector2 position = new Vector2(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);// * tile.size();

                    this.spritebatcher.beginBatch(Assets.tile);

                       this.spritebatcher.drawSprite(position.x, position.y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, texture);

                    this.spritebatcher.endBatch();
                }
            }
        }
    }

    public TileCollision GetCollision(int x, int y)
    {
        // Prevent escaping past the level ends.
        if (x < 0 || x >= 1920)
            return TileCollision.Impassable;
        // Allow jumping past the level top and falling through the bottom.
        if (y < 0 || y >= 1080)
            return TileCollision.Impassable;

        return tiles[x][y].Collision;
    }

    /// <summary>
    /// Restores the player to the starting point to try the level again.
    /// </summary>
    public void StartNewLife()
    {
        player.Reset(new Vector2(player.GetStart()));
    }

    public void dispose()  {   }

    ///////////////////////////////////////////////////////////////////////
    // KEEP THE PLAYER WITHIN THE CAMERA
    //////////////////////////////////////////////////////////////////////

    public void ScrollCamera(Display display)
    {
        final float ViewMargin = 0.35f;
        Point size = new Point();
        display.getSize(size);

        // Calculate the edges of the screen.
        float marginWidth = size.x * ViewMargin;
        float marginLeft  = cameraPosition + marginWidth;
        float marginRight = cameraPosition + size.x - marginWidth;

        // Calculate how far to scroll when the player is near the edges of the screen.
        float cameraMovement = 0.0f;

        if (player.position.x < marginLeft)
            cameraMovement = player.position.x - marginLeft;

        else if (player.position.x > marginRight)
            cameraMovement = player.position.x - marginRight;

        // Update the camera position, but prevent scrolling off the ends of the level.
        float maxCameraPosition = Tile.TILE_WIDTH *  tiles.length - size.x;
        cameraPosition = Clamp.clamp(cameraPosition + cameraMovement, 0.0f, maxCameraPosition);
    }
}