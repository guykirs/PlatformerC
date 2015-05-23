package com.guillaumesoft.platformerc;

import android.view.Display;

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
}
