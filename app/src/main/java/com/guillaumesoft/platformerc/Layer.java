package com.guillaumesoft.platformerc;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

//////////////////////////////////////////////////////////////////
// October / 21/ 2014
// Guillaume Swolfs
// guillaumesoft
// RatingSplashScreen class
//////////////////////////////////////////////////////////////////
public class Layer
{
    /////////////////////////////////////////
    // CLASS VARAIBLES
    /////////////////////////////////////////
    public Texture[] textures;
    public TextureRegion[] texturesRegion;


    /////////////////////////////////////////
    // CLASS CONSTRUCTOR
    /////////////////////////////////////////
    public Layer()
    {
        textures = new Texture[3];
        for (int i = 0; i < 3; ++i)
        {
            switch(i)
            {
                case 0:
                    textures[i] = Assets.layer00;
                    break;
                case 1:
                    textures[i] = Assets.layer10;
                    break;
                case 2:
                    textures[i] = Assets.layer20;
                    break;
            }
        }

        texturesRegion = new TextureRegion[3];
        for (int i = 0; i < 3; ++i)
        {
            switch(i)
            {
                case 0:
                    texturesRegion[i] = Assets.layer00Region;
                    break;
                case 1:
                    texturesRegion[i] =  Assets.layer10Region;
                    break;
                case 2:
                    texturesRegion[i] =  Assets.layer20Region;
                    break;
            }
        }
    }


    public void update(float deltaTime)
    {

    }


    public void Draw(SpriteBatcher batcher, float camposition)
    {

        batcher.beginBatch(textures[0]);

           batcher.drawSprite(ScreenManager.GetCamera().position.x, ScreenManager.GetCamera().frustumHeight / 2, 1920, 1080, texturesRegion[0]);

           if(camposition > 0)
           {
               batcher.drawSprite(ScreenManager.GetCamera().position.x + 1920, ScreenManager.GetCamera().frustumHeight / 2, 1920, 1080, texturesRegion[0]);
           }

        batcher.endBatch();

        batcher.beginBatch(textures[1]);

           batcher.drawSprite(ScreenManager.GetCamera().position.x, ScreenManager.GetCamera().frustumHeight / 2, 1920, 1080, texturesRegion[1]);

           if(camposition > 0)
           {
               batcher.drawSprite(ScreenManager.GetCamera().position.x + 1920, ScreenManager.GetCamera().frustumHeight / 2, 1920, 1080, texturesRegion[1]);
           }

        batcher.endBatch();

        batcher.beginBatch( textures[2]);

            batcher.drawSprite(ScreenManager.GetCamera().position.x, ScreenManager.GetCamera().frustumHeight / 2, 1920, 1080, texturesRegion[2]);

           if(camposition > 0)
           {
              batcher.drawSprite(ScreenManager.GetCamera().position.x + 1920, ScreenManager.GetCamera().frustumHeight / 2, 1920, 1080, texturesRegion[2]);
           }

        batcher.endBatch();

    }
}


