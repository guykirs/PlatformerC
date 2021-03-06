package com.guillaumesoft.platformerc;

import android.view.Display;

import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.impl.GLGame;
import com.badlogic.androidgames.framework.math.Vector2;

import tv.ouya.console.api.OuyaController;

/**
 * Created by Guillaume on 10/6/2014.
 * class ScreenManager
 */
public abstract class ScreenManager
{
    ///////////////////////////////////////////////////////////
    // GET SET FUNCTION FOR DISPLAY SIZE
    public static Display GetDisplay()
    {
        return display;
    }

    public static void SetDisplay(Display value)
    {
        display = value;
    }

    public static Display display;

    ///////////////////////////////////////////////////////////
    // GET SET FUNCTION FOR CAMERA
    public static Camera2D GetCamera()
    {
        return gameCamera;
    }

    public static void SetCamera(Camera2D value)
    {
        gameCamera = value;
    }

    public static Camera2D gameCamera;

    /////////////////////////////////////////////////////////////////////////
    // GET AND SET GAME CONTROLLER ID
    public static OuyaController currentInput;

    public static GLGame game;
}
