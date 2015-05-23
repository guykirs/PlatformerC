package com.guillaumesoft.platformerc;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.GLGame;

import java.io.InputStream;
import java.security.PublicKey;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import tv.ouya.console.api.OuyaController;
import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.content.OuyaContent;

public class Platformer extends GLGame
{
    /////////////////////////////////////////////////////////////////////////
    // CLASS VARAIBLES
    boolean firstTimeCreate = true;
    boolean firstStart = true;
    private OuyaFacade mOuyaFacade;
    String TAG;
    public  static final String DEVELOPER_ID = "ab58c9eb-0774-4cfc-8309-0c24895fc58f";
    private PublicKey mPublicKey = null;
    private static final String LOG_TAG = "Platformer";

    // CLASS FUNCTION
    @Override
    public Screen getStartScreen()
    {
        return new PressStartScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        super.onSurfaceCreated(gl, config);

        if (firstTimeCreate)
        {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;

            // CREATE A STATIC CONTEXT FOR THE GAME
            //ScreenManager.game = this;

            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            ScreenManager.SetDisplay(wm.getDefaultDisplay());

            /*
                Point size = new Point();
                display.getSize(size);*/

            //ScreenManager.WORLD_WIDTH = size.x;
            //ScreenManager.WORLD_HEIGHT = size.y;
            // }
            // else
            // {
            //Assets.reload();
            // }

        }
        else
        {
            Assets.reload();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return OuyaController.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {

        if (keyCode == OuyaController.BUTTON_A)
        {
            finish();
        }

        if(firstStart)
        {
            // GAME STARTING INITIALIZE THE GAME COMPONENTS
            if (keyCode == OuyaController.BUTTON_O)
            {
                GameScreen gamescreen = new GameScreen(this);
                setScreen(gamescreen);
            }

            firstStart = false;
        }

        return OuyaController.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        return OuyaController.onGenericMotionEvent(event) || super.onGenericMotionEvent(event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void onPause()
    {
        super.onPause();
    }
}
