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
    private Texture[]       textures;
    private TextureRegion[] layerRegion;
    public float ScrollRate = 0.2f;
    private final int EntityLayer = 2;

    float[] myIntArray;

    public Layer()
    {
        // Assumes each layer only has 3 segments.
        textures = new Texture[3];
        layerRegion  = new TextureRegion[3];

        for (int i = 0; i <= 2; i++)
        {
            switch (i) {
                case 0:
                    textures[i] = Assets.layer00;
                    layerRegion[i] = Assets.layer00Region;
                    break;
                case 1:
                    textures[i] = Assets.layer10;
                    layerRegion[i] = Assets.layer10Region;
                    break;
                case 2:
                    textures[i] = Assets.layer20;
                    layerRegion[i] = Assets.layer20Region;
                    break;
            }
        }

        myIntArray = new float[3];
        myIntArray = new float[]{0.2f, 0.5f, 0.8f};

    }

    public void Draw(SpriteBatcher spriteBatch, float cameraPosition)//, float cameraPosition)
    {
        Vector2 xy = Vector2.Zero();

        // Assume each segment is the same width.
        int segmentWidth = layerRegion[0].texture.width;



        //spriteBatch.Draw(Textures[leftSegment % Textures.Length], new Vector2(xy.X, 0.0f), Color.White);
        //spriteBatch.Draw(Textures[rightSegment % Textures.Length], new Vector2(xy.X + segmentWidth, 0.0f), Color.White);

        for (int i = 0; i <= EntityLayer; ++i)
        {
            // Calculate which segments to draw and how much to offset them.
            xy.x  = cameraPosition * myIntArray[i];

            int leftSegment = (int)Math.floor(xy.x / segmentWidth);
            int rightSegment = leftSegment + 1;

            xy.x = (xy.x / segmentWidth - leftSegment) * -segmentWidth;

            spriteBatch.beginBatch(textures[i]);

               spriteBatch.drawSprite(xy.x, 1080 / 2, 1920, 1080, layerRegion[leftSegment % textures.length]);
               spriteBatch.drawSprite(xy.x + segmentWidth,  1080 /2, 1920, 1080, layerRegion[rightSegment % textures.length]);

            spriteBatch.endBatch();
        }
    }
}

