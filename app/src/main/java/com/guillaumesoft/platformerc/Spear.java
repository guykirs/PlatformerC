package com.guillaumesoft.platformerc;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;

/// <summary>
///  THIS CLASS DRAWS THE GEM TO THE SCREEN
///  THIS PROVIDE THE PLAY POINTS AND HEALTH
///  OCTOBER 23, 2014
///  GUILLAUME SWOLFS
///  GUILLAUMESOFT
/// </summary>
class Spear extends DynamicGameObject
{
    //////////////////////////////////////////////
    // CLASS STATIC VARAIBLES
    public static float SPEAR_WIDTH  = 40.0f;
    public static float SPEAR_HEIGHT = 40.0f;

    //////////////////////////////////////////////
    // CLASS VARAIBLES
    public final int PointValue = 30;
    float stateTime;
    private float bounce;

    /// <summary>
    /// Constructs a new spear.
    /// </summary>
    public Spear(float x, float y)
    {
        super(x, y, SPEAR_WIDTH, SPEAR_HEIGHT);
    }

    /// <summary>
    /// Called when this spear has been collected by a player and removed from the level.
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
        bounce = (float)Math.sin(t) * BounceHeight * SPEAR_HEIGHT;
    }

    /// <summary>
    /// Draws a spear in the appropriate color.
    /// </summary>
    public void Draw(SpriteBatcher batcher)
    {
        batcher.beginBatch(Assets.objects);

           batcher.drawSprite(position.x, position.y + bounce, SPEAR_WIDTH, SPEAR_HEIGHT, Assets.spear);

        batcher.endBatch();
    }
}

