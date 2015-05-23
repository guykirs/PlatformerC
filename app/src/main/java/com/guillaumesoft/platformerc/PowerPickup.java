package com.guillaumesoft.platformerc;

import com.badlogic.androidgames.framework.GameObject;
import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.TextureRegion;

/// <summary>
///  THIS CLASS DRAWS THE POWERPICKUP TO THE SCREEN
///  THIS PROVIDE THE PLAY POINTS AND HEALTH
///  OCTOBER 23, 2014
///  GUILLAUME SWOLFS
///  GUILLAUMESOFT
/// </summary>
class PowerPickup extends GameObject
{
    ////////////////////////////////////////////////////
    // CLASS VARAIBLES
    public static float POWER_WIDTH  = 40.0f;
    public static float POWER_HEIGHT = 42.0f;

    public final int PointValue  = 7;
    float stateTime;

    /// <summary>
    /// Constructs a new gem.
    /// </summary>
    public PowerPickup(float x, float y)
    {
        super(x, y, POWER_WIDTH, POWER_HEIGHT);
    }

    public void OnCollected()
    {
        Assets.playSound(Assets.gemCollected);
    }

    public void Update(float deltaTime)
    {
        stateTime += deltaTime;
    }

    /// <summary>
    /// Draws a gem in the appropriate color.
    /// </summary>
    public void Draw(SpriteBatcher batcher)
    {
        TextureRegion keyFrame;
        keyFrame = Assets.powerPickup.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);

        batcher.beginBatch(Assets.objectsAnimation);

            batcher.drawSprite(position.x, position.y, POWER_WIDTH, POWER_HEIGHT, keyFrame);

        batcher.endBatch();
    }
}

