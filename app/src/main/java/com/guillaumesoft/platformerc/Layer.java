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

    public  TextureRegion[] texturesregion;
    public  Texture[] texture;
    public float ScrollRate;


    public Layer(TextureRegion region, Texture textureLayer, float scrollRate)
    {
        // Assumes each layer only has 3 segments.
        texturesregion = new TextureRegion[3];
        texture = new Texture[3];

        for (int i = 0; i < 3; ++i)
        {
            texturesregion[i] = region;
            texture[i] = textureLayer;
        }

        ScrollRate = scrollRate;
    }

    public void Draw(SpriteBatcher spriteBatch, float cameraPosition)
    {
        Vector2 xy = Vector2.Zero();

        // Assume each segment is the same width.
        int segmentWidth = texturesregion[0].texture.width;

        // Calculate which segments to draw and how much to offset them.
        xy.x  = cameraPosition * ScrollRate;

        int leftSegment = (int)Math.floor(xy.x / segmentWidth);
        int rightSegment = leftSegment + 1;

        xy.x = (xy.x / segmentWidth - leftSegment) * -segmentWidth;

        spriteBatch.beginBatch(texture[leftSegment % texturesregion.length]);

           spriteBatch.drawSprite(xy.x, 1080 / 2, 1920, 1080, texturesregion[leftSegment % texturesregion.length]);

           spriteBatch.drawSprite(xy.x + segmentWidth, 1080 / 2, 1920, 1080, texturesregion[rightSegment % texturesregion.length]);

        spriteBatch.endBatch();

    }
}

