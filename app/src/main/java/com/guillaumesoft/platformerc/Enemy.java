package com.guillaumesoft.platformerc;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.math.Clamp;
import com.badlogic.androidgames.framework.math.Vector2;

/// <summary>
/// A monster who is impeding the progress of our fearless adventurer.
/// </summary>
class Enemy extends DynamicGameObject
{
    /////////////////////////////////////////
    // CLASS CONSTANTS
    private static final float ENEMY_WIDTH  = 90f;
    private static final float ENEMY_HEIGHT = 100f;

    private static final int   MONSTER_STATE_WALK    = 0;
    private static final int   MONSTER_STATE_IDLE    = 1;
    private static final int   MONSTER_STATE_KILLED  = 2;

    //////////////////////////////////////////
    // CLASS VARAIBLES
    public  int     state;
    private float   elapsed;
    public boolean isAlive;

    /// <summary>
    /// How long this enemy has been waiting before turning around.
    /// </summary>
    private float waitTime;
    private int side = 0;
    private Level level;
    private int Character;

    private final float deathTimeMax = 1.0f;
    public float deathTime = deathTimeMax;

    /// <summary>
    /// Constructs a new Enemy.
    /// </summary>
    public Enemy(Level level, Vector2 position, int iType)
    {
        super(position.x, position.y, ENEMY_WIDTH, ENEMY_HEIGHT);

        this.level = level;
        this.position = position;

        state = MONSTER_STATE_IDLE;

        // RESET PLAYER VELOCITY
        velocity = new Vector2(Vector2.Zero());

        isAlive = true;

        side = -1;

        Character = iType;
    }

    /// <summary>
    /// Paces back and forth along a platform, waiting at either end.
    /// </summary>
    public void Update(float deltaTime)
    {
        if(!isAlive)
           deathTime -= deltaTime;

        final float MaxWaitTime = 0.5f;
        final float MoveSpeed = 0.9f;
        final float MaxMoveSpeed = 1.5f;

        elapsed += deltaTime;

        // KEEP TRAK OF THE BOUNDS
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);

        // Calculate tile position based on the side we are walking towards.
        float posX = position.x + bounds.width / 2 * side;
        int tileX  = (int)Math.floor(posX / Tile.TILE_WIDTH) - side;
        int tileY  = (int)Math.floor(position.y / Tile.TILE_HEIGHT);

        if(isAlive)
        {
            if (waitTime > 0)
            {
                // Wait for some amount of time.
                waitTime = Math.max(0.0f, waitTime - deltaTime);

                state = MONSTER_STATE_IDLE;

                if (waitTime <= 0.0f)
                {
                    if(side == 1)
                        side = -1;
                    else
                        side = 1;
                }
            }
            else
            {
                if(side == 1)
                {

                    //If we are about to run into a wall or off a cliff, start waiting.
                    if ((level.GetCollision(tileX + side, tileY - 1) == TileCollision.Impassable)||(level.GetCollision(tileX + side, tileY - 1) == TileCollision.Checkpoint))
                    {
                        state = MONSTER_STATE_WALK;

                        // Move in the current direction.
                        Vector2 velocity = new Vector2(side * MoveSpeed * elapsed, 0.0f);
                        velocity.x = Clamp.clamp(velocity.x, -MaxMoveSpeed, MaxMoveSpeed);
                        position.add(velocity);
                    }
                    else
                    {
                        waitTime = MaxWaitTime;
                    }
                }
                else
                {
                    //If we are about to run into a wall or off a cliff, start waiting.
                    if ((level.GetCollision(tileX - side , tileY - 1) == TileCollision.Impassable)||(level.GetCollision(tileX - side, tileY - 1) == TileCollision.Checkpoint))
                    {
                        state = MONSTER_STATE_WALK;

                        // Move in the current direction.
                        Vector2 velocity = new Vector2(side * MoveSpeed * elapsed, 0.0f);
                        velocity.x = Clamp.clamp(velocity.x, -MaxMoveSpeed, MaxMoveSpeed);
                        position.add(velocity);
                    }
                    else
                    {
                        waitTime = MaxWaitTime;
                    }
                }

                // DID THE ENEMY FIND THE PLAY
                float chase  = position.x - level.player.position.x;
                float diff  = position.y - level.player.position.y;

                if((chase <=  120)&&(diff < 54.0)&&(diff > 0.0))
                {
                    if(position.x > level.player.position.x)
                    {
                        side = -1;
                    }
                    else
                    {
                        side = 1;
                    }
                }

                // HAVE WE HI THE SIDE OF THE SCREEN
                if((position.x < Tile.TILE_WIDTH * 3) ||(position.x > 1920 - Tile.TILE_WIDTH * 3))
                {
                    if(side == 1)
                        side = -1;
                    else
                        side = 1;
                }
            }
        }
    }

    public void OnKilled()
    {
        state = MONSTER_STATE_KILLED;

        Assets.playSound(Assets.monsterkilled);

        isAlive = false;
    }

    /// <summary>
    /// Draws the animated enemy.
    /// </summary>
    public void Draw( SpriteBatcher batcher)
    {
        TextureRegion keyFrame = null;

        switch(Character)
        {
            case 0:
                switch (state)
                {

                    case MONSTER_STATE_IDLE:
                        keyFrame = Assets.monsterIdleA.getKeyFrame(elapsed, Animation.ANIMATION_LOOPING);
                        break;
                    case MONSTER_STATE_WALK:
                        keyFrame = Assets.monsterRunA.getKeyFrame(elapsed, Animation.ANIMATION_LOOPING);
                        break;
                    case MONSTER_STATE_KILLED:
                        if(deathTime < deathTimeMax)
                            keyFrame = Assets.monsterIdleA.getKeyFrame(elapsed, Animation.ANIMATION_NONLOOPING);
                        break;
                    default:
                        keyFrame = Assets.monsterIdleA.getKeyFrame(elapsed, Animation.ANIMATION_NONLOOPING);
                        break;
                }

                batcher.beginBatch(Assets.monsterA);

                  batcher.drawSprite(position.x + Tile.TILE_HEIGHT, position.y, side * ENEMY_WIDTH, ENEMY_HEIGHT, keyFrame);

                batcher.endBatch();
                break;
            case 1:
                switch (state)
                {

                    case MONSTER_STATE_IDLE:
                        keyFrame = Assets.monsterIdleB.getKeyFrame(elapsed, Animation.ANIMATION_LOOPING);
                        break;
                    case MONSTER_STATE_WALK:
                        keyFrame = Assets.monsterRunB.getKeyFrame(elapsed, Animation.ANIMATION_LOOPING);
                        break;
                    case MONSTER_STATE_KILLED:
                        if(deathTime < deathTimeMax)
                            keyFrame = Assets.monsterIdleB.getKeyFrame(elapsed, Animation.ANIMATION_NONLOOPING);
                        break;
                    default:
                        keyFrame = Assets.monsterIdleB.getKeyFrame(elapsed, Animation.ANIMATION_NONLOOPING);
                        break;
                }
                batcher.beginBatch(Assets.monsterB);

                   batcher.drawSprite(position.x + Tile.TILE_HEIGHT, position.y, side * ENEMY_WIDTH, ENEMY_HEIGHT, keyFrame);

                batcher.endBatch();
                break;
            case 2:
                switch (state)
                {

                    case MONSTER_STATE_IDLE:
                        keyFrame = Assets.monsterIdleC.getKeyFrame(elapsed, Animation.ANIMATION_LOOPING);
                        break;
                    case MONSTER_STATE_WALK:
                        keyFrame = Assets.monsterRunC.getKeyFrame(elapsed, Animation.ANIMATION_LOOPING);
                        break;
                    case MONSTER_STATE_KILLED:

                        if(deathTime < deathTimeMax)
                            keyFrame = Assets.monsterIdleC.getKeyFrame(elapsed, Animation.ANIMATION_NONLOOPING);

                        break;
                    default:
                        keyFrame = Assets.monsterIdleC.getKeyFrame(elapsed, Animation.ANIMATION_NONLOOPING);
                        break;
                }
                batcher.beginBatch(Assets.monsterC);

                   batcher.drawSprite(position.x + Tile.TILE_HEIGHT, position.y, side * ENEMY_WIDTH, ENEMY_HEIGHT, keyFrame);

                batcher.endBatch();
                break;
        }
    }
}


