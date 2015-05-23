package com.guillaumesoft.platformerc;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;

/// <summary>
///  THIS CLASS DRAWS THE SKULL TO THE SCREEN
///  THIS PROVIDE THE PLAY POINTS AND HEALTH
///  OCTOBER 23, 2014
///  GUILLAUME SWOLFS
///  GUILLAUMESOFT
/// </summary>
class Skull extends DynamicGameObject
{
    //////////////////////////////////////////////
    // CLASS STATIC VARAIBLES
    public static float GEM_WIDTH  = 40.0f;
    public static float GEM_HEIGHT = 40.0f;

    //////////////////////////////////////////////
    // CLASS VARAIBLES
    public final int PointValue = 30;
    float stateTime;
    private float bounce;

    /// <summary>
    /// Constructs a new skull.
    /// </summary>
    public Skull(float x, float y)
    {
        super(x, y, GEM_WIDTH, GEM_HEIGHT);
    }

    /// <summary>
    /// Called when this skull has been collected by a player and removed from the level.
    /// </summary>
    public void OnCollected()
    {
        Assets.playSound(Assets.gemCollected);
    }

    public void Update(float deltaTime)
    {
        stateTime += deltaTime;

        final float BounceHeight = 0.30f;
        final float BounceRate   = 3.0f;
        final float BounceSync   = -0.75f;

        double t = stateTime * BounceRate + position.x * BounceSync;
        bounce = (float)Math.sin(t) * BounceHeight * GEM_HEIGHT;
    }

    /// <summary>
    /// Draws a skull in the appropriate color.
    /// </summary>
    public void Draw(SpriteBatcher batcher)
    {
        batcher.beginBatch(Assets.objects);

           batcher.drawSprite(position.x, position.y + bounce, GEM_WIDTH, GEM_HEIGHT, Assets.skull);

        batcher.endBatch();
    }
}

