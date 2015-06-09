package com.guillaumesoft.platformerc;

import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.math.Vector2;

/// <summary>
/// This is a game component that implements IUpdateable.
/// </summary>
public class Layer
{
    public  Texture[] texture;
    public  TextureRegion[] region;

    public float GetScrollRate()
    {
        return ScrollRate;
    }
    public void SetScrollRate(float value)
    {
        ScrollRate = value;
    }
    public float ScrollRate;

    public Layer(String Layer, float scrollRate)
    {
        texture = new Texture[3];

        for (int i = 0; i < 3; ++i)
            texture[i] = new Texture(ScreenManager.game, Layer + i + ".png");

        // Assumes each layer only has 3 segments.
        region = new TextureRegion[3];

        for (int i = 0; i < 3; ++i)
            region[i] = new TextureRegion(texture[i] , 0, 0, 512, 512);

        ScrollRate = scrollRate;
    }

    public void Draw(SpriteBatcher spriteBatch, float cameraPosition)
    {
        Vector2 xy = Vector2.Zero();

        // Assume each segment is the same width.
        int segmentWidth = 1920;//region[0].texture.width;

        // Calculate which segments to draw and how much to offset them.
        xy.x  = cameraPosition * ScrollRate;

        int leftSegment = (int)Math.floor(xy.x / segmentWidth);
        int rightSegment = leftSegment + 1;

        xy.x = (xy.x / segmentWidth - leftSegment) * -segmentWidth;

        xy.y = region[0].texture.height;

        spriteBatch.beginBatch(texture[leftSegment % region.length]);

           spriteBatch.drawSprite(xy.x, xy.y, 1920, 1080, region[leftSegment % region.length]);

        spriteBatch.endBatch();

        spriteBatch.beginBatch(texture[rightSegment % region.length]);

           spriteBatch.drawSprite(xy.x + segmentWidth, xy.y, 1920, 1080, region[rightSegment % region.length]);

        spriteBatch.endBatch();
    }
}

